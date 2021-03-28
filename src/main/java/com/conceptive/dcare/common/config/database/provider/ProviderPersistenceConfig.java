package com.conceptive.dcare.common.config.database.provider;

import com.conceptive.dcare.common.util.ProviderIdResolver;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackages = {"com.conceptive.dcare.common.dao.provider"},
                       entityManagerFactoryRef = "providerEntityManagerFactory",
                       transactionManagerRef = "providerTransactionManager")
public class ProviderPersistenceConfig {
    private final JpaProperties jpaProperties;

    @Autowired
    public ProviderPersistenceConfig(JpaProperties jpaProperties) {
        this.jpaProperties=jpaProperties;
    }

    @Autowired
    private ProviderBasedConnectionProvider connectionProvider;

    @Autowired
    private ProviderIdResolver tenantResolver;



    @Bean
    public LocalContainerEntityManagerFactoryBean providerEntityManagerFactory(DataSource datasource){
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        localContainerEntityManagerFactoryBean.setDataSource(datasource);
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("privider-persistence-unit");
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.conceptive.dcare.common.dao.provider");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap(this.jpaProperties.getProperties());
        properties.remove(AvailableSettings.DEFAULT_SCHEMA);
        properties.put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        properties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, connectionProvider);
        properties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantResolver);
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);

        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager providerTransactionManager(@Qualifier("providerEntityManagerFactory") EntityManagerFactory emf){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(emf);
        return jpaTransactionManager;
    }


}
