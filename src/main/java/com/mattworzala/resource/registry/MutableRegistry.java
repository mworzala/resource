package com.mattworzala.resource.registry;

import com.mattworzala.resource.Identifier;
import com.mattworzala.resource.Resource;
import org.jetbrains.annotations.NotNull;

public interface MutableRegistry<R extends Resource> extends Registry<R> {
    static <R extends Resource> MutableRegistry<R> newThreadSafeRegistry() {
        return new ThreadSafeMapRegistry<>();
    }


    void register(@NotNull Identifier id, @NotNull R resource);

    @Deprecated
    void unregister(@NotNull Identifier id);

    @Deprecated
    void clear();
}
