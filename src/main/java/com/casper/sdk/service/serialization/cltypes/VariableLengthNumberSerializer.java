package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.exceptions.ConversionException;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;

import static com.casper.sdk.service.serialization.util.ByteUtils.concat;
import static com.casper.sdk.service.serialization.util.NumberUtils.toBigInteger;

/**
 * For u128, u256, u512, we have to and append extra byte for length
 */
abstract class VariableLengthNumberSerializer implements TypesSerializer {

    /** The maximum number of bytes allowed for the number */
    private final int maxBytes;

    VariableLengthNumberSerializer(int maxBytes) {
        this.maxBytes = maxBytes;
    }

    @Override
    public byte[] serialize(Object toSerialize) {
        //Convert string to a bigint
        final BigInteger bigInt = toBigInteger(toSerialize);
        //Get the byte array
        byte[] bytes = bigInt.toByteArray();

        if (bytes.length > maxBytes) {
            throw new ConversionException(bigInt + " exceeds " + maxBytes + " bytes length");
        }

        // Remove any leading zeros
        if (bytes.length > 1 && bytes[0] == 0) {
            bytes = removeLeadingZeros(bytes);
        }

        // Switch from BE to LE byte order
        ArrayUtils.reverse(bytes);

        //Build and return the hex of the length byte
        //plus the reversed byte array of the number
        return concat(new byte[]{(byte) bytes.length}, bytes);
    }

    private byte[] removeLeadingZeros(final byte[] bytes) {
        byte[] copy = new byte[bytes.length - 1];
        System.arraycopy(bytes, 1, copy, 0, bytes.length - 1);
        return copy;
    }
}
