package com.casper.sdk.service.serialization.factory;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class U64 implements TypesInterface {
    @Override public String serialize(final String toSerialize) {
        //Convert string to a bigint
        final BigInteger bigInt = new BigInteger(toSerialize);
        Long longValue = bigInt.longValue();

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(longValue);
        byte[] bytes = buffer.array();

        ArrayUtils.reverse(bytes);

        return Hex.encodeHexString(bytes);
    }
}
