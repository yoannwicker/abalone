package com.app.infrastructure.jpa;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatasourceConfig {

  private static final Logger logger = LoggerFactory.getLogger(DatasourceConfig.class);

  @Bean
  public DataSource datasource(
      @Value("${db.url}") String url,
      @Value("${db.driver}") String driver,
      @Value("${db.username}") String username,
      @Value("${db.password}") String password) {
    logger.info("Starting database ...");

    var hikariConfig = new HikariConfig();
    // hikariConfig.setJdbcUrl(url + ";sendStringParametersAsUnicode=false;trustServerCertificate=true");
    hikariConfig.setJdbcUrl(url);
    hikariConfig.setDriverClassName(driver);
    hikariConfig.setUsername(username);
    hikariConfig.setPassword(password);
    return new HikariDataSource(hikariConfig);
  }
}
