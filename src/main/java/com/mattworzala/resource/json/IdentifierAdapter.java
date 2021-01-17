package com.mattworzala.resource.json;

import com.google.gson.*;
import com.mattworzala.resource.Identifier;

import java.lang.reflect.Type;

public interface IdentifierAdapter {
    JsonSerializer<Identifier> SERIALIZER = new Serializer() {};
    JsonDeserializer<Identifier> DESERIALIZER = new Deserializer() {};

    interface Serializer extends JsonSerializer<Identifier> {
        @Override
        default JsonElement serialize(Identifier src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.toString(), String.class);
        }
    }

    interface Deserializer extends JsonDeserializer<Identifier> {
        @Override
        default Identifier deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return Identifier.of(context.deserialize(json, String.class));
        }
    }
}
