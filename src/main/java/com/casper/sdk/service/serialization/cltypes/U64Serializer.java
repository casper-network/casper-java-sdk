package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.exceptions.ConversionException;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.util.Arrays;

import static com.casper.sdk.service.serialization.util.NumberUtils.toBigInteger;

class U64Serializer implements TypesSerializer {

    @Override
    public byte[] serialize(final Object toSerialize) {

        final BigInteger bigInt = toBigInteger(toSerialize);
        byte[] bytes = bigInt.toByteArray();

        // Will pad with leading 0x00 for positive and -0xFF for negative numbers if length less than 8
        if (bytes.length <= 8) {
            bytes = zeroPad(bytes, bigInt.signum() == -1);
        } else if (bytes.length == 9 && bytes[0] == 0) {
            // If leading zero trim if 9 bytes long
            bytes = trimLeadingZero(bytes);
        } else {
            throw new ConversionException(bigInt + " exceeds " + 8 + " bytes length");
        }

        ArrayUtils.reverse(bytes);

        return bytes;
    }

    private byte[] trimLeadingZero(byte[] bytes) {
        byte[] copy = new byte[8];
        System.arraycopy(bytes, bytes.length - 8, copy, 0, 8);
        bytes = copy;
        return bytes;
    }

    private byte[] zeroPad(byte[] bytes,  final boolean negative) {
        if (bytes.length < 8) {
            // Zero pad
            byte[] copy = new byte[8];
            Arrays.fill(copy, 0, 8 - bytes.length,(byte) (negative ?  0xFF: 0));
            System.arraycopy(bytes, 0, copy, 8 - bytes.length, bytes.length);
            bytes = copy;
        }
        return bytes;
    }
}
