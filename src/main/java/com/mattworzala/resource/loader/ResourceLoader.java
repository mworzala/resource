package com.mattworzala.resource.loader;

import com.mattworzala.resource.registry.Registry;
import com.mattworzala.resource.Resource;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * A base resource loader. A system used to load resources into a {@link Registry}.
 *
 * @param <S> The source to load from, a {@link Path} for example.
 * @param <R> The type of the {@link Resource} being loaded.
 */
public interface ResourceLoader<S, R extends Resource> {
//    static <R extends Resource> FileResourceLoader<R> file() {
//        return
//    }

    /**
     * Loads the given source, and returns the number of resources successfully loaded.
     *
     * @param source A source containing zero or more resource load candidates.
     * @return The number of resources successfully loaded.
     */
    int load(S source);

    /**
     * Unloads all resources in this loader.
     *
     * @return The number of resources unloaded.
     */
    int unload();

    /**
     * Unloads and reloads all resources in this loader.
     * <p>
     * The number of resources <b>loaded</b>.
     *
     * @return The number of resources reloaded.
     */
    int reload();

    /**
     * Loads a single resource into this loader directly.
     * <p>
     * Resources loaded this way will <b>not</b> be reloaded on {@link #reload()}.
     *
     * @param resource The resource to load.
     */
    void loadDirect(@NotNull R resource);
}
