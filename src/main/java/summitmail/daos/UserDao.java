package summitmail.daos;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import summitmail.models.Session;
import summitmail.models.User;


import java.util.List;
import java.util.Map;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class UserDao extends AbstractDao {

    private final MongoCollection<User> usersCollection;
    private final MongoCollection<Session> sessionsCollection;

    private final Logger log;

    @Autowired
    public UserDao(
            MongoClient mongoClient, @Value("${spring.mongodb.database}") String databaseName) {
        super(mongoClient, databaseName);
        CodecRegistry pojoCodecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));

        usersCollection = db.getCollection("users", User.class).withCodecRegistry(pojoCodecRegistry);
        log = LoggerFactory.getLogger(this.getClass());
        sessionsCollection = db.getCollection("sessions", Session.class).withCodecRegistry(pojoCodecRegistry);
    }

    /**
     * Inserts the `user` object in the `users` collection.
     *
     * @param user - User object to be added
     * @return True if successful, throw IncorrectDaoOperation otherwise
     */
    public boolean addUser(User user) {
        if (!user.isEmpty()) {
            usersCollection.insertOne(user);
            return true;
        }
        // Ticket: Handling Errors - make sure to only add new users
        // and not users that already exist.
        else return false;
    }

    /**
     * Creates session using userId and jwt token.
     *
     * @param userId - user string identifier
     * @param jwt    - jwt string token
     * @return true if successful
     */
    public boolean createUserSession(String userId, String jwt) {
        //Ticket: User Management - implement the method that allows session information to be
        // stored in it's designated collection.
        Session userSession = new Session();
        userSession.setUserId(userId);
        userSession.setJwt(jwt);
        sessionsCollection.insertOne(userSession);
        return true;
        //Ticket: Handling Errors - implement a safeguard against
        // creating a session with the same jwt token.
    }

    /**
     * Returns the User object matching the an email string value.
     *
     * @param email - email string to be matched.
     * @return User object or null.
     */
    public User getUser(String email) {
        User user = null;
        //Ticket: User Management - implement the query that returns the first User object.
        Bson queryFilter = new Document("email", email);
        User userFound = usersCollection.find(queryFilter).iterator().tryNext();
        return userFound;
    }

    /**
     * Given the userId, returns a Session object.
     *
     * @param userId - user string identifier.
     * @return Session object or null.
     */
    public Session getUserSession(String userId) {
        //Ticket: User Management - implement the method that returns Sessions for a given
        // userId
        Bson sessionFilter = new Document("user_id", userId);
        Session sessionFound = sessionsCollection.find(sessionFilter).iterator().tryNext();
        return sessionFound;
    }

    public boolean deleteUserSessions(String userId) {
        //Ticket: User Management - implement the delete user sessions method
        Bson sessionFilter = new Document("user_id", userId);
        sessionsCollection.deleteMany(sessionFilter);
        return false;
    }

    /**
     * Removes the user document that match the provided email.
     *
     * @param email - of the user to be deleted.
     * @return true if user successfully removed
     */
    public boolean deleteUser(String email) {
        //Ticket: User Management - implement the delete user method
        //Ticket: Handling Errors - make this method more robust by
        // handling potential exceptions.
        Bson userQuery = new Document("email", email);
        usersCollection.deleteOne(userQuery);
        deleteUserSessions(email);
        return true;
    }
}
