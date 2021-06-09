package com.example.r2dbcmultitenancy.config;

import com.example.r2dbcmultitenancy.config.tenant.TenantProperties;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(FlywayProperties.class)
public class FlywayConfig {

  private final FlywayProperties flywayProperties;
  private final TenantProperties tenantProperties;

  @PostConstruct
  public void migrateFlyway() {
    tenantProperties.getNames().forEach(tenant -> createFlyway(tenant).migrate());
  }

  public Flyway createFlyway(String tenantId) {
    final var schemaName = tenantId.toLowerCase();
    return new Flyway(
        Flyway.configure()
            .baselineVersion(flywayProperties.getBaselineVersion())
            .baselineOnMigrate(flywayProperties.isBaselineOnMigrate())
            .schemas(schemaName)
            .defaultSchema(schemaName)
            .dataSource(flywayDataSource()));
  }

  public DataSource flywayDataSource() {
    return DataSourceBuilder.create(this.getClass().getClassLoader())
        .url(flywayProperties.getUrl())
        .username(flywayProperties.getUser())
        .password(flywayProperties.getPassword())
        .build();
  }
}
