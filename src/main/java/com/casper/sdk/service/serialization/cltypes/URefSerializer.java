package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.domain.URef;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;

/**
 * Serializes a {@link URef} to a byte array
 */
class URefSerializer implements TypesSerializer {

    @Override
    public byte[] serialize(final Object toSerialize) {

        return new ByteArrayBuilder()
                .append(((URef) toSerialize).getBytes())
                .append(((URef) toSerialize).getAccessRights().getBits())
                .toByteArray();
    }
}
