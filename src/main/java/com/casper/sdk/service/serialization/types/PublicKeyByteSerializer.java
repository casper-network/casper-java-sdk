package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.PublicKey;

import static com.casper.sdk.service.serialization.util.ByteUtils.concat;

public class PublicKeyByteSerializer implements ByteSerializer<PublicKey> {
    @Override
    public byte[] toBytes(final PublicKey source) {
        return concat(
                new byte[]{(byte) source.getTag()},
                source.getBytes()
        );
    }

    @Override
    public Class<PublicKey> getType() {
        return PublicKey.class;
    }
}
