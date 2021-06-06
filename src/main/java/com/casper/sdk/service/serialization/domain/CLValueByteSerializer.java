package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.CLTypeHelper;
import com.casper.sdk.domain.CLTypeInfo;
import com.casper.sdk.domain.CLValue;
import com.casper.sdk.service.serialization.util.ByteUtils;

/**
 * Converts a CLValue to a byte array
 */
class CLValueByteSerializer implements ByteSerializer<CLValue> {

    @Override
    public byte[] toBytes(final CLValue source) {
        return ByteUtils.concat(toBytes(source.getCLTypeInfo()), source.getBytes());
    }

    @Override
    public Class<CLValue> getType() {
        return CLValue.class;
    }

    private static byte[] toBytes(final CLTypeInfo clTypeInfo) {
        return CLTypeHelper.toBytesHelper(clTypeInfo);
    }
}
