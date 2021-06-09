package com.example.r2dbcmultitenancy.serializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.r2dbc.postgresql.codec.Json;
import java.io.IOException;
import lombok.experimental.UtilityClass;

@UtilityClass
public class JsonObjectSerializer {

  public static class PgJsonObjectSerializer extends JsonSerializer<Json> {

    @Override
    public Class<Json> handledType() {
      return Json.class;
    }

    @Override
    public void serialize(Json value, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
      JsonFactory factory = new JsonFactory();
      JsonParser parser = factory.createParser(value.asString());
      var node = gen.getCodec().readTree(parser);
      serializers.defaultSerializeValue(node, gen);
    }
  }

  public static class PgJsonObjectDeserializer extends JsonDeserializer<Json> {

    @Override
    public Class<?> handledType() {
      return Json.class;
    }

    @Override
    public Json deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
      var value = ctxt.readTree(p);
      return Json.of(value.toString());
    }
  }
}
