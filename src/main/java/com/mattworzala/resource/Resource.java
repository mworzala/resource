package com.mattworzala.resource;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an identifiable resource.
 */
public interface Resource {

    /**
     * Gets the id of this particular resource.
     *
     * @return this resources id.
     */
    @NotNull
    Identifier getId();
}
