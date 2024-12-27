package org.example.trainerworkloadservice.configuration;

import com.mongodb.client.MongoClient;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class MongoDbInitializer {

    @Bean
    public CommandLineRunner cleanDatabase(MongoClient mongoClient) {
        return args -> {
            mongoClient.getDatabase("test-db").drop();
        };
    }
}
