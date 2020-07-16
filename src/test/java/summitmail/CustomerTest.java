package summitmail;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.types.ObjectId;
import org.junit.After;
import summitmail.config.MongoDBConfiguration;
import org.bson.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import summitmail.daos.CustomerDao;
import summitmail.models.Customer;
import summitmail.services.CustomerService;
import summitmail.utils.CustomerCodec;

import java.util.ArrayList;
import java.util.List;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@SpringBootTest(classes = {MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerTest extends AbstractTest {

  private CustomerDao dao;
  private CustomerService customerService;
  private Customer testCustomer;
  private ObjectId customer1Id;
  private ObjectId customer2Id;
  private static String email = "info@test1.com";
  @Autowired MongoClient mongoClient;

  @Value("${spring.mongodb.database}")
  String databaseName;

  @Before
  public void setUp() throws Exception {
    //CustomerCodec customerCodec = new CustomerCodec();
    //CodecRegistry codecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(), fromCodecs(customerCodec));
    //MongoCollection<Customer> testCollection = testDb.getCollection("customers", Customer.class).withCodecRegistry(codecRegistry);
    this.dao = new CustomerDao(mongoClient, databaseName);
    this.customerService = new CustomerService();
    this.testCustomer = new Customer().withNewId();
    this.testCustomer.setName("Test 1");
    this.testCustomer.setCountry("Testland");
    this.testCustomer.setWebsite("test1.com");
    this.testCustomer.setEmail("info@test1.com");
    mongoClient
            .getDatabase(databaseName)
            .getCollection("customers");
  }

  @After
  public void tearDown() {
    MongoDatabase db = mongoClient.getDatabase(databaseName);
    db.getCollection("customers").deleteMany(new Document("email", email));
  }

  @Test
  public void testAddCustomer() {
    Assert.assertTrue(
            "Should have correctly added customer into db",
            dao.addCustomer(testCustomer)
    );
  }

  @Test
  public void testGetCustomer() {
    Assert.assertNotEquals(dao.getCustomer(testCustomer.getId()), testCustomer);
    dao.addCustomer(testCustomer);
    Customer customer = customerService.getCustomer(testCustomer.getId());
    Assert.assertFalse(customer.isEmpty());
  }

  @Test
  public void testFindCustomerByCountry() {
    int expectedSize = 2;
    String country = "Testland";
    Iterable<Customer> cursor = dao.getCustomersByCountry(country);
    int actualSize = 0;
    for (Customer d : cursor) {
      System.out.println(d);
      actualSize++;
    }

    Assert.assertEquals(
        "Unexpected number of returned movie documents. Check your query filter",
        expectedSize,
        actualSize);
  }
}
