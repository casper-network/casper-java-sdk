package com.casper.sdk.service.serialization.factory;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;

import static com.casper.sdk.service.serialization.ByteUtils.concat;

class U512 implements TypesSerializer {

    @Override
    public byte[] serialize(final Object toSerialize) {

        //Convert string to a bigint
        final BigInteger bigInt = toBigInteger(toSerialize);
        //Get the byte array
        byte[] bytes = bigInt.toByteArray();

        //Now reverse it
        ArrayUtils.reverse(bytes);

        //Build and return the hex of the length byte
        //plus the reversed byte array of the number
        return concat(new byte[]{(byte) bytes.length}, bytes);
    }
}
