package summitmail;

import com.mongodb.ConnectionString;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import summitmail.config.MongoDBConfiguration;
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

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = {CustomerDao.class, MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class TimeoutsTest extends TicketTest {

  @Autowired MongoClient mongoClient;
  private String mongoUri;

  @Value("${spring.mongodb.database}")
  String databaseName;

  private CustomerDao customerDao;

  @Before
  public void setUp() throws IOException {
    this.customerDao = new CustomerDao(mongoClient, databaseName);
    mongoUri = getProperty("spring.mongodb.uri");
    mongoClient = MongoClients.create(mongoUri);
  }

  @Test
  public void testConfiguredWtimeout() {
    WriteConcern wc =
            this.customerDao.mongoClient.getDatabase(databaseName).getWriteConcern();

    Assert.assertNotNull(wc);
    int actual = wc.getWTimeout(TimeUnit.MILLISECONDS);
    int expected = 2500;
    Assert.assertEquals("Configured `wtimeoutms` not set has expected", expected, actual);
  }

  @Test
  public void testConfiguredConnectionTimeoutMs() {
    ConnectionString connectionString = new ConnectionString(mongoUri);
    int expected = 2000;
    int actual = connectionString.getConnectTimeout();

    Assert.assertEquals(
        "Configured `connectionTimeoutMS` does not match expected", expected, actual);
  }
}
