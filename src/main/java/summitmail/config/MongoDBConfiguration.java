package summitmail.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableMongoAuditing
@Service
public class MongoDBConfiguration {

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
    public MongoClient mongoClient(@Value("${spring.mongodb.uri}") String connectionString) {

        ConnectionString connString = new ConnectionString(connectionString);

        MongoClientSettings settings = MongoClientSettings.builder()
                .writeConcern(WriteConcern.MAJORITY.withWTimeout(2500, TimeUnit.MILLISECONDS))
                .applyConnectionString(connString).build();
        //Ticket: Handling Timeouts - configure the expected
        // WriteConcern `wtimeout` and `connectTimeoutMS` values
        MongoClient mClient = MongoClients.create(settings);

        return mClient;
    }
}