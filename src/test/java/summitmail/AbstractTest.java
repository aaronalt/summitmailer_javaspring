package summitmail;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import summitmail.TicketTest;
import org.bson.Document;
import summitmail.models.Customer;

import java.io.IOException;

public abstract class AbstractTest extends TicketTest {
  protected MongoDatabase db;
  protected MongoDatabase testDb;
  protected MongoCollection<Customer> customerCollection;

  public AbstractTest() {
    try {
      String mongoUri = getProperty("spring.mongodb.uri");
      String databaseName = getProperty("spring.mongodb.database");
      db = MongoClients.create(mongoUri).getDatabase(databaseName);
      testDb = MongoClients.create(mongoUri).getDatabase("testDb");

    } catch (IOException e) {
      this.db = null;
    }
  }
}
