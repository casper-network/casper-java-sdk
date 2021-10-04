package com.casper.sdk.service.hash;

import com.casper.sdk.exceptions.HashException;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.SignatureAlgorithm;
import com.rfksystems.blake2b.Blake2b;
import com.rfksystems.blake2b.security.Blake2bProvider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * Simple service to provide a 32 bit blake2b hash A string key is passed in, once it has been validated a blake2b hash
 * is returned
 */
public class HashService {

    static {
        Security.addProvider(new Blake2bProvider());
    }

    /**
     * Get the blake2b hash
     *
     * @param accountKey string key used to generate the hash
     * @return 32 bit blake2b hash
     */
    public String getAccountHash(final String accountKey) {
        return ByteUtils.encodeHexString(getAccountHash(ByteUtils.decodeHex(accountKey)));
    }

    /**
     * Create a 32byte hashed array from the provided byte array
     *
     * @param accountKey the input bytes
     * @return a hashed 32 byte array as a hex string
     */
    public byte[] getAccountHash(final byte[] accountKey) {

        try {
            final MessageDigest digest = MessageDigest.getInstance(Blake2b.BLAKE2_B_256);
            digest.update(getAlgoNameBytes(accountKey));
            digest.update(new byte[1]);
            digest.update(accountKey, 1, accountKey.length - 1);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new HashException("Error getAccountHash", e);
        }
    }

    /**
     * Create a 32byte hashed array from the provided byte array
     *
     * @param in the input bytes
     * @return a hashed 32 byte array as a hex string
     */
    public byte[] getHash(final byte[] in) {
        try {
            final MessageDigest digest = MessageDigest.getInstance(Blake2b.BLAKE2_B_256);
            digest.update(in);
            return digest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new HashException("Error getHash", e);
        }
    }

    private byte[] getAlgoNameBytes(byte[] key) {

        if (key == null || key.length < 33) {
            throw new IllegalArgumentException("Key size must be equal or greater than 66 chars");
        }

        final SignatureAlgorithm keyAlgorithm = SignatureAlgorithm.fromId((char) key[0]);

        switch (keyAlgorithm) {

            case ED25519:
                if (key.length != 33) {
                    throw new IllegalArgumentException("Key length must be 66 chars (key " + key.length + ")");
                }
                break;
            case SECP256K1:
                if (key.length != 34) {
                    throw new IllegalArgumentException("Key length must be 68 chars");
                }
                break;

            default:
                throw new IllegalArgumentException(String.format("Unknown key prefix: [%s]", key[0]));
        }

        return keyAlgorithm.name().toLowerCase().getBytes(StandardCharsets.UTF_8);
    }
}