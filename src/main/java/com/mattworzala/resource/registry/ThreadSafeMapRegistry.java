package com.mattworzala.resource.registry;

import com.mattworzala.resource.Identifier;
import com.mattworzala.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class ThreadSafeMapRegistry<R extends Resource> implements MutableRegistry<R> {
    private final Map<Identifier, R> items = new ConcurrentHashMap<>();

    @Override
    public @Nullable R get(@NotNull Identifier id) {
        return items.get(id);
    }

    @Override
    public @NotNull Stream<R> stream() {
        return items.values().stream();
    }

    @Override
    public void register(@NotNull Identifier id, @NotNull R resource) {
        if (items.containsKey(id))
            throw new IllegalArgumentException("Cannot register the same resource '" + id + "' twice!");
        items.put(id, resource);
    }

    @Override
    public void unregister(@NotNull Identifier id) {
        items.remove(id);
    }

    @Override
    public void clear() {
        items.clear();
    }
}
