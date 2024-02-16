package com.example.demo.postgres.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.example.demo.postgres.repo", 
    entityManagerFactoryRef = "userEntityManager", 
    transactionManagerRef = "userTransactionManagerBean"
)
public class ConfigurationPostgres {

  @Autowired
  private Environment env;
  // datasource
  @Primary
  @Bean(name = "userDataSourceBean")
  public DataSource userDataSource() {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
      dataSource.setUrl(env.getProperty("spring.datasource.url"));
      dataSource.setUsername(env.getProperty("spring.datasource.username"));
      dataSource.setPassword(env.getProperty("spring.datasource.password"));

      return dataSource;
  }
   
  // entityMangement factory
  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean userEntityManager() {
      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
      em.setDataSource(userDataSource());
      em.setPackagesToScan("com.example.demo.postgres.model");
      HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
      em.setJpaVendorAdapter(vendorAdapter);
      HashMap<String, Object> properties = new HashMap<>();
      properties.put("hibernate.hbm2ddl.auto", "update");
      properties.put("hibernate.show_sql", "true");
      em.setJpaPropertyMap(properties);

      return em;
  }
  
  // platformTransaction Manager
  @Primary
  @Bean(name = "userTransactionManagerBean")
  public PlatformTransactionManager userTransactionManager() {

      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(
        userEntityManager().getObject());
      return transactionManager;
  }
}
