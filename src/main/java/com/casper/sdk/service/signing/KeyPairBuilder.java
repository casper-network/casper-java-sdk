package com.casper.sdk.service.signing;


import com.casper.sdk.types.Algorithm;

import java.security.KeyPair;
import java.security.PublicKey;

/**
 * Interface implemented by classes that create keys/
 */
public interface KeyPairBuilder {

    /**
     * Generates a new key pair
     *
     * @return a new key pain
     */
    KeyPair generateKeyPair();

    /**
     * The algorithm of the signer
     *
     * @return the signers algorithm
     */
    Algorithm getAlgorithm();

    /**
     * Indicates if the provided public key has an algorithm that is supported by this signer
     *
     * @param publicKey the public key to test
     * @return true if the provide key is supported by the signer otherwise false
     */
    boolean isSupportedPublicKey(final PublicKey publicKey);

    /**
     * Obtains the raw bytes from a public key so can be used in casper types
     *
     * @param publicKey the public key to obtain the raw bytes from
     * @return the raw bytes of the provided key
     */
    byte[] getPublicKeyRawBytes(final PublicKey publicKey);

    /**
     * Creates a public key from the provided raw bytes
     *
     * @param publicKey the public key raw bytes
     * @return a new PublicKet created from the provided bytes
     */
    PublicKey createPublicKey(final byte[] publicKey);
}
