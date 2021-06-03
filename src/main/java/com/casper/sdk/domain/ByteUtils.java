package com.casper.sdk.domain;

import com.casper.sdk.exceptions.ConversionException;
import com.casper.sdk.service.serialization.factory.TypesFactory;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class ByteUtils {

    public static final TypesFactory typesFactory = new TypesFactory();

    static byte[] toU32(Number number) {
        return decodeHex(typesFactory.getInstance(CLType.U32).serialize(number.intValue()));
    }

    static byte[] toU64(final Number number) {
        return decodeHex(typesFactory.getInstance(CLType.U64).serialize(number));
    }

    static byte[] toU512(final Number number) {
        return decodeHex(typesFactory.getInstance(CLType.U512).serialize(number));
    }

    /**
     * Joins multiple arrays into a new concatenated array
     *
     * @param arrays the array of arrays
     * @return new concatenated array
     */
    static byte[] concat(byte[]... arrays) {

        int len = 0;
        for (byte[] array : arrays) {
            len += array.length;
        }

        final byte[] c = new byte[len];
        int pos = 0;
        for (byte[] array : arrays) {
            System.arraycopy(array, 0, c, pos, array.length);
            pos += array.length;
        }
        return c;
    }

    static byte[] decodeHex(final String hex) {
        try {
            return Hex.decodeHex(hex.toCharArray());
        } catch (DecoderException e) {
            throw new ConversionException("Unable to decode " + hex, e);
        }
    }

    static byte[] toByteString(final String source) {
        return decodeHex(typesFactory.getInstance(CLType.STRING).serialize(source));
    }
}
