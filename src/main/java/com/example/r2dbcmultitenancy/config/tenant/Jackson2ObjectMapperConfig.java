package com.example.r2dbcmultitenancy.config.tenant;

import com.example.r2dbcmultitenancy.serializer.JsonObjectSerializer.PgJsonObjectDeserializer;
import com.example.r2dbcmultitenancy.serializer.JsonObjectSerializer.PgJsonObjectSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class Jackson2ObjectMapperConfig {

  @Bean
  public ObjectMapper objectMapper() {
    return Jackson2ObjectMapperBuilder.json()
        .modulesToInstall(JavaTimeModule.class)
        .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .serializers(new PgJsonObjectSerializer())
        .deserializers(new PgJsonObjectDeserializer())
        .build();
  }
}
