package com.mattworzala.resource.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.mattworzala.resource.Identifier;
import com.mattworzala.resource.Resource;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class VarResourceMap<R extends Resource> implements JsonDeserializer<Map<Identifier, R>> {

    private final Class<R> type;

    protected VarResourceMap(Class<R> type) {
        this.type = type;
    }

    @Override
    public Map<Identifier, R> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            // Deserialize as list
            final Map<Identifier, R> items = new HashMap<>();
            for (JsonElement child : json.getAsJsonArray()) {
                R resource = context.deserialize(child, this.type);
                items.put(resource.getId(), resource);
            }
            return Collections.unmodifiableMap(items);
        } else {
            // Try to deserialize as a single item
            R resource = context.deserialize(json, this.type);
            return Collections.singletonMap(resource.getId(), resource);
        }
    }
}
