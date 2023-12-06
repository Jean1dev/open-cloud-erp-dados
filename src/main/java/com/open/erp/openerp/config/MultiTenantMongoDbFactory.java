package com.open.erp.openerp.config;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.open.erp.openerp.context.TenantContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

import java.util.Objects;

public class MultiTenantMongoDbFactory extends SimpleMongoClientDatabaseFactory {
    public MultiTenantMongoDbFactory(String connectionString) {
        super(connectionString);
    }

    public MultiTenantMongoDbFactory(ConnectionString connectionString) {
        super(connectionString);
    }

    public MultiTenantMongoDbFactory(MongoClient mongoClient, String databaseName) {
        super(mongoClient, databaseName);
    }

    @Override
    public MongoDatabase getMongoDatabase(String dbName) throws DataAccessException {
        String tenantId = TenantContext.getTenantId();
        if (tenantId != null && !tenantId.isBlank()) {
            return super.getMongoDatabase(tenantId + "-erp");
        }

        return super.getMongoDatabase(dbName);
    }
}
