package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.CLType;
import com.casper.sdk.types.DeployNamedArg;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.cltypes.TypesSerializer;
import com.casper.sdk.service.serialization.util.ByteUtils;

public class DeployNamedArgByteSerializer implements ByteSerializer<DeployNamedArg> {

    private final CLValueByteSerializer valueSerializer;
    private final TypesSerializer u32Serializer;

    public DeployNamedArgByteSerializer(final TypesFactory typesFactory) {
        u32Serializer = typesFactory.getInstance(CLType.U32);
        valueSerializer = new CLValueByteSerializer(typesFactory);
    }

    @Override
    public byte[] toBytes(DeployNamedArg source) {
        byte[] name = source.getName().getBytes();

        return ByteUtils.concat(
                ByteUtils.concat(u32Serializer.serialize(name.length), name),
                valueSerializer.toBytes(source.getValue())
        );
    }

    @Override
    public Class<DeployNamedArg> getType() {
        return DeployNamedArg.class;
    }
}
