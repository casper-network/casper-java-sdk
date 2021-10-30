package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.CLPublicKey;
import com.casper.sdk.types.Digest;

import java.security.PublicKey;

/**
 * Converts a Java Byte array to a casper byte array
 */
class ByteArraySerializer extends AbstractTypesSerializer {

    public ByteArraySerializer(final TypesFactory typesFactory) {
        super(typesFactory);
    }

    @Override
    public byte[] serialize(final Object toSerialize) {

        byte[] originalBytes = toBytes(toSerialize);

        return new ByteArrayBuilder()
                .append(getU32Serializer().serialize(originalBytes.length))
                .append(originalBytes)
                .toByteArray();
    }

    private byte[] toBytes(final Object toSerialize) {
        if (toSerialize instanceof String) {
            return ByteUtils.decodeHex((String) toSerialize);
        } else if (toSerialize instanceof byte[]) {
            return (byte[]) toSerialize;
        } else if (toSerialize instanceof Digest) {
            return ((Digest) toSerialize).getHash();
        } else if (toSerialize instanceof PublicKey || toSerialize instanceof CLPublicKey) {
            return getPublicKeySerializer().serialize(toSerialize);
        } else {
            return new byte[0];
        }
    }
}
