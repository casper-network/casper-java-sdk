package com.casper.sdk.domain;

import com.casper.sdk.service.serialization.util.ByteArrayBuilder;

import static com.casper.sdk.service.serialization.util.ByteUtils.toU32;

/**
 * Helper class for converting CLTypes to bytes
 */
public class CLTypeHelper {

    public static byte[] toBytesHelper(final CLTypeInfo typeInfo) {
        return switch (typeInfo.getType()) {
            case BOOL, I32, I64, U8, U32, U64, U128, U256, U512, UNIT, STRING, KEY, UREF, PUBLIC_KEY -> getTypeBytes(typeInfo);
            case BYTE_ARRAY -> getByteArrayType((CLByteArrayInfo) typeInfo);
            case OPTION -> getOptionType(typeInfo);
            default -> throw new IllegalArgumentException("Wrong type " + typeInfo.getType());
        };
    }

    private static byte[] getByteArrayType(CLByteArrayInfo typeInfo) {
        return new ByteArrayBuilder()
                .append(getTypeBytes(typeInfo))
                .append(toU32(typeInfo.getSize()))
                .toByteArray();
    }

    private static byte[] getOptionType(final CLTypeInfo typeInfo) {
        return new ByteArrayBuilder()
                .append(getTypeBytes(typeInfo))
                .append(toBytesHelper(((CLOptionTypeInfo) typeInfo).getInnerType()))
                .toByteArray();
    }

    private static byte[] getTypeBytes(final CLTypeInfo typeInfo) {
        return new byte[]{typeInfo.getType().getClType()};
    }
}


