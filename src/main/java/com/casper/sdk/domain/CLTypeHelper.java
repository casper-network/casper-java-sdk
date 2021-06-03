package com.casper.sdk.domain;

import com.casper.sdk.service.serialization.ByteArrayBuilder;
import com.casper.sdk.service.serialization.factory.TypesFactory;

/**
 * Helper class for converting CLTypes to bytes
 */
public class CLTypeHelper {

    public static final TypesFactory typesFactory = new TypesFactory();

    public static byte[] toBytesHelper(final CLTypeInfo typeInfo) {
        return switch (typeInfo.getType()) {
            case BOOL, I32, I64, U8, U32, U64, U128, U256, U512, UNIT, STRING, KEY, UREF, PUBLIC_KEY -> getTypeByte(typeInfo);
            case BYTE_ARRAY -> getArrayTypeBytes(typeInfo);
            default -> throw new IllegalArgumentException("Wrong type");
        };
    }

    private static byte[] getArrayTypeBytes(CLTypeInfo typeInfo) {
        final ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(getTypeByte(typeInfo));
        builder.append(typesFactory.getInstance(CLType.U32).serialize(((CLByteArrayInfo) typeInfo).getSize()));
        return builder.toByteArray();
    }

    private static byte[] getTypeByte(CLTypeInfo typeInfo) {
        return new byte[]{(byte) typeInfo.getType().getClType()};
    }

}


