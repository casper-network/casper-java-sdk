package com.casper.sdk.service.signing;

import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.SignatureAlgorithm;
import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPublicKey;

import java.security.KeyPair;
import java.security.PublicKey;

/**
 * Creates  for the Ed25519 algorithm key paris
 */
class Ed25519KeyPariBuilder extends AbstractKeyPairBuilder {

    public static final String ALGORITHM = "Ed25519";

    Ed25519KeyPariBuilder() {
        super(SignatureAlgorithm.ED25519);
    }

    @Override
    public KeyPair generateKeyPair() {
        return generateKeyPair(ALGORITHM, ALGORITHM);
    }

    @Override
    public boolean isSupportedPublicKey(final PublicKey publicKey) {
        return publicKey instanceof BCEdDSAPublicKey && ALGORITHM.equalsIgnoreCase(publicKey.getAlgorithm());
    }

    @Override
    public byte[] getPublicKeyRawBytes(final PublicKey publicKey) {
        return ByteUtils.concat(
                ByteUtils.toByteArray(SignatureAlgorithm.ED25519.getValue()),
                ((BCEdDSAPublicKey) publicKey).getPointEncoding()
        );
    }

}

