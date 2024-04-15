package com.syntifi.crypto.key.deterministic;

import com.syntifi.crypto.key.encdec.Hex;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * The procedure to implement BIP32 or SLIP 10 to generate deterministic keys hierarchically
 * https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki
 * https://github.com/satoshilabs/slips/blob/master/slip-0010.md
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.3.0
 */
public class HierarchicalDeterministicKey {
    private final static Long MAX_VALUE_INDEX = 2147483648L;

    /**
     *
     * @param seed           byte array containing the seed
     * @param init           chaincode, that is the rightmost 32 bytes of the key
     * @param derivationPath path to follow when deriving the key
     * @return derived key in byte array
     * @throws IOException if an error occurs when processing the bytestream
     */
    public static byte[] getFromSeed(byte[] seed, byte[] init, int[] derivationPath) throws IOException {
        byte[] key = HierarchicalDeterministicKey.getMasterKeyFromSeed(seed, init);
        byte[] iL;
        byte[] iR;
        iL = Arrays.copyOfRange(key, 0, 32);
        iR = Arrays.copyOfRange(key, 32, 64);
        for (int i : derivationPath) {
            key = HierarchicalDeterministicKey.childKeyDerivation(
                    iL, iR, HierarchicalDeterministicKey.longToBytes(MAX_VALUE_INDEX + i));
            iL = Arrays.copyOfRange(key, 0, 32);
            iR = Arrays.copyOfRange(key, 32, 64);
        }
        return key;
    }

    /**
     * Derives the masterkey given a seed byte array
     *
     * @param seed byte array containing the seed
     * @param key initial Hmac value
     * @return master key as a byte array
     */
    public static byte[] getMasterKeyFromSeed(byte[] seed, byte[] key) {
        HMac hMac = new HMac(new SHA512Digest());
        hMac.init(new KeyParameter(key));
        hMac.update(seed, 0, seed.length);
        byte[] result = new byte[hMac.getMacSize()];
        hMac.doFinal(result, 0);
        return result;
    }

    /**
     * CKD function in Bipt32 and Slip10
     *
     * @param key        master key, that is the leftmost 32 bytes of the key
     * @param chainCode  hierarchical chain code 'a/b/c' but in array format: { a, b, c}
     * @param init       chaincode, that is the rightmost 32 bytes of the key
     * @return master key as a byte array
     * @throws IOException if an error occurs when processing the bytestream
     */
    public static byte[] childKeyDerivation(byte[] key, byte[] chainCode, byte[] init) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        os.write(Hex.decode("00"));
        os.write(key);
        os.write(Arrays.copyOfRange(init, 4, 8));
        HMac hMac = new HMac(new SHA512Digest());
        hMac.init(new KeyParameter(chainCode));
        hMac.update(os.toByteArray(), 0, os.size());
        byte[] result = new byte[hMac.getMacSize()];
        hMac.doFinal(result, 0);
        return result;
    }

    private static byte[] longToBytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(x);
        return buffer.array();
    }
}
