package com.my.offerservice.config;

import com.my.offerservice.repository.OfferRepository;
import com.my.offerservice.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(
        transactionManagerRef = "merchantDBTransactionManager",
        entityManagerFactoryRef = "merchantDBEntityManagerFactory",
        basePackageClasses = {
                ProductRepository.class,
                OfferRepository.class
        }
)
@EnableTransactionManagement
public class DataBaseConfig {

    public static final String MODEL_PACKAGE = "com.my.offerservice.model";

    @Bean
    public Database merchantDatabase(@Value("${merchant.datasource.vendor}") String databaseVendor) {
        return Database.valueOf(databaseVendor);
    }

    @Bean
    public DataSource merchantDataSource(@Value("${merchant.datasource.url}") String url,
                                         @Value("${merchant.datasource.username}") String user,
                                         @Value("${merchant.datasource.password}") String pass) {
        return new DriverManagerDataSource(url, user, pass);
    }

    @Bean
    public PlatformTransactionManager merchantDBTransactionManager(
            @Qualifier("merchantDBEntityManagerFactory") final EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean merchantDBEntityManagerFactory(
            @Qualifier("merchantDataSource") final DataSource dataSource,
            @Qualifier("merchantJpaVendorAdapter") final JpaVendorAdapter jpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setPackagesToScan(MODEL_PACKAGE);
        factoryBean.setPersistenceUnitName("merchant");
        return factoryBean;
    }

    @Bean
    public JpaVendorAdapter merchantJpaVendorAdapter(@Qualifier("merchantDatabase") final Database database,
                                                     @Value("${merchant.jpa.schemaUpdate}") final boolean schemaUpdate,
                                                     @Value("${merchant.jpa.show-sql:false}") final boolean showSQl) {

        HibernateJpaVendorAdapter hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
        hibernateJpaVendorAdapter.setGenerateDdl(schemaUpdate);
        hibernateJpaVendorAdapter.setDatabase(database);
        hibernateJpaVendorAdapter.setShowSql(showSQl);
        return hibernateJpaVendorAdapter;
    }


}
