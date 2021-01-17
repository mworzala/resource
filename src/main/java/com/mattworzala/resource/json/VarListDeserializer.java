package com.mattworzala.resource.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Used to deserialize a variable length array to an immutable {@link List}.
 * <p>
 * VarList should be applied
 * to relevant fields with the {@link com.google.gson.annotations.JsonAdapter} rather than
 * being added to the {@link com.google.gson.Gson} instance to avoid conflict with other
 * List parsing due to type erasure.
 * <p>
 * The parsed list is immutable.
 * <p>
 * For example, given the following schema:
 * <p><pre>
 *  class ExampleResource {
 *      private List<String> names;
 *  }
 * </pre></p>
 *
 * It may be helpful to support a single name or multiple names as follows:
 * <p><pre>
 *  [
 *      {
 *          "names": ["John Smith", "Jane Smith"]
 *      },
 *      {
 *          "names": "John Smith"
 *      }
 *  ]
 * </pre></p>
 *
 * Some common types are implemented as subclasses.
 */
public abstract class VarListDeserializer<T> implements JsonDeserializer<List<T>> {
    public static class String extends VarListDeserializer<java.lang.String> {
        public String() { super(java.lang.String.class); }
    }
    //todo other common uses

    private final Class<T> type;

    protected VarListDeserializer(Class<T> type) {
        this.type = type;
    }

    @Override
    public List<T> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (json.isJsonArray()) {
            // Deserialize as list
            final List<T> items = new ArrayList<>();
            for (JsonElement child : json.getAsJsonArray())
                items.add(context.deserialize(child, this.type));
            return Collections.unmodifiableList(items);
        } else {
            // Try to deserialize as a single item
            return Collections.singletonList(context.deserialize(json, this.type));
        }
    }
}
