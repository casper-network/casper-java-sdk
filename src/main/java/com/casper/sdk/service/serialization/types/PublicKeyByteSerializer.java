package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.CLPublicKey;

import static com.casper.sdk.service.serialization.util.ByteUtils.concat;

public class PublicKeyByteSerializer implements ByteSerializer<CLPublicKey> {
    @Override
    public byte[] toBytes(final CLPublicKey source) {
        return concat(new byte[]{(byte) source.getAlgorithm().getValue()}, source.getBytes());
    }

    @Override
    public Class<CLPublicKey> getType() {
        return CLPublicKey.class;
    }
}
