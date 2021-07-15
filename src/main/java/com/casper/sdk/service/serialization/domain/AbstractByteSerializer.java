package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.CLByteArrayInfo;
import com.casper.sdk.domain.CLOptionTypeInfo;
import com.casper.sdk.domain.CLType;
import com.casper.sdk.domain.CLTypeInfo;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.cltypes.TypesSerializer;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;

abstract class AbstractByteSerializer<T> implements ByteSerializer<T> {

    private final TypesSerializer u32Serializer;

    AbstractByteSerializer(final TypesFactory typesFactory) {
        u32Serializer = typesFactory.getInstance(CLType.U32);
    }

    byte[] toBytesForCLTypeInfo(final CLTypeInfo typeInfo) {
        return switch (typeInfo.getType()) {
            case BOOL, I32, I64, U8, U32, U64, U128, U256, U512, UNIT, STRING, KEY, UREF, PUBLIC_KEY -> getTypeBytes(typeInfo);
            case BYTE_ARRAY -> getByteArrayType((CLByteArrayInfo) typeInfo);
            case OPTION -> getOptionType(typeInfo);
            default -> throw new IllegalArgumentException("Wrong type " + typeInfo.getType());
        };
    }

    public TypesSerializer getU32Serializer() {
        return u32Serializer;
    }

    private  byte[] getByteArrayType(CLByteArrayInfo typeInfo) {
        return new ByteArrayBuilder()
                .append(getTypeBytes(typeInfo))
                .append(u32Serializer.serialize(typeInfo.getSize()))
                .toByteArray();
    }

    private  byte[] getOptionType(final CLTypeInfo typeInfo) {
        return new ByteArrayBuilder()
                .append(getTypeBytes(typeInfo))
                .append(toBytesForCLTypeInfo(((CLOptionTypeInfo) typeInfo).getInnerType()))
                .toByteArray();
    }

    private  byte[] getTypeBytes(final CLTypeInfo typeInfo) {
        return new byte[]{typeInfo.getType().getClType()};
    }
}
