package com.casper.sdk.service.serialization.types;

/**
 * Interface implemented by all classed that convert the casper type objects to byte arrays.
 *
 * @param <T>
 */
public interface ByteSerializer<T> {

    /**
     * Converts an object to a byte array.
     *
     * @param source the source object to convert to bytes
     * @return the object converted to bytes
     */
    byte[] toBytes(final T source);


    /**
     * Obtains the type of the source to be serialized.
     *
     * @return the type of the object being serialized to bytes
     */

    Class<T> getType();
}
