package com.mattworzala.resource.json.process;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Applies various effects to objects after being deserialized.
 * <p>
 * The implementation is loosely based on
 * <a href="https://medium.com/mobile-app-development-publication/post-processing-on-gson-deserialization-26ce5790137d">this</a>
 * Medium article.
 * <p>
 * Objects which inherit from {@link PostProcessed} will have their processing method
 * called, and they will have all {@link Required} fields inspected to ensure they have
 * a value.
 */
public class PostProcessor implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        return new TypeAdapter<T>() {
            @Override
            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                T t = delegate.read(in);
                if (t instanceof PostProcessed) {
                    // Call PostProcessed#postProcess
                    ((PostProcessed) t).postProcess();

                    // Inspect required fields
                    inspectRequired(t);
                }
                return null;
            }
        };
    }

    private void inspectRequired(Object object) {
        try {
            //todo inspect super classes
            for (Field field : object.getClass().getDeclaredFields()) {
                if (field.getAnnotation(Required.class) == null)
                    continue;

                field.setAccessible(true);
                if (field.get(object) == null)
                    throw new JsonParseException("Null value in field " + field.getName() + " of " + object.getClass());
            }
        } catch (IllegalAccessException e) {
            throw new JsonParseException(e);
        }
    }
}
