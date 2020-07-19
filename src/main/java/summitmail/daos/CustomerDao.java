package summitmail.daos;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
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
import static com.mongodb.client.model.Filters.*;

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
        customersCollection = db.getCollection("customers", Customer.class).withCodecRegistry(codecRegistry);
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
        try {
            if (!customer.isEmpty()) {
                customersCollection.insertOne(customer);
                return true;
            }
            else return false;
        } catch (Exception e) {
            return false;
        }
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
        List<Customer> customers =
                new ArrayList<>(getCustomers(limit, skip, Sorts.descending()));
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
    public List<Customer> getCustomersByCountry(String... country) {

        List<Customer> customers = new ArrayList<>();
        Bson qFilter = all("country", country);
        customersCollection.find(qFilter).into(customers);
        return customers;
    }

    /**
     * Counts the total amount of documents in the `customers` collection
     *
     * @return number of documents in the customers collection.
     */
    public long getCustomersCount() {
        return this.customersCollection.countDocuments();
    }
}
