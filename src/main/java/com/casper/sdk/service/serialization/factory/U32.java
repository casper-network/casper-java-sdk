package com.casper.sdk.service.serialization.factory;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

class U32 implements TypesSerializer {

    @Override
    public String serialize(final Object toSerialize) {

        final BigInteger bigInt = toBigInteger(toSerialize);
        int intValue = bigInt.intValue();

        final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(intValue);
        byte[] bytes = buffer.array();

        ArrayUtils.reverse(bytes);

        return Hex.encodeHexString(bytes);
    }
}
