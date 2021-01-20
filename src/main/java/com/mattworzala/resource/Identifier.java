package com.mattworzala.resource;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class Identifier {
    @NotNull
    public static Identifier of(@Nullable String namespace, @NotNull String id) {
        return new Identifier(namespace, id);
    }

    @NotNull
    public static Identifier of(@NotNull String namespacedId) {
        if (namespacedId.contains(":")) {
            String[] parts = namespacedId.split(":");
            return new Identifier(parts[0], parts[1]);
        }
        return new Identifier(null, namespacedId);
    }

    private final String namespace;
    private final String id;

    private Identifier(@Nullable String namespace, @NotNull String id) {
        this.namespace = namespace == null ? "minecraft" : namespace;
        this.id = id;
    }

    @NotNull
    public String getNamespace() {
        return this.namespace;
    }

    @NotNull
    public String getId() {
        return this.id;
    }

    @NotNull
    public String toString() {
        return this.namespace + ":" + this.id;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Identifier)) {
            return false;
        } else {
            Identifier that = (Identifier)o;
            return this.namespace.equals(that.namespace) && this.id.equals(that.id);
        }
    }

    public int hashCode() {
        return Objects.hash(this.namespace, this.id);
    }
}
