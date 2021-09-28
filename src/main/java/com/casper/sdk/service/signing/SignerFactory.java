package com.casper.sdk.service.signing;

import java.util.HashMap;
import java.util.Map;

public class SignerFactory {

    private final Map<SignatureAlgorithm, Signer> signers = new HashMap<>();

    public SignerFactory() {
        registerSigner(new Ed25519Signer());
        registerSigner(new Secp256k1Signer());
    }

    void registerSigner(final Signer signer) {
        signers.put(signer.getAlgorithm(), signer);
    }

    Signer getSigner(final SignatureAlgorithm algorithm) {
        return signers.get(algorithm);
    }
}
