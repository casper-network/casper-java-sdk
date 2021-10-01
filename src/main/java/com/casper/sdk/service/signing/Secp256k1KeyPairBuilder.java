package com.casper.sdk.service.signing;

import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.SignatureAlgorithm;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.math.ec.ECPoint;

import java.security.KeyPair;
import java.security.PublicKey;

/**
 * Signer for the Secp256k1 algorithm
 */
public class Secp256k1KeyPairBuilder extends AbstractKeyPairBuilder {

    public static final String CURVE_NAME = "secp256k1";
    public static final String ALGORITHM = "ECDSA";

    Secp256k1KeyPairBuilder() {
        super(SignatureAlgorithm.SECP256K1);
    }

    public KeyPair generateKeyPair() {
        return generateKeyPair(ALGORITHM, CURVE_NAME);
    }

    @Override
    public boolean isSupportedPublicKey(final PublicKey publicKey) {
        return publicKey instanceof BCECPublicKey && ALGORITHM.equalsIgnoreCase(publicKey.getAlgorithm());
    }

    @Override
    public byte[] getPublicKeyRawBytes(final PublicKey publicKey) {
        ECPoint q = ((BCECPublicKey) publicKey).getQ();
        return ByteUtils.concat(
                ByteUtils.toByteArray(SignatureAlgorithm.SECP256K1.getValue()),
                q.getEncoded(true)
        );
    }
}
