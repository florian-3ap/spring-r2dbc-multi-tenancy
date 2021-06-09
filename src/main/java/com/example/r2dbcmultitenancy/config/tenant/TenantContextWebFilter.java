package com.example.r2dbcmultitenancy.config.tenant;

import static com.example.r2dbcmultitenancy.config.tenant.TenantResolver.DEFAULT_TENANT;
import static com.example.r2dbcmultitenancy.config.tenant.TenantResolver.TENANT_ID_CONTEXT_KEY;

import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class TenantContextWebFilter implements WebFilter {

  private static final String HEADER_NAME = "tenant_id";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    return chain
        .filter(exchange)
        .contextWrite(
            context ->
                context.put(
                    TENANT_ID_CONTEXT_KEY,
                    Optional.ofNullable(exchange.getRequest().getHeaders().getFirst(HEADER_NAME))
                        .orElse(DEFAULT_TENANT)));
  }
}
