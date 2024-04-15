package com.syntifi.crypto.key.hash;

import org.bouncycastle.crypto.digests.Blake2bDigest;

/**
 * Blake2 Hash helper class
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
public class Blake2b {
    /**
     * returns a Blake2b Hash of size length in bytes
     *
     * @param input byte array to hash
     * @param length desired output length in bytes
     * @return a byte array of size 'length' bytes
     */
    public static byte[] digest(byte[] input, int length) {
        Blake2bDigest d = new Blake2bDigest(length * 8);
        d.update(input, 0, input.length);
        byte[] result = new byte[d.getDigestSize()];
        d.doFinal(result, 0);
        return result;
    }
}
