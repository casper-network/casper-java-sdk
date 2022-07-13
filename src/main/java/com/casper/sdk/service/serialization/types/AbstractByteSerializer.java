package com.casper.sdk.service.serialization.types;

import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.cltypes.TypesSerializer;
import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.types.*;

abstract class AbstractByteSerializer<T> implements ByteSerializer<T> {

    private final TypesSerializer u32Serializer;

    AbstractByteSerializer(final TypesFactory typesFactory) {
        u32Serializer = typesFactory.getInstance(CLType.U32);
    }

    byte[] toBytesForCLTypeInfo(final CLTypeInfo typeInfo) {
        switch (typeInfo.getType()) {
            case BOOL:
            case I32:
            case I64:
            case U8:
            case U32:
            case U64:
            case U128:
            case U256:
            case U512:
            case UNIT:
            case STRING:
            case KEY:
            case UREF:
            case PUBLIC_KEY:
                return getTypeBytes(typeInfo);

            case BYTE_ARRAY:
                return getByteArrayType((CLByteArrayInfo) typeInfo);

            case OPTION:
                return getOptionType(typeInfo);

            case MAP:
                return getMapType((CLMapTypeInfo) typeInfo);

            default:
                throw new IllegalArgumentException("Wrong type " + typeInfo.getType());
        }
    }

    private byte[] getMapType(final CLMapTypeInfo typeInfo) {
        return new ByteArrayBuilder()
                .append(getTypeBytes(typeInfo))
                .append(getTypeBytes(typeInfo.getKeyType()))
                .append(getTypeBytes(typeInfo.getValueType()))
                .toByteArray();
    }

    public TypesSerializer getU32Serializer() {
        return u32Serializer;
    }

    private byte[] getByteArrayType(CLByteArrayInfo typeInfo) {
        return new ByteArrayBuilder()
                .append(getTypeBytes(typeInfo))
                .append(u32Serializer.serialize(typeInfo.getSize()))
                .toByteArray();
    }

    private byte[] getOptionType(final CLTypeInfo typeInfo) {
        return new ByteArrayBuilder()
                .append(getTypeBytes(typeInfo))
                .append(toBytesForCLTypeInfo(((CLOptionTypeInfo) typeInfo).getInnerType()))
                .toByteArray();
    }

    private byte[] getTypeBytes(final CLTypeInfo typeInfo) {
        return new byte[]{typeInfo.getType().getClType()};
    }
}
