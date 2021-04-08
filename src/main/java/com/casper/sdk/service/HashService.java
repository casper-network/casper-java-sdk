package com.casper.sdk.service;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import com.rfksystems.blake2b.Blake2b;
import com.rfksystems.blake2b.security.Blake2bProvider;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

/**
 * Simple service to provide a 32 bit blake2b hash
 * A string key is passed in, once it has been validated
 * a blake2b hash is returned
 */
public class HashService {

    /**
     * Get the blake2b hash
     *
     * @param accountKey string key used to generate
     *            the hash
     * @return 32 bit blake2b hash
     * @throws NoSuchAlgorithmException library error
     * @throws DecoderException digest error
     */
    public static String getAccountHash(final String accountKey) throws NoSuchAlgorithmException, DecoderException {

        Security.addProvider(new Blake2bProvider());

        final MessageDigest digest = MessageDigest.getInstance(Blake2b.BLAKE2_B_256);

        digest.update(getAlgo(accountKey).getBytes(StandardCharsets.UTF_8));
        digest.update(new byte[1]);
        digest.update(Hex.decodeHex(accountKey.substring(2).toCharArray()));

        return Hex.encodeHexString(digest.digest());
    }


    private static String getAlgo(final String key){

        final String algo;

        if (key == null || key.length() < 66){
            throw new IllegalArgumentException("Key size must be equal or greater than 66 chars");
        }

        try{
            algo = TypeEnums.KeyAlgorithm.valueOf(Integer.parseInt(key.substring(0, 2))).toString().toLowerCase();
            switch(key.substring(0, 2)){
                case "01":
                    if (key.length() != 66){
                        throw new IllegalArgumentException("Key length must be 66 chars");
                    }
                    break;
                case "02":
                    if (key.length() != 68){
                        throw new IllegalArgumentException("Key length must be 68 chars");
                    }
                    break;
            }

        } catch (Exception e){
            throw new IllegalArgumentException(String.format("Unknown key prefix: [%s]", key.substring(0, 2)));
        }

        return algo;

    }


}