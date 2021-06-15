package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.*;
import com.casper.sdk.service.serialization.util.ByteUtils;

/**
 * Converts a CLValue to a byte array
 */
class CLValueByteSerializer implements ByteSerializer<CLValue> {

    private static byte[] toBytes(final CLTypeInfo clTypeInfo) {
        return CLTypeHelper.toBytesHelper(clTypeInfo);
    }

    @Override
    public byte[] toBytes(final CLValue source) {

        if (source.getCLType() == CLType.OPTION) {
            return toOptionBytes(source);
        } else {
            return toValueBytes(source);
        }
    }

    @Override
    public Class<CLValue> getType() {
        return CLValue.class;
    }

    private byte[] toValueBytes(CLValue source) {
        return ByteUtils.concat(
                ByteUtils.toU32(source.getBytes().length),
                source.getBytes(),
                toBytes(source.getCLTypeInfo())
        );
    }

    private byte[] toOptionBytes(final CLValue source) {
        return ByteUtils.concat(
                ByteUtils.toU32(source.getBytes().length + 1),
                getOptionByte(source.getBytes()),
                source.getBytes(),
                toBytes(source.getCLTypeInfo())
        );
    }

    private byte[] getOptionByte(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return CLOptionTypeInfo.OPTION_NONE;
        } else {
            return CLOptionTypeInfo.OPTION_SOME;
        }
    }


}
