package com.casper.sdk.domain;

import com.casper.sdk.service.serialization.util.ByteArrayBuilder;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;

/**
 * Helper class for converting CLTypes to bytes
 */
public class CLTypeHelper {

    public static final TypesFactory typesFactory = new TypesFactory();

    public static byte[] toBytesHelper(final CLTypeInfo typeInfo) {
        return switch (typeInfo.getType()) {
            case BOOL, I32, I64, U8, U32, U64, U128, U256, U512, UNIT, STRING, KEY, UREF, PUBLIC_KEY -> getTypeByte(typeInfo);
            case BYTE_ARRAY -> getArrayTypeBytes(typeInfo);
            case OPTION -> getOptionType(typeInfo);
            default -> throw new IllegalArgumentException("Wrong type " + typeInfo.getType());
        };
    }

    private static byte[] getOptionType(final CLTypeInfo typeInfo) {
        final ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(getTypeByte(typeInfo));
        builder.append(toBytesHelper(((CLOptionTypeInfo)typeInfo).getInnerType()));
        return builder.toByteArray();
    }

    private static byte[] getArrayTypeBytes(final CLTypeInfo typeInfo) {
        final ByteArrayBuilder builder = new ByteArrayBuilder();
        builder.append(getTypeByte(typeInfo));
        // Write the size of the array as U32 LE bytes
        builder.append(typesFactory.getInstance(CLType.U32).serialize(((CLByteArrayInfo) typeInfo).getSize()));
        return builder.toByteArray();
    }

    private static byte[] getTypeByte(final CLTypeInfo typeInfo) {
        return new byte[]{(byte) typeInfo.getType().getClType()};
    }

}


