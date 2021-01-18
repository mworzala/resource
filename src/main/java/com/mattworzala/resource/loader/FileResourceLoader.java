package com.mattworzala.resource.loader;

import com.google.gson.Gson;
import com.mattworzala.resource.Resource;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Loads resources from files or directories on disk.
 *
 * @param <R> The resource type to load
 */
public abstract class FileResourceLoader<R extends Resource> implements ResourceLoader<Path, R> {
    private static final Logger logger = LoggerFactory.getLogger(FileResourceLoader.class);

    private final List<Path> loaded = new ArrayList<>();

    private final Gson gson;
    private final Class<? extends R> type;
    private boolean recursive = false;
    private boolean failOnIssue = false;

    public FileResourceLoader(Gson gson, Class<? extends R> type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public int load(Path fileOrDirectory) {
        if (Files.isDirectory(fileOrDirectory))
            return loadDirectory(fileOrDirectory);
        return loadFile(fileOrDirectory);
    }

    @Override
    public int reload() {
        List<Path> loaded = new ArrayList<>(this.loaded);
        this.loaded.clear();
        unload();
        return loaded.stream().mapToInt(this::loadFile).sum();
    }

    @Contract("_ -> this")
    public FileResourceLoader<R> setRecursive(boolean recursive) {
        this.recursive = recursive;
        return this;
    }

    @Contract("_ -> this")
    public FileResourceLoader<R> setFailOnIssue(boolean fail) {
        this.failOnIssue = fail;
        return this;
    }

    protected int loadDirectory(Path directory) {
        return (this.recursive ?
                loadDirectoryRecursive(directory) :
                loadDirectoryShallow(directory))
                .mapToInt(this::loadFile)
                .sum();
    }

    protected Stream<Path> loadDirectoryRecursive(Path directory) {
        try {
            return Files.list(directory).flatMap(child -> {
                if (Files.isDirectory(child))
                    return loadDirectoryRecursive(child);
                return Stream.of(child);
            });
        } catch (IOException e) {
            if (failOnIssue) throw new RuntimeException(e);
            logger.warn("Failed to load directory {}!", directory);
            return Stream.of();
        }
    }

    protected Stream<Path> loadDirectoryShallow(Path directory) {
        try {
            return Files.list(directory).filter(Files::isRegularFile);
        } catch (IOException e) {
            if (failOnIssue) throw new RuntimeException(e);
            logger.warn("Failed to load directory {}!", directory);
            return Stream.of();
        }
    }

    protected int loadFile(Path file) {
        final String name = file.getFileName().toString();
        loaded.add(file);
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            R resource = this.gson.fromJson(reader, this.type);
            this.loadDirect(resource);
            return 1;
        } catch (Exception e) {
            if (failOnIssue) throw new RuntimeException(e);
            logger.warn("Failed to load {}: ", name, e);
            return 0;
        }
    }

}
