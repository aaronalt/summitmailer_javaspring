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
  private Customer testCustomer;
  private Customer testCustomer2;
  private Customer testCustomer3;
  private Customer testCustomer4;
  private static String email = "info@test1.com";

  @Autowired MongoClient mongoClient;

  @Value("${spring.mongodb.database}")
  String databaseName;

  @Before
  public void setUp() throws Exception {

    this.dao = new CustomerDao(mongoClient, databaseName);

    this.testCustomer = new Customer().withNewId();
    this.testCustomer.setName("Test 1");
    this.testCustomer.setCountry("Testland");
    this.testCustomer.setWebsite("test1.com");
    this.testCustomer.setEmail("info@test1.com");
    this.testCustomer2 = new Customer().withNewId();
    this.testCustomer2.setName("Test 2");
    this.testCustomer2.setCountry("Testland");
    this.testCustomer2.setWebsite("test2.com");
    this.testCustomer2.setEmail("test2@test2.com");
    this.testCustomer3 = new Customer().withNewId();
    this.testCustomer3.setName("Test 3");
    this.testCustomer3.setCountry("Testlandia");
    this.testCustomer3.setWebsite("test3.io");
    this.testCustomer3.setEmail("test3@test3.io");
    mongoClient
            .getDatabase(databaseName)
            .getCollection("customers")
            .deleteMany(new Document());
  }

  @Test
  public void testAddCustomer() {
    Assert.assertTrue(
            "Should have correctly added customer into db",
            dao.addCustomer(testCustomer)
    );
    Assert.assertEquals(1, dao.getCustomersCount());
    Assert.assertFalse(dao.addCustomer(testCustomer));
  }

  @Test
  public void testGetCustomer() {
    Assert.assertNotEquals(dao.getCustomerByEmail(testCustomer.getId()), testCustomer);
    dao.addCustomer(testCustomer);
    Assert.assertEquals(1, dao.getCustomersCount());
    Assert.assertNotNull(testCustomer.getId());
    Customer customer = dao.getCustomerByEmail(testCustomer.getEmail());
    Assert.assertEquals(customer.getName(), testCustomer.getName());
    Assert.assertEquals(customer.getEmail(), testCustomer.getEmail());
    Assert.assertEquals(customer.getCountry(), testCustomer.getCountry());
    Assert.assertEquals(customer.getWebsite(), testCustomer.getWebsite());
  }

  @Test
  public void testGetAllCustomers() {
    dao.addCustomer(testCustomer);
    dao.addCustomer(testCustomer2);
    dao.addCustomer(testCustomer3);
    int expectedSize = 3;
    List<Customer> customers = dao.getCustomers();
    Assert.assertNotNull(customers);
    Assert.assertEquals(expectedSize, customers.size());
  }

  @Test
  public void testFindCustomerByCountry() {
    dao.addCustomer(testCustomer);
    dao.addCustomer(testCustomer2);
    int expectedSize = 2;
    String country = "Testland";
    List<Customer> cursor = dao.getCustomersByCountry(country);
    int actualSize = 0;
    for (Customer d : cursor) {
      System.out.println(d);
      actualSize++;
    }
    Assert.assertEquals(expectedSize, actualSize);
  }

  @After
  public void tearDown() {
    MongoDatabase db = mongoClient.getDatabase(databaseName);
    db.getCollection("customers").deleteMany(new Document("email", email));
  }
}
