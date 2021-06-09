package com.example.r2dbcmultitenancy.config.tenant;

import org.springframework.r2dbc.connection.lookup.AbstractRoutingConnectionFactory;
import reactor.core.publisher.Mono;

public class TenantAwareConnectionFactory extends AbstractRoutingConnectionFactory {

  @Override
  protected Mono<Object> determineCurrentLookupKey() {
    return TenantResolver.resolve().map(String::toLowerCase);
  }
}
