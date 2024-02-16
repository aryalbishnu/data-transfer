package com.example.demo.mysql.config;

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
    basePackages = "com.example.demo.mysql.repo", 
    entityManagerFactoryRef = "secondEntityManager", 
    transactionManagerRef = "seconduserTransactionManagerBean"
)
public class ConfigurationMysql {
  
  @Autowired
  private Environment env;
  // datasource
  @Primary
  @Bean(name = "seconduserDataSourceBean")
  public DataSource userDataSource() {
      DriverManagerDataSource dataSource = new DriverManagerDataSource();
      dataSource.setDriverClassName(env.getProperty("second.datasource.driver-class-name"));
      dataSource.setUrl(env.getProperty("second.datasource.url"));
      dataSource.setUsername(env.getProperty("second.datasource.username"));
      dataSource.setPassword(env.getProperty("second.datasource.password"));

      return dataSource;
  }
   
  // entityMangement factory
  @Bean
  @Primary
  public LocalContainerEntityManagerFactoryBean secondEntityManager() {
      LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
      em.setDataSource(userDataSource());
      em.setPackagesToScan("com.example.demo.mysql.model");
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
  @Bean(name = "seconduserTransactionManagerBean")
  public PlatformTransactionManager userTransactionManager() {

      JpaTransactionManager transactionManager = new JpaTransactionManager();
      transactionManager.setEntityManagerFactory(
          secondEntityManager().getObject());
      return transactionManager;
  }


}
