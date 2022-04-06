package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import com.casper.sdk.types.Algorithm;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
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

    /**
     * Converts an DER ASN.1 signature to a P1363 Signature
     *
     * @param asn1EncodedSignature the DER signature bytes
     * @return the P1363 signature
     */
    @Override
    public byte[] convertFromDER(byte[] asn1EncodedSignature) {
        final ASN1Sequence seq = ASN1Sequence.getInstance(asn1EncodedSignature);
        final BigInteger r = ((ASN1Integer) seq.getObjectAt(0)).getValue();
        final BigInteger s = ((ASN1Integer) seq.getObjectAt(1)).getValue();
        int n = (r.bitLength() + 7) / 8;
        // round up to the nearest even integer
        //noinspection IntegerDivisionInFloatingPointContext
        n = Math.round((n + 1) / 2) * 2;
        final byte[] out = new byte[2 * n];
        toFixed(r, out, 0, n);
        toFixed(s, out, n, n);
        return out;
    }

    /**
     * Converts a P1363 encoded signature to a DER ASN1 signature
     *
     * @param p1363EncodedSignature the signature the convert
     * @return a DER ASN.1 signature
     */
    @Override
    public byte[] convertToDER(byte[] p1363EncodedSignature) {
        final int n = p1363EncodedSignature.length / 2;
        final BigInteger r = new BigInteger(+1, Arrays.copyOfRange(p1363EncodedSignature, 0, n));
        final BigInteger s = new BigInteger(+1, Arrays.copyOfRange(p1363EncodedSignature, n, n * 2));
        final ASN1EncodableVector v = new ASN1EncodableVector();
        v.add(new ASN1Integer(r));
        v.add(new ASN1Integer(s));
        try {
            return new DERSequence(v).getEncoded();
        } catch (IOException e) {
            throw new SignatureException(e);
        }
    }

    private void toFixed(BigInteger x, byte[] a, int off, int len) {
        final byte[] t = x.toByteArray();
        if (t.length == len + 1 && t[0] == 0) System.arraycopy(t, 1, a, off, len);
        else if (t.length <= len) System.arraycopy(t, 0, a, off + len - t.length, t.length);
        else throw new SignatureException("Invalid length");
    }
}
