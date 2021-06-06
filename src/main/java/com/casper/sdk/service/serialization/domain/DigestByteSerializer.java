package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.Digest;
import com.casper.sdk.service.serialization.util.ByteUtils;

/**
 * The byte serializer for a {@link Digest} domain object.
 */
class DigestByteSerializer implements ByteSerializer<Digest> {

    @Override
    public byte[] toBytes(final Digest source) {
        return ByteUtils.decodeHex(source.getHash());
    }

    @Override
    public Class<Digest> getType() {
        return Digest.class;
    }
}
