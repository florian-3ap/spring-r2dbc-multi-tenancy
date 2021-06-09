package com.example.r2dbcmultitenancy.config.tenant;

import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import java.util.HashMap;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;
import org.springframework.data.r2dbc.core.DefaultReactiveDataAccessStrategy;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.r2dbc.dialect.DialectResolver;
import org.springframework.data.r2dbc.dialect.R2dbcDialect;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@EnableR2dbcRepositories(
    basePackages = "com.example.r2dbcmultitenancy",
    entityOperationsRef = "tenantEntityTemplate")
@EnableR2dbcAuditing
@RequiredArgsConstructor
public class TenantAwareR2dbcConfig extends AbstractR2dbcConfiguration {

  private final HashMap<Object, Object> tenantConnectionFactoriesMap = new HashMap<>();

  private final R2dbcProperties r2dbcProperties;
  private final TenantProperties tenantProperties;

  @Bean
  ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
    return new R2dbcTransactionManager(connectionFactory);
  }

  @Override
  @Bean("connectionFactory")
  @Qualifier("connectionFactory")
  public ConnectionFactory connectionFactory() {
    TenantAwareConnectionFactory tenantAwareConnectionFactory = new TenantAwareConnectionFactory();
    tenantAwareConnectionFactory.setDefaultTargetConnectionFactory(defaultConnectionFactory());
    tenantAwareConnectionFactory.setTargetConnectionFactories(tenantConnectionFactoriesMap);
    return tenantAwareConnectionFactory;
  }

  @Bean
  public R2dbcEntityOperations tenantEntityTemplate(
      @Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
    return createEntityTemplate(connectionFactory);
  }

  public ConnectionFactory defaultConnectionFactory() {
    return ConnectionFactories.get(
        ConnectionFactoryOptions.builder()
            .from(ConnectionFactoryOptions.parse(r2dbcProperties.getUrl()))
            .option(ConnectionFactoryOptions.USER, r2dbcProperties.getUsername())
            .option(ConnectionFactoryOptions.PASSWORD, r2dbcProperties.getPassword())
            .build());
  }

  private R2dbcEntityOperations createEntityTemplate(ConnectionFactory connectionFactory) {
    R2dbcDialect dialect = DialectResolver.getDialect(connectionFactory);
    DefaultReactiveDataAccessStrategy strategy = new DefaultReactiveDataAccessStrategy(dialect);
    DatabaseClient databaseClient =
        DatabaseClient.builder()
            .connectionFactory(connectionFactory)
            .bindMarkers(dialect.getBindMarkersFactory())
            .build();

    return new R2dbcEntityTemplate(databaseClient, strategy);
  }

  @PostConstruct
  public void initializeTenantDataSources() {
    tenantProperties.getNames().forEach(this::createTenantConnectionFactory);
  }

  public void createTenantConnectionFactory(String tenant) {
    String schemaName = tenant.toLowerCase();
    ConnectionFactory tenantConnectionFactory =
        ConnectionFactories.get(
            ConnectionFactoryOptions.builder()
                .from(
                    ConnectionFactoryOptions.parse(
                        r2dbcProperties.getUrl() + "?schema=" + schemaName))
                .option(ConnectionFactoryOptions.USER, r2dbcProperties.getUsername())
                .option(ConnectionFactoryOptions.PASSWORD, r2dbcProperties.getPassword())
                .build());
    tenantConnectionFactoriesMap.putIfAbsent(schemaName, tenantConnectionFactory);
  }
}
