package com.casper.sdk.service.serialization.cltypes;


/**
 * Interface to be implemented by classes that are to be converted in to casper byte format
 */
public interface TypesSerializer {

    /**
     * Converts a object to casper byte format
     *
     * @param toSerialize the object to serialize to casper byte format
     * @return the object converted to casper bytes
     */
    byte[] serialize(final Object toSerialize);
}
