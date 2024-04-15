package com.syntifi.crypto.key.encdec;

/**
 * Hex encoder/decoder
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.3.0
 */
public class Hex {

    /**
     * Byte array encoder
     *
     * @param bytes byte array
     * @return Hex String
     */
    public static String encode(byte[] bytes) {
        return org.bouncycastle.util.encoders.Hex.toHexString(bytes);
    }

    /**
     * Hex string decoder
     *
     * @param hex Hex string
     * @return byte array
     */
    public static byte[] decode(String hex) {
        return org.bouncycastle.util.encoders.Hex.decode(hex);
    }

}
