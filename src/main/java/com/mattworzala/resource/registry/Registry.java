package com.mattworzala.resource.registry;

import com.mattworzala.resource.Identifier;
import com.mattworzala.resource.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public interface Registry<R extends Resource> {
    @Nullable
    static <R extends Resource> R get(@NotNull Registry<R> registry, @NotNull Identifier id) {
        return registry.get(id);
    }

    @Nullable
    static <R extends Resource> R get(@NotNull Registry<R> registry, @NotNull String namespacedId) {
        return registry.get(namespacedId);
    }

    static <R extends Resource> void register(@NotNull Registry<R> registry, @NotNull Identifier id, @NotNull R resource) {
        if (!(registry instanceof MutableRegistry))
            throw new IllegalArgumentException("Cannot register " + id + " to immutable registry!");
        ((MutableRegistry<R>) registry).register(id, resource);
    }

    /* ------ */

    @Nullable
    default R get(@NotNull String namespacedId) { return get(Identifier.of(namespacedId)); }

    @Nullable
    R get(@NotNull Identifier id);

    @NotNull
    Stream<R> stream();
}
