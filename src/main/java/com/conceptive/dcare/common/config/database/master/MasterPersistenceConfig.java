package com.conceptive.dcare.common.config.database.master;

import com.conceptive.dcare.common.model.ProviderContext;
import com.conceptive.dcare.common.util.ProviderIdResolver;
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
@EnableJpaRepositories(basePackages = {"com.conceptive.dcare.common.dao.master"},entityManagerFactoryRef = "masterEntityManagerFactory",
                        transactionManagerRef = "masterTransactionManager")
public class MasterPersistenceConfig {
    private final JpaProperties jpaProperties;

    @Autowired
    public MasterPersistenceConfig(JpaProperties jpaProperties) {
        this.jpaProperties = jpaProperties;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(DataSource datasource){
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        localContainerEntityManagerFactoryBean.setDataSource(datasource);
        localContainerEntityManagerFactoryBean.setPersistenceUnitName("master-persistence-unit");
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.conceptive.dcare.common.dao.master");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap(this.jpaProperties.getProperties());
        localContainerEntityManagerFactoryBean.setJpaPropertyMap(properties);

        return localContainerEntityManagerFactoryBean;
    }

    @Bean
    public JpaTransactionManager masterTransactionManager(@Qualifier("masterEntityManagerFactory")EntityManagerFactory emf){
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setEntityManagerFactory(emf);
        return jpaTransactionManager;
    }
}
