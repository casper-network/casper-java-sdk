package com.casper.sdk.service.serialization;

import com.casper.sdk.domain.CLType;
import com.casper.sdk.exceptions.ConversionException;
import com.casper.sdk.service.serialization.factory.TypesFactory;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class ByteUtils {

    public static final TypesFactory typesFactory = new TypesFactory();

    public static byte[] toU32(Number number) {
        return typesFactory.getInstance(CLType.U32).serialize(number.intValue());
    }

    public static byte[] toU64(final Number number) {
        return typesFactory.getInstance(CLType.U64).serialize(number);
    }

    public static byte[] toU512(final Number number) {
        return typesFactory.getInstance(CLType.U512).serialize(number);
    }

    /**
     * Joins multiple arrays into a new concatenated array
     *
     * @param arrays the array of arrays
     * @return new concatenated array
     */
    public static byte[] concat(byte[]... arrays) {

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

    public static byte[] decodeHex(final String hex) {
        try {
            return Hex.decodeHex(hex.toCharArray());
        } catch (DecoderException e) {
            throw new ConversionException("Unable to decode " + hex, e);
        }
    }

    public static byte[] toBytes(final String source) {
        return typesFactory.getInstance(CLType.STRING).serialize(source);
    }

    public static String encodeHexString(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }
}
