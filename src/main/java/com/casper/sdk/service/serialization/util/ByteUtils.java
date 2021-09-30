package com.casper.sdk.service.serialization.util;

import com.casper.sdk.exceptions.ConversionException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class ByteUtils {

    /**
     * Joins multiple arrays into a new concatenated array
     *
     * @param arrays the array of arrays
     * @return new concatenated array
     */
    public static byte[] concat(byte[]... arrays) {

        int len = 0;
        for (byte[] array : arrays) {
            len += array != null ? array.length : 0;
        }

        final byte[] c = new byte[len];
        int pos = 0;
        for (byte[] array : arrays) {
            if (array != null) {
                System.arraycopy(array, 0, c, pos, array.length);
                pos += array.length;
            }
        }
        return c;
    }

    public static byte[] decodeHex(final String hex) {
        try {
            return Hex.decodeHex(hex.toCharArray());
        } catch (DecoderException e) {
            throw new ConversionException("Unable to decode: \"" + hex + "\" length " + hex.length(), e);
        }
    }

    public static String encodeHexString(byte[] bytes) {
        return Hex.encodeHexString(bytes);
    }

    /**
     * Obtains the last 'length' bytes from a byte array
     *
     * @param toTruncate the byte array to obtain the bytes from
     * @param length     the number of bytes to obtain
     * @return the last 'length' bytes from a byte array
     */
    public static byte[] lastNBytes(byte[] toTruncate, final int length) {
        byte[] lastNBytes = new byte[length];
        int start = toTruncate.length - length;
        System.arraycopy(toTruncate, start, lastNBytes, 0, length);
        return lastNBytes;
    }

    /**
     * Converts a number to a byte value in a byte array
     *
     * @param toByteInArray the number to convert
     * @return the byte array containing a single byte value
     */
    public static byte[] toByteArray(final Number toByteInArray) {
        return new byte[]{toByteInArray.byteValue()};
    }
}
