package dev.oak3.sbs4j.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities class for Byte related operations
 *
 * @since 0.1.0
 */
public final class ByteUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(ByteUtils.class);

    /**
     * Hex string code case selector
     */
    public enum HexCase {
        LOWER("0123456789abcdef".toCharArray()),
        UPPER("0123456789ABCDEF".toCharArray());

        private final char[] hexCodes;

        public char[] getHexCodes() {
            return this.hexCodes;
        }

        HexCase(char[] hexCodes) {
            this.hexCodes = hexCodes;
        }
    }

    /**
     * Reverses a byte array, used to change endian
     *
     * @param bytes array of bytes to reverse
     */
    public static void reverse(byte[] bytes) {
        reverse(bytes, 0, bytes.length - 1);
    }

    /**
     * Reverses a byte array, used to change endian
     *
     * @param bytes array of bytes to reverse
     * @param from  first byte position to swap
     * @param to    last byte position to swap
     */
    public static void reverse(byte[] bytes, int from, int to) {
        LOGGER.debug("Reversing {}", bytes);
        for (int i = from; i < Math.ceil((to - from) / 2f) + from; i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[to - i + from];
            bytes[to - i + from] = temp;
        }
        LOGGER.debug("Reversed {}", bytes);
    }

    /**
     * Parses a hex string to byte array
     *
     * @param hexString the hex string to parse
     * @return the encoded byte array
     * @throws IllegalArgumentException thrown if hexString is invalid
     */
    public static byte[] parseHexString(String hexString) throws IllegalArgumentException {
        final int len = hexString.length();

        // "111" is not a valid hex encoding.
        if (len % 2 != 0)
            throw new IllegalArgumentException("hexBinary needs to be even-length: " + hexString);

        byte[] out = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            int h = hexToByte(hexString.charAt(i));
            int l = hexToByte(hexString.charAt(i + 1));
            if (h == -1 || l == -1)
                throw new IllegalArgumentException("contains illegal character for hexBinary: " + hexString);

            out[i / 2] = (byte) (h * 16 + l);
        }

        return out;
    }

    /**
     * Converts hex characters to int value of byte
     *
     * @param charToConvert the char to convert to byte
     * @return the int value of the converted byte
     */
    private static int hexToByte(char charToConvert) {
        if ('0' <= charToConvert && charToConvert <= '9') return charToConvert - '0';
        if ('A' <= charToConvert && charToConvert <= 'F') return charToConvert - 'A' + 10;
        if ('a' <= charToConvert && charToConvert <= 'f') return charToConvert - 'a' + 10;
        return -1;
    }

    /**
     * Encodes a byte array to an hex string in lower case as default
     *
     * @param byteArray the byte array to encode
     * @return the encoded hex string
     */
    public static String encodeHexString(byte[] byteArray) {
        return encodeHexString(byteArray, HexCase.LOWER);
    }

    /**
     * Encodes a byte array to an hex string
     *
     * @param byteArray the byte array to encode
     * @param hexCase upper or lower hex code case to use
     * @return the encoded hex string
     */
    public static String encodeHexString(byte[] byteArray, HexCase hexCase) {
        StringBuilder r = new StringBuilder(byteArray.length * 2);
        for (byte b : byteArray) {
            r.append(hexCase.getHexCodes()[(b >> 4) & 0xF]);
            r.append(hexCase.getHexCodes()[(b & 0xF)]);
        }
        return r.toString();
    }
}
