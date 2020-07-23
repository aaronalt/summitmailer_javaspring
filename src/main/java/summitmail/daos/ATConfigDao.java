package summitmail.daos;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import summitmail.models.ATConfig;
import summitmail.models.User;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@Configuration
public class ATConfigDao extends AbstractDao {

    private final MongoCollection<ATConfig> atconfigCollection;

    @Autowired
    public ATConfigDao(MongoClient mongoClient, @Value("${spring.mongo.database}") String databaseName) {
        super(mongoClient, databaseName);
        CodecRegistry pojoCodecRegistry =
                fromRegistries(
                        MongoClientSettings.getDefaultCodecRegistry(),
                        fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        atconfigCollection = db.getCollection("atconfigs", ATConfig.class).withCodecRegistry(pojoCodecRegistry);
    }

    public boolean addConfig(ATConfig config) {
        if (config.hasApiKey() && config.hasBaseId()) {
            atconfigCollection.insertOne(config);
            return true;
        } else return false;
    }


}
