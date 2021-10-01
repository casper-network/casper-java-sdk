package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import com.casper.sdk.types.SignatureAlgorithm;

import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class KeyPairFactory {

    private final Map<SignatureAlgorithm, KeyPairBuilder> keyPairBuilderMap = new HashMap<>();

    public KeyPairFactory() {
        registerSigner(new Ed25519KeyPariBuilder());
        registerSigner(new Secp256k1KeyPairBuilder());
    }

    void registerSigner(final KeyPairBuilder signer) {
        keyPairBuilderMap.put(signer.getAlgorithm(), signer);
    }

    KeyPairBuilder getKeyPairBuilder(final SignatureAlgorithm algorithm) {
        return keyPairBuilderMap.get(algorithm);
    }

    public KeyPairBuilder getKeyPairBuilderForPublicKey(final PublicKey publicKey) {
        for (KeyPairBuilder keyPairBuilder : keyPairBuilderMap.values()) {
            if (keyPairBuilder.isSupportedPublicKey(publicKey)) {
                return keyPairBuilder;
            }
        }

        throw new SignatureException("Unsupported PublicKey " + publicKey);
    }
}
