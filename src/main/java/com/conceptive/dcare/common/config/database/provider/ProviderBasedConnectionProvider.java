package com.conceptive.dcare.common.config.database.provider;

import com.conceptive.dcare.common.dao.ProviderSchemaMapper;
import com.conceptive.dcare.common.dao.ProviderSchemaRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.hibernate.service.UnknownUnwrapTypeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ProviderBasedConnectionProvider implements MultiTenantConnectionProvider {


  @Autowired
  private ProviderSchemaRepository providerConfigRepository;

    @Value("${provider.master.schema}")
    private  String masterSchema;

    @Autowired
    private transient DataSource datasource;

    private ConcurrentHashMap<String,String> providersSchemas= new ConcurrentHashMap();


    @Override
    public Connection getAnyConnection() throws SQLException {
        return datasource.getConnection();
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        connection.close();
    }

    @Override
    public Connection getConnection(String providerIdentifier) throws SQLException {
        log.info("the Provider schema is "+providerIdentifier);

        String providerSchema=providersSchemas.get(providerIdentifier);
        if(providerSchema==null){
            throw new RuntimeException("Provide is Not found");
        }
        Connection connection=getAnyConnection();
        connection.setSchema(providerSchema);
        return connection;
    }

    @Override
    public void releaseConnection(String providerIdentifier, Connection connection) throws SQLException {
        log.info("Releaseing Connection from provider schema "+providerIdentifier);
        connection.setSchema(masterSchema);
        releaseAnyConnection(connection);
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public boolean isUnwrappableAs(Class unwrapType) {
        return MultiTenantConnectionProvider.class.isAssignableFrom(unwrapType);
    }

    @Override
    public <T> T unwrap(Class<T> unwrapType) {
        if ( MultiTenantConnectionProvider.class.isAssignableFrom(unwrapType) ) {
            return (T) this;
        } else {
            throw new UnknownUnwrapTypeException( unwrapType );
        }
    }

    //Put this in Redis Cache later
    @EventListener
    private void loadProviderSchemas(){
        providerConfigRepository
                .findAll()
                .stream()
                .map(ProviderSchemaMapper::newProviderSchema)
                .collect(Collectors.toMap(provider->provider.getProviderId(),provider->provider.getSchema()));
    }

}
