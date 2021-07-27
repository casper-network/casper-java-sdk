package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.Digest;

/**
 * The byte serializer for a {@link Digest} type object.
 */
class DigestByteSerializer implements ByteSerializer<Digest> {

    @Override
    public byte[] toBytes(final Digest source) {
        return source.getHash();
    }

    @Override
    public Class<Digest> getType() {
        return Digest.class;
    }
}
