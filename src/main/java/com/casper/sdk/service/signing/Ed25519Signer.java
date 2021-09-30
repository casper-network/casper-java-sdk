package com.casper.sdk.service.signing;

import java.security.KeyPair;

/**
 * Signer for the Ed25519 algorithm
 */
class Ed25519Signer extends AbstractSigner {

    Ed25519Signer() {
        super(SignatureAlgorithm.ED25519);
    }

    @Override
    public KeyPair generateKeyPair() {
        return generateKeyPair("Ed25519", "Ed25519");
    }

}

