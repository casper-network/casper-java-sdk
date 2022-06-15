package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.Algorithm;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.Sign;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

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

    @Override
    public KeyPair generateKeyPair(final byte[] seed) {
        return generateKeyPair(ALGORITHM, CURVE_NAME, seed);
    }

    @Override
    public boolean isSupportedPublicKey(final PublicKey publicKey) {
        return publicKey instanceof BCECPublicKey && ALGORITHM.equalsIgnoreCase(publicKey.getAlgorithm());
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

    @Override
    public byte[] signWithPrivateKey(final PrivateKey privateKey, final byte[] message) {

        // The Standard Java API create a DER format signature casper requires a
        final BCECPrivateKey bcecPrivateKey = (BCECPrivateKey) privateKey;
        final BigInteger d = bcecPrivateKey.getD();
        final ECKeyPair keyPair = ECKeyPair.create(d);
        final Sign.SignatureData signature = Sign.signMessage(Hash.sha256(message), keyPair, false);
        final ByteBuffer bb = ByteBuffer.allocate(signature.getR().length + signature.getS().length);
        bb.put(signature.getR());
        bb.put(signature.getS());
        return bb.array();
    }

    @Override
    public boolean verifySignature(final PublicKey publicKey, final byte[] message, final byte[] signature) {

        try {
            final byte[] x = ((BCECPublicKey) publicKey).getQ().getXCoord().getEncoded();
            final byte[] y = ((BCECPublicKey) publicKey).getQ().getYCoord().getEncoded();
            final byte[] shortPublicKey = getShortKey(new BigInteger(ByteUtils.concat(x, y)));
            final Sign.SignatureData signatureData = new Sign.SignatureData(
                    (byte) 27,
                    Arrays.copyOfRange(signature, 0, 32),
                    Arrays.copyOfRange(signature, 32, 64)
            );
            final BigInteger derivedKey = Sign.signedMessageHashToKey(Hash.sha256(message), signatureData);

            byte[] shortDerivedKey = getShortKey(derivedKey);
            boolean verified = Arrays.equals(shortDerivedKey, shortPublicKey);
            return verified;
        } catch (Exception e) {
            throw new SignatureException(e);
        }
    }

    /**
     * Gets a short key from a {@link BigInteger} key
     *
     * @param key the key as a {@link BigInteger}
     * @return short key as byte array
     */
    private byte[] getShortKey(final BigInteger key) {
        final String pubKeyPrefix = key.testBit(0) ? "03" : "02";
        final byte[] pubKeyBytes = Arrays.copyOf(key.toByteArray(), 32);
        return Hex.decode(pubKeyPrefix + Hex.toHexString(pubKeyBytes));
    }
}
