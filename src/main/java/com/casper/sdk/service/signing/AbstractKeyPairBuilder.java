package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.types.Algorithm;
import com.casper.sdk.types.CLType;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;

import static com.casper.sdk.service.signing.SigningService.PROVIDER;

abstract class AbstractKeyPairBuilder implements KeyPairBuilder {

    private static final TypesFactory TYPES_FACTORY = new TypesFactory();
    private final Algorithm algorithm;

    AbstractKeyPairBuilder(final Algorithm algorithm) {
        this.algorithm = algorithm;
    }

    @Override
    public Algorithm getAlgorithm() {
        return algorithm;
    }

    KeyPair generateKeyPair(final String algorithm, final String curve) {
        try {
            final KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm, PROVIDER);
            final ECGenParameterSpec ecGenParameterSpec = new ECGenParameterSpec(curve);
            keyPairGenerator.initialize(ecGenParameterSpec, new SecureRandom());
            return keyPairGenerator.generateKeyPair();
        } catch (Exception e) {
            throw new SignatureException(e);
        }
    }

    @Override
    public byte[] getPublicKeyRawBytes(final PublicKey publicKey) {
        return TYPES_FACTORY.getInstance(CLType.PUBLIC_KEY).serialize(publicKey);
    }
}
