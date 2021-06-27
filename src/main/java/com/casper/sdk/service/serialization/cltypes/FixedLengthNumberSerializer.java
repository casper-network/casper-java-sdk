package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.exceptions.ConversionException;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.util.Arrays;

import static com.casper.sdk.service.serialization.util.NumberUtils.toBigInteger;

abstract class FixedLengthNumberSerializer implements TypesSerializer {

    /** The maximum number of bytes allowed for the number */
    private final int maxBytes;
    /** Indicated if the number is signed (allows negative values) */
    private final boolean signed;

    public FixedLengthNumberSerializer(final int maxBytes, final boolean signed) {
        this.maxBytes = maxBytes;
        this.signed = signed;
    }

    @Override
    public byte[] serialize(final Object toSerialize) {

        final BigInteger bigInt = toBigInteger(toSerialize);
        byte[] bytes = bigInt.toByteArray();

        // Will pad with leading 0x00 for positive and -0xFF for negative numbers if length less than maxBytes
        if (bytes.length <= maxBytes) {
            bytes = zeroPad(bytes, (signed && bigInt.signum() == -1));
        } else if (bytes.length == maxBytes + 1 && bytes[0] == 0) {
            // If leading zero trim if maxBytes + 1 bytes long
            bytes = trimLeadingZero(bytes);
        } else {
            throw new ConversionException(bigInt + " exceeds " + maxBytes + " bytes length");
        }

        // Switch from BE to LE byte order
        ArrayUtils.reverse(bytes);

        return bytes;
    }

    private byte[] trimLeadingZero(byte[] bytes) {
        byte[] copy = new byte[maxBytes];
        System.arraycopy(bytes, bytes.length - maxBytes, copy, 0, maxBytes);
        bytes = copy;
        return bytes;
    }

    private byte[] zeroPad(byte[] bytes, final boolean negative) {
        if (bytes.length < maxBytes) {
            // Zero pad
            byte[] copy = new byte[maxBytes];
            Arrays.fill(copy, 0, maxBytes - bytes.length, (byte) (negative ? 0xFF : 0));
            System.arraycopy(bytes, 0, copy, maxBytes - bytes.length, bytes.length);
            bytes = copy;
        }
        return bytes;
    }
}
