package com.syntifi.crypto.key.checksum;

import com.syntifi.crypto.key.encdec.Hex;
import com.syntifi.crypto.key.hash.Blake2b;
import com.syntifi.crypto.key.hash.Keccak256;

/**
 * Mixed checksumEncoding
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.5.0
 */
public class MixedCaseChecksum {

    /**
     * Implementation of the EIP-55: Mixed-case checksum address encoding
     * Documentation available at: https://eips.ethereum.org/EIPS/eip-55
     *
     * @param value Hex string
     * @return
     */
    public static String checksumEncodeEIP55(String value) {
        char[] hash = Hex.encode(Keccak256.digest(value.toLowerCase().getBytes())).toCharArray();
        char[] chars = value.toCharArray();
        char[] encoded = new char[chars.length];
        for (int i = 0; i < chars.length; i++) {
            encoded[i] = Character.digit(hash[i], 16) > 7
                    ? Character.toUpperCase(chars[i])
                    : Character.toLowerCase(chars[i]);
        }
        return String.valueOf(encoded);
    }

    /**
     * Implementation of the CEP-57
     * Documentation available at: https://github.com/casper-network/ceps/blob/master/text/0057-checksummed-addresses.md
     *
     * @param value Hex string
     * @return
     */
    public static String checksumEncodeCEP57(String value) {
        byte[] hash = Blake2b.digest(Hex.decode(value), 32);
        char[] chars = value.toCharArray();
        char[] encoded = new char[chars.length];
        int whichByte = 0;
        int whichBit = 0;
        for (int i = 0; i < chars.length; i++) {
            boolean bitSet = (Byte.toUnsignedInt(hash[whichByte]) & (1<<whichBit)) != 0;
            if (Character.isAlphabetic(chars[i])) {
                encoded[i] = bitSet ? Character.toUpperCase(chars[i]) : Character.toLowerCase(chars[i]);
                whichByte = (whichByte + whichBit/7) % hash.length;
                whichBit = (whichBit + 1) % 8;
            } else {
                encoded[i] = chars[i];
            }
        }
        return String.valueOf(encoded);
    }
}
