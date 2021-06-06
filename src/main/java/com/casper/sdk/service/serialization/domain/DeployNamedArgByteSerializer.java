package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.DeployNamedArg;
import com.casper.sdk.service.serialization.util.ByteUtils;

import static com.casper.sdk.service.serialization.util.ByteUtils.toU32;

public class DeployNamedArgByteSerializer implements ByteSerializer<DeployNamedArg> {

    private final CLValueByteSerializer valueSerializer = new CLValueByteSerializer();

    @Override
    public byte[] toBytes(DeployNamedArg source) {
        byte[] name = source.getName().getBytes();

        return ByteUtils.concat(
                ByteUtils.concat(toU32(name.length), name),
                valueSerializer.toBytes(source.getValue())
        );
    }

    @Override
    public Class<DeployNamedArg> getType() {
        return DeployNamedArg.class;
    }
}
