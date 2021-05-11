package com.casper.sdk.service.serialization.factory;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

public class U64 implements TypesInterface {
    @Override public String serialize(final Object toSerialize) {

        if (String.valueOf(toSerialize).length() < 1){
            return "00";
        }

        //Convert string to a bigint
        final BigInteger bigInt = new BigInteger(String.valueOf(toSerialize));

        if (bigInt.longValueExact() == 0L){
            return "00";
        }

        Long longValue = bigInt.longValue();

        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(longValue);
        byte[] bytes = buffer.array();

        ArrayUtils.reverse(bytes);

        //append optional 01/00 to returned vale
        //01 has value
        //00 no value

        return "01" + Hex.encodeHexString(bytes);
    }

    @Override public String serialize(final String toSerialize, final TypesFactory typesFactory) {
        return null;
    }
}
