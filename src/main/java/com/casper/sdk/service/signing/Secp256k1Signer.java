package com.casper.sdk.service.signing;

import java.security.KeyPair;

/**
 * Signer for the Secp256k1 algorithm
 */
public class Secp256k1Signer extends AbstractSigner {

    public static final String CURVE_NAME = "secp256k1";
    public static final String ALGORITHM = "ECDSA";

    Secp256k1Signer() {
        super(SignatureAlgorithm.SECP256K1);
    }

    public KeyPair generateKeyPair() {
        return generateKeyPair(ALGORITHM, CURVE_NAME);
    }


}
