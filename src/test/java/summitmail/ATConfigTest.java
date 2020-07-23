package summitmail;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import summitmail.config.MongoDBConfiguration;
import summitmail.daos.ATConfigDao;
import summitmail.daos.UserDao;
import summitmail.models.ATConfig;
import summitmail.models.User;
import summitmail.services.TokenAuthenticationService;
import summitmail.utils.AuthFacade;
import summitmail.utils.UserAuthFacade;

@SpringBootTest(classes = {MongoDBConfiguration.class})
@EnableConfigurationProperties
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
public class ATConfigTest extends AbstractTest {

    @Autowired
    MongoClient mongoClient;

    private AuthFacade authFacade;

    private String user;
    private String cfgName;
    private ATConfigDao dao;
    private ATConfig config;
    // test user
    private UserDao userDao;
    private static String email = "gryffindor@hogwarts.edu";
    private User testUser;
    private String jwt;

    @Value("${spring.mongodb.database}")
    String databaseName;

    @Before
    public void setUp() throws Exception {

        // register test user
        this.userDao = new UserDao(mongoClient, databaseName);
        this.testUser = new User();
        this.testUser.setName("Ichiro Suzuki");
        this.testUser.setEmail(email);
        this.testUser.setHashedpw("somehashedpw");
        this.jwt = "somemagicjwt";
        mongoClient
                .getDatabase(databaseName)
                .getCollection("users")
                .deleteOne(new Document("email", "log@out.com"));
        // create new ATConfig
        this.dao = new ATConfigDao(mongoClient, databaseName);
        this.config = new ATConfig();
        this.config.setApiKey("key1q!Q2w@W3e#E4r$R5t%T");
        this.config.setBaseId("id987654321");
        mongoClient
                .getDatabase(databaseName)
                .getCollection("atconfigs")
                .deleteMany(new Document());
        Authentication auth = authFacade.getAuthentication();
        this.user = auth.getName();
        this.cfgName = "testConfig";
    }

    @Test
    public void testInsertNewConfig() {
        Assert.assertTrue(
                "Should have correctly added new config into DB - check ATConfigDao.addConfig()",
                dao.addConfig(config));
    }

    @After
    public void tearDown() {
        MongoDatabase db = mongoClient.getDatabase(databaseName);
        db.getCollection("atconfigs").deleteMany(new Document("name", cfgName));
    }
}
