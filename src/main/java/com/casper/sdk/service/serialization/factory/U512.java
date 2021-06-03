package com.casper.sdk.service.serialization.factory;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;

class U512 implements TypesSerializer {

    @Override
    public String serialize(final Object toSerialize) {
        //Convert string to a bigint
        final BigInteger bigInt = toBigInteger(toSerialize);
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
