package summitmail.daos;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import summitmail.models.Customer;
import summitmail.utils.CustomerCodec;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.*;

@Component
public class CustomerDao extends AbstractDao {

    private final MongoCollection<Customer> customersCollection;

    @Autowired
    public CustomerDao(
            MongoClient mongoClient, @Value("${spring.mongodb.database}") String databaseName) {
        super(mongoClient, databaseName);
        CustomerCodec customerCodec = new CustomerCodec();
        CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromCodecs(customerCodec));
        // CodecRegistry pojoCodecRegistry =
        //        fromRegistries(
        //                MongoClientSettings.getDefaultCodecRegistry(),
        //                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        customersCollection = db.getCollection("customers", Customer.class).withCodecRegistry(codecRegistry);
    }

    public long sizeOfCollection() {
        return customersCollection.countDocuments();
    }

    @SuppressWarnings("unchecked")
    private Bson buildLookupStage() {
        return null;
    }

    /**
     * Inserts Customer object into the 'customers' collection.
     *
     * @param customer - Customer object to be added
     * @return True if successful, throw IncorrectDaoOperation otherwise
     */
    public boolean addCustomer(Customer customer) {
        if (!customer.isEmpty()) {
            customersCollection.insertOne(customer);
            return true;
        }
        else return false;
    }

    /**
     * customerId needs to be a hexadecimal string value. Otherwise it won't be possible to translate to
     * an ObjectID
     *
     * @param customerId - customer object identifier
     * @return true if valid customerId.
     */
    private boolean validIdValue(String customerId) {
        //Ticket: Handling Errors - implement a way to catch a
        //any potential exceptions thrown while validating a customer id.
        //Check out this method's use in the method that follows.
        boolean isHex;
        try {
            int x = Integer.parseInt(customerId, 16);
            isHex = true;
        } catch (NumberFormatException nfe) {
            isHex = false;
        }
        return isHex;
    }

    /**
     * Gets a customer object from the database.
     *
     * @param email - customer identifier string.
     * @return Document object or null.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public Customer getCustomer(String email) {
        // if (!validIdValue(customerId)) { return null; }
        Bson queryFilter = new Document("email", email);
        Customer found = customersCollection.find(queryFilter).iterator().tryNext();
        return found;
    }

    /**
     * Returns all customers within the defined limit and skip values using a default descending sort key
     * `tomatoes.viewer.numReviews`
     *
     * @param limit - max number of returned documents.
     * @param skip  - number of documents to be skipped.
     * @return list of documents.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public List<Customer> getCustomers(int limit, int skip) {
        String defaultSortKey = "tomatoes.viewer.numReviews";
        List<Customer> customers =
                new ArrayList<>(getCustomers(limit, skip, Sorts.descending(defaultSortKey)));
        return customers;
    }

    /**
     * Finds a limited amount of customers documents, for a given sort order.
     *
     * @param limit - max number of documents to be returned.
     * @param skip  - number of documents to be skipped.
     * @param sort  - result sorting criteria.
     * @return list of documents that sorted by the defined sort criteria.
     */
    public List<Customer> getCustomers(int limit, int skip, Bson sort) {

        List<Customer> customers = new ArrayList<>();

        customersCollection
                .find()
                .limit(limit)
                .skip(skip)
                .sort(sort)
                .iterator()
                .forEachRemaining(customers::add);

        return customers;
    }

    /**
     * For a given a country, return all the customers that match that country.
     *
     * @param country - Country string value to be matched.
     * @return List of matching Document objects.
     */
    public ArrayList<Customer> getCustomersByCountry(String... country) {

        Bson queryFilter = new Document("country", country);
        ArrayList<Customer> customers = new ArrayList<>();
        Iterable<Customer> customersFound = customersCollection.find(queryFilter);
        for (Customer customer : customersFound) {
            customers.add(customer);
        }
        return customers;
    }

    /**
     * This method will execute the following mongo shell query: db.customers.find({"$text": { "$search":
     * `keywords` }}, {"score": {"$meta": "textScore"}}).sort({"score": {"$meta": "textScore"}})
     *
     * @param limit    - integer value of number of documents to be limited to.
     * @param skip     - number of documents to be skipped.
     * @param keywords - text matching keywords or terms
     * @return List of query matching Document objects
     *
    public List<Document> getcustomersByText(int limit, int skip, String keywords) {
        Bson textFilter = Filters.text(keywords);
        Bson projection = Projections.metaTextScore("score");
        Bson sort = Sorts.metaTextScore("score");
        List<Document> customers = new ArrayList<>();
        customersCollection
                .find(textFilter)
                .projection(projection)
                .sort(sort)
                .skip(skip)
                .limit(limit)
                .iterator()
                .forEachRemaining(customers::add);
        return customers;
    }
    */

    private ArrayList<Integer> runtimeBoundaries() {
        ArrayList<Integer> runtimeBoundaries = new ArrayList<>();
        runtimeBoundaries.add(0);
        runtimeBoundaries.add(60);
        runtimeBoundaries.add(90);
        runtimeBoundaries.add(120);
        runtimeBoundaries.add(180);
        return runtimeBoundaries;
    }

    private ArrayList<Integer> ratingBoundaries() {
        ArrayList<Integer> ratingBoundaries = new ArrayList<>();
        ratingBoundaries.add(0);
        ratingBoundaries.add(50);
        ratingBoundaries.add(70);
        ratingBoundaries.add(90);
        ratingBoundaries.add(100);
        return ratingBoundaries;
    }

    /**
     * This method is the java implementation of the following mongo shell aggregation pipeline {
     * "$bucket": { "groupBy": "$runtime", "boundaries": [0, 60, 90, 120, 180], "default": "other",
     * "output": { "count": {"$sum": 1} } } }
     */
    private Bson buildRuntimeBucketStage() {

        BucketOptions bucketOptions = new BucketOptions();
        bucketOptions.defaultBucket("other");
        BsonField count = new BsonField("count", new Document("$sum", 1));
        bucketOptions.output(count);
        return Aggregates.bucket("$runtime", runtimeBoundaries(), bucketOptions);
    }

    /*
    This method is the java implementation of the following mongo shell aggregation pipeline
    {
     "$bucket": {
       "groupBy": "$metacritic",
       "boundaries": [0, 50, 70, 90, 100],
       "default": "other",
       "output": {
       "count": {"$sum": 1}
       }
      }
     }
     */
    private Bson buildRatingBucketStage() {
        BucketOptions bucketOptions = new BucketOptions();
        bucketOptions.defaultBucket("other");
        BsonField count = new BsonField("count", new Document("$sum", 1));
        bucketOptions.output(count);
        return Aggregates.bucket("$metacritic", ratingBoundaries(), bucketOptions);
    }


    /**
     * This method is the java implementation of the following mongo shell aggregation pipeline
     * pipeline.aggregate([ {$match: {cast: {$in: ... }}}, {$sort: {tomatoes.viewer.numReviews: -1}},
     * {$skip: ... }, {$limit: ... }, {$facet:{ runtime: {$bucket: ...}, rating: {$bucket: ...},
     * customers: {$addFields: ...}, }} ])
     *
    public List<Document> getcustomersCastFaceted(int limit, int skip, String... cast) {
        List<Document> customers = new ArrayList<>();
        String sortKey = "tomatoes.viewer.numReviews";
        Bson skipStage = Aggregates.skip(skip);
        Bson matchStage = Aggregates.match(Filters.in("cast", cast));
        Bson sortStage = Aggregates.sort(Sorts.descending(sortKey));
        Bson limitStage = Aggregates.limit(limit);
        Bson facetStage = buildFacetStage();
        // Using a LinkedList to ensure insertion order
        List<Bson> pipeline = new LinkedList<>();

        // TODO > Ticket: Faceted Search - build the aggregation pipeline by adding all stages in the
        // correct order
        // Your job is to order the stages correctly in the pipeline.
        // Starting with the `matchStage` add the remaining stages.
        pipeline.add(matchStage);

        customersCollection.aggregate(pipeline).iterator().forEachRemaining(customers::add);
        return customers;
    }

    /**
     * This method is the java implementation of the following mongo shell aggregation pipeline
     * pipeline.aggregate([ ..., {$facet:{ runtime: {$bucket: ...}, rating: {$bucket: ...}, customers:
     * {$addFields: ...}, }} ])
     *
     * @return Bson defining the $facet stage.
     *
    private Bson buildFacetStage() {

        return Aggregates.facet(
                new Facet("runtime", buildRuntimeBucketStage()),
                new Facet("rating", buildRatingBucketStage()),
                new Facet("customers", Aggregates.addFields(new Field("title", "$title"))));
    }

    */

    /**
     * Counts the total amount of documents in the `customers` collection
     *
     * @return number of documents in the customers collection.
     */
    public long getCustomersCount() {
        return this.customersCollection.countDocuments();
    }

    /**
     * Counts the number of documents matched by this text query
     *
     * @param keywords - set of keywords that match the query
     * @return number of matching documents.
     */
    public long getTextSearchCount(String keywords) {
        return this.customersCollection.countDocuments(Filters.text(keywords));
    }

    /**
     * Counts the number of documents matched by this cast elements
     *
     * @param cast - cast string vargs.
     * @return number of matching documents.
     */
    public long getCastSearchCount(String... cast) {
        return this.customersCollection.countDocuments(Filters.in("cast", cast));
    }

}
