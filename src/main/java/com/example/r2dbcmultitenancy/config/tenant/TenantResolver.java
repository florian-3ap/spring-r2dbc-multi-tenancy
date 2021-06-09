package com.example.r2dbcmultitenancy.config.tenant;

import lombok.experimental.UtilityClass;
import reactor.core.publisher.Mono;

@UtilityClass
public class TenantResolver {

  public static final String DEFAULT_TENANT = "public";
  public static final String TENANT_ID_CONTEXT_KEY = "tenant_id";

  public static Mono<String> resolve() {
    return Mono.deferContextual(
        contextView ->
            contextView
                .getOrEmpty(TENANT_ID_CONTEXT_KEY)
                .map(value -> Mono.just(String.valueOf(value)))
                .orElseGet(() -> Mono.just(DEFAULT_TENANT)));
  }
}
