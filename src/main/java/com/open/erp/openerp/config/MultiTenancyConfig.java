package com.open.erp.openerp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;

@Configuration
@AutoConfigureBefore(MongoAutoConfiguration.class)
public class MultiTenancyConfig {

    @Value("${spring.data.mongodb.uri}")
    private String connectionString;

    @Bean
    @Primary
    public MongoDatabaseFactory mongoDatabaseFactory() {
        return new MultiTenantMongoDbFactory(connectionString);
    }
}
