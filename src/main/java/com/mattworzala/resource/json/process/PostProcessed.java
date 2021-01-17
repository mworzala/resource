package com.mattworzala.resource.json.process;

/**
 *
 */
public interface PostProcessed {

    /**
     * Called after the object has been deserialized, to allow more complex
     * deserialization behavior or checks.
     */
    void postProcess();
}
