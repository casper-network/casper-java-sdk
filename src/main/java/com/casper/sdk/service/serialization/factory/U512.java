package com.casper.sdk.service.serialization.factory;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;

public class U512 implements TypesInterface {
    @Override public String serialize(final String toSerialize) {
        //Convert string to a bigint
        final BigInteger bigInt = new BigInteger(toSerialize);
        //Get the byte array
        byte[] bytes = bigInt.toByteArray();
        //Now reverse it
        ArrayUtils.reverse(bytes);

        //Build and return the hex of the length byte
        //plus the reversed byte array of the number
        return new StringBuilder()
                .append(Hex.encodeHexString(new byte[]{(byte) bytes.length}))
                .append(Hex.encodeHexString(bytes))
                .toString();
    }
}
