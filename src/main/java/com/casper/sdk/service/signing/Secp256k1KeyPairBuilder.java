package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.Algorithm;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PublicKey;

import static com.casper.sdk.service.signing.SigningService.PROVIDER;

/**
 * Signer for the Secp256k1 algorithm
 */
public class Secp256k1KeyPairBuilder extends AbstractKeyPairBuilder {

    public static final String CURVE_NAME = "secp256k1";
    public static final String ALGORITHM = "ECDSA";

    Secp256k1KeyPairBuilder() {
        super(Algorithm.SECP256K1);
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
                ByteUtils.toByteArray(Algorithm.SECP256K1.getValue()),
                q.getEncoded(true)
        );
    }

    @Override
    public PublicKey createPublicKey(final byte[] publicKey) {

        try {
            final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM, PROVIDER);
            final ECParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec(CURVE_NAME);
            final ECPoint point = ecSpec.getCurve().decodePoint(publicKey);
            final ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, ecSpec);
            return keyFactory.generatePublic(pubSpec);
        } catch (Exception e) {
            throw new SignatureException(e);
        }
    }
}
