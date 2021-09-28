package com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StringByteHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringByteHelper.class);

    private StringByteHelper() {
    }

    /**
     * Reverses a byte array, used to go from little to big endianess
     * 
     * @param bytes array of bytes to reverse
     */
    public static void reverse(byte[] bytes) {
        LOGGER.debug("Reversing bytes: {}", bytes);
        for (int i = 0; i < Math.abs(bytes.length / 2); i++) {
            byte temp = bytes[i];
            bytes[i] = bytes[bytes.length - i - 1];
            bytes[bytes.length - i - 1] = temp;
        }
        LOGGER.debug("Bytes reversed: {}", bytes);
    }

    /**
     * Helper method which converts hex-encoded {@link String} to byte array
     * 
     * @param hexString the hex-encoded {@link String} to decode
     * @return decoded array of bytes
     */
    public static byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] bytes = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            bytes[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        LOGGER.debug("Converted Hex {} to Bytes: {}", hexString, bytes);
        return bytes;
    }

    /**
     * Helper method which returns byte arrays as hex strings
     * 
     * @param bytes the byte array to encode
     * @return hex-encoded value {@link String}
     */
    public static String convertBytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte temp : bytes) {
            result.append(String.format("%02x", temp));
        }
        LOGGER.debug("Converted Bytes {} to Hex: {}", bytes, result);
        return result.toString();
    }
}
