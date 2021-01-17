package com.mattworzala.resource.loader;

import com.google.gson.Gson;
import com.mattworzala.resource.Resource;
import com.mattworzala.resource.registry.MutableRegistry;
import org.jetbrains.annotations.NotNull;

public class RegistryFileResourceLoader<R extends Resource> extends FileResourceLoader<R> {
    private final MutableRegistry<R> registry;

    public RegistryFileResourceLoader(Gson gson, MutableRegistry<R> registry, Class<R> type) {
        super(gson, type);
        this.registry = registry;
    }

    @Override
    public void loadDirect(@NotNull R resource) {
        registry.register(resource.getId(), resource);
    }

    @Override
    public int unload() {
        int total = (int) registry.stream().count();
        registry.clear();
        return total;
    }

    @NotNull
    public MutableRegistry<R> getRegistry() {
        return registry;
    }
}
