package com.casper.sdk.helper;

import com.casper.sdk.model.key.AlgorithmTag;
import com.syntifi.crypto.key.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;

/**
 * Key service provides methods to easily work with private and public keys
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.5.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CasperKeyHelper {

    /**
     * Returns a private key generated using secure random
     *
     * @return private key
     */
    public static Ed25519PrivateKey createRandomEd25519Key() {
        return Ed25519PrivateKey.deriveRandomKey();
    }

    /**
     * Returns a private key generated using secure random
     *
     * @return private key
     */
    public static Secp256k1PrivateKey createRandomSecp256k1Key() throws IOException {
        return Secp256k1PrivateKey.deriveRandomKey();
    }

    /**
     * Returns a public key generated form the private key
     *
     * @param privateKey private key
     * @return public key
     */
    public static Ed25519PublicKey derivePublicKey(Ed25519PrivateKey privateKey) {
        return (Ed25519PublicKey) privateKey.derivePublicKey();
    }

    /**
     * Returns a public key generated form the private key
     *
     * @param privateKey private key
     * @return public key
     */
    public static Secp256k1PublicKey derivePublicKey(Secp256k1PrivateKey privateKey) {
        return (Secp256k1PublicKey) privateKey.derivePublicKey();
    }
}
