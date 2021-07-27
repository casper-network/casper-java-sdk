package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.types.URef;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;

/**
 * Serializes a {@link URef} to a byte array
 */
class URefSerializer implements TypesSerializer {

    private static final byte TAG = 2;

    @Override
    public byte[] serialize(final Object toSerialize) {

        return new ByteArrayBuilder()
                .append(TAG)
                .append(((URef) toSerialize).getBytes())
                .append(((URef) toSerialize).getAccessRights().getBits())
                .toByteArray();
    }
}
