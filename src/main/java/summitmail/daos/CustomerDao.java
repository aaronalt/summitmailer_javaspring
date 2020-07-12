package summitmail.daos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Component
public class CustomerDao extends AbstractDao {

    public static String CUSTOMERS_COLLECTION = "customers";

    private MongoCollection<Document> customersCollection;

    @Autowired
    public CustomerDao(
            MongoClient mongoClient, @Value("${spring.mongodb.database}") String databaseName) {
        super(mongoClient, databaseName);
        customersCollection = db.getCollection(CUSTOMERS_COLLECTION);
    }

    @SuppressWarnings("unchecked")
    private Bson buildLookupStage() {
        return null;

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
     * @param customerId - customer identifier string.
     * @return Document object or null.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public Document getCustomer(String customerId) {
        if (!validIdValue(customerId)) {
            return null;
        }
        List<Bson> pipeline = new ArrayList<>();
        // match stage to find customer
        Bson match = Aggregates.match(Filters.eq("_id", new ObjectId(customerId)));
        pipeline.add(match);
        Document customer = customersCollection.aggregate(pipeline).first();
        return customer;
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
    public List<Document> getCustomers(int limit, int skip) {
        String defaultSortKey = "tomatoes.viewer.numReviews";
        List<Document> customers =
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
    public List<Document> getCustomers(int limit, int skip, Bson sort) {

        List<Document> customers = new ArrayList<>();

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
    public List<Document> getcustomersByCountry(String... country) {

        Bson queryFilter = new Document();
        Bson projection = new Document();
        //TODO> Ticket: Projection - implement the query and projection required by the unit test
        List<Document> customers = new ArrayList<>();

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
     */
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

    /**
     * Finds all customers that contain any of the `casts` members, sorted in descending by the `sortKey`
     * field.
     *
     * @param sortKey - sort key.
     * @param limit   - number of documents to be returned.
     * @param skip    - number of documents to be skipped.
     * @param cast    - cast selector.
     * @return List of documents sorted by sortKey that match the cast selector.
     */
    public List<Document> getcustomersByCast(String sortKey, int limit, int skip, String... cast) {
        Bson castFilter = null;
        Bson sort = null;
        //TODO> Ticket: Subfield Text Search - implement the expected cast
        // filter and sort
        List<Document> customers = new ArrayList<>();
        customersCollection
                .find(castFilter)
                .sort(sort)
                .limit(limit)
                .skip(skip)
                .iterator()
                .forEachRemaining(customers::add);
        return customers;
    }

    /**
     * Finds all customers that match the provide `genres`, sorted descending by the `sortKey` field.
     *
     * @param sortKey - sorting key string.
     * @param limit   - number of documents to be returned.
     * @param skip    - number of documents to be skipped
     * @param genres  - genres matching string vargs.
     * @return List of matching Document objects.
     */
    public List<Document> getcustomersByGenre(String sortKey, int limit, int skip, String... genres) {
        // query filter
        Bson castFilter = Filters.in("genres", genres);
        // sort key
        Bson sort = Sorts.descending(sortKey);
        List<Document> customers = new ArrayList<>();
        // TODO > Ticket: Paging - implement the necessary cursor methods to support simple
        // pagination like skip and limit in the code below
        customersCollection.find(castFilter).sort(sort).iterator()
        .forEachRemaining(customers::add);
        return customers;
    }

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
     */
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
     */
    private Bson buildFacetStage() {

        return Aggregates.facet(
                new Facet("runtime", buildRuntimeBucketStage()),
                new Facet("rating", buildRatingBucketStage()),
                new Facet("customers", Aggregates.addFields(new Field("title", "$title"))));
    }

    /**
     * Counts the total amount of documents in the `customers` collection
     *
     * @return number of documents in the customers collection.
     */
    public long getcustomersCount() {
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

    /**
     * Counts the number of documents match genres filter.
     *
     * @param genres - genres string vargs.
     * @return number of matching documents.
     */
    public long getGenresSearchCount(String... genres) {
        return this.customersCollection.countDocuments(Filters.in("genres", genres));
    }
}
