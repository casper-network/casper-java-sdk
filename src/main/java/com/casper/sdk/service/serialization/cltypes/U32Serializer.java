package com.casper.sdk.service.serialization.cltypes;

import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import static com.casper.sdk.service.serialization.util.NumberUtils.toBigInteger;

/**
 * Converts a value to a little endian 32 bit array
 */
class U32Serializer implements TypesSerializer {

    @Override
    public byte[] serialize(final Object toSerialize) {

        final BigInteger bigInt = toBigInteger(toSerialize);
        int intValue = bigInt.intValue();

        final ByteBuffer buffer = ByteBuffer.allocate(Integer.BYTES);
        buffer.putInt(intValue);
        byte[] bytes = buffer.array();

        ArrayUtils.reverse(bytes);

        return bytes;
    }
}
