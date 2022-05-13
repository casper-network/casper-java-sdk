package com.casper.sdk.service.serialization.util;


/**
 * Provide utilities for Java's issues with generic casting etc.
 */
public class GenericsUtils {

    /**
     * Utility method to suppress Java's stupid generics warnings
     *
     * @param obj the object to cast to a generic type
     * @param <T> the type to cast to
     * @return the cast object
     */
    @SuppressWarnings("unchecked")
    public static <T> T genericCast(final Object obj) {
        return (T) obj;
    }
}
