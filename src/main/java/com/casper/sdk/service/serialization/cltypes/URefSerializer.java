package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.types.URef;

/**
 * Serializes a {@link URef} to a byte array
 */
class URefSerializer implements TypesSerializer {

    @Override
    public byte[] serialize(final Object toSerialize) {

        return new ByteArrayBuilder()
                .append((byte) ((URef) toSerialize).getTag())
                .append(((URef) toSerialize).getBytes())
                .append(((URef) toSerialize).getAccessRights().getBits())
                .toByteArray();
    }
}
