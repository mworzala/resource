package com.mattworzala.resource.json.process;

import com.google.gson.JsonParseException;

/**
 *
 */
public interface PostProcessed {

    /**
     * Called after the object has been deserialized, to allow more complex
     * deserialization behavior or checks.
     */
    void postProcess() throws JsonParseException;
}
