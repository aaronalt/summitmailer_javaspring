package summitmail;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
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

@SpringBootTest(classes = {MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class CustomerTest extends TicketTest {

  private CustomerDao dao;
  private Customer testCustomer;
  private static String email = "info@test1.com";
  @Autowired MongoClient mongoClient;

  @Value("${spring.mongodb.database}")
  String databaseName;

  @Before
  public void setup() {

    this.dao = new CustomerDao(mongoClient, databaseName);
    this.testCustomer = new Customer();
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
  public void testFindMoviesByCountry() {
    int expectedSize = 2;
    String country = "Kosovo";
    Iterable<Document> cursor = dao.getCustomersByCountry(country);
    int actualSize = 0;
    for (Document d : cursor) {
      System.out.println(d);
      actualSize++;
    }

    Assert.assertEquals(
        "Unexpected number of returned movie documents. Check your query filter",
        expectedSize,
        actualSize);
  }
}
