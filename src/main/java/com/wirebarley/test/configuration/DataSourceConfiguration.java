package com.wirebarley.test.configuration;

import com.zaxxer.hikari.HikariDataSource;
import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@EnableConfigurationProperties({
  DataSourceConfiguration.DatasourceProperties.class,
})
public class DataSourceConfiguration {

  @Bean
  @Primary
  public HikariDataSource hikariDataSource(@NonNull final DatasourceProperties properties) {
    final var hikariDataSource = new HikariDataSource();
    hikariDataSource.setJdbcUrl(properties.getUrl());
    hikariDataSource.setUsername(properties.getUsername());
    hikariDataSource.setPassword(properties.getPassword());
    return hikariDataSource;
  }

  @Bean
  @FlywayDataSource
  public HikariDataSource flywayDataSource(@NonNull final DatasourceProperties properties) {
    final var hikariDataSource = new HikariDataSource();
    hikariDataSource.setJdbcUrl(properties.getUrl());
    hikariDataSource.setUsername(properties.getUsername());
    hikariDataSource.setPassword(properties.getPassword());
    return hikariDataSource;
  }

  @Data
  @ConfigurationProperties(prefix = "spring.datasource")
  public static class DatasourceProperties {
    private String username;
    private String password;
    private String url;
  }
}
