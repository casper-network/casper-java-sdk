package com.casper.sdk.service.serialization.factory;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class U32 implements TypesInterface {
    @Override public String serialize(final Object toSerialize) {
        final BigInteger bigInt = new BigInteger(String.valueOf(toSerialize));
        int intValue = bigInt.intValue();

        ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(intValue);
        byte[] bytes = buffer.array();

        ArrayUtils.reverse(bytes);

        return Hex.encodeHexString(bytes);
    }

    @Override public String serialize(final String toSerialize, final TypesFactory typesFactory) {
        return null;
    }
}
