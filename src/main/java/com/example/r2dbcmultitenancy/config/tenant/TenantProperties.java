package com.example.r2dbcmultitenancy.config.tenant;

import java.util.Arrays;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "tenants")
@Data
public class TenantProperties {

  private String[] names;

  public List<String> getNames() {
    return Arrays.asList(names);
  }
}
