package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import com.casper.sdk.service.serialization.util.ByteUtils;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.DERSequenceGenerator;
import org.bouncycastle.asn1.DLSequence;
import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.generators.ECKeyPairGenerator;
import org.bouncycastle.crypto.params.*;
import org.bouncycastle.crypto.signers.ECDSASigner;
import org.bouncycastle.crypto.signers.HMacDSAKCalculator;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * Signer for the Secp256k1 algorithm
 */
public class Secp256k1Signer extends AbstractSigner {

     static class SecureRandomProvider {
        private static final SecureRandom publicSecureRandom = secureRandom();

        // Returns a shared instance of secure random intended to be used where the value is used publicly
        public static SecureRandom publicSecureRandom() {
            return publicSecureRandom;
        }

        public static SecureRandom createSecureRandom() {
            return secureRandom();
        }

        @SuppressWarnings("DoNotCreateSecureRandomDirectly")
        private static SecureRandom secureRandom() {
            return new SecureRandom();
        }
    }

    public static final String ALGORITHM = "ECDSA";
    public static final String CURVE_NAME = "secp256k1";
    public static final String PROVIDER = "BC";
    public static final ECDomainParameters CURVE;
    public static final BigInteger HALF_CURVE_ORDER;
    static final X9ECParameters curve = SECNamedCurves.getByName("secp256k1");
    static final ECDomainParameters domain = new ECDomainParameters(curve.getCurve(), curve.getG(), curve.getN(), curve.getH());

    // private static final KeyPairGenerator KEY_PAIR_GENERATOR;
    private static final BigInteger CURVE_ORDER;

    static {

        final X9ECParameters params = SECNamedCurves.getByName(CURVE_NAME);
        CURVE = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
        CURVE_ORDER = CURVE.getN();
        HALF_CURVE_ORDER = CURVE_ORDER.shiftRight(1);
    }

    Secp256k1Signer() {
        super(SignatureAlgorithm.SECP256K1);
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {

        final ECNamedCurveParameterSpec curve = ECNamedCurveTable.getParameterSpec("secp256k1");
        final ECDomainParameters domainParams = new ECDomainParameters(curve.getCurve(), curve.getG(), curve.getN(), curve.getH(), curve.getSeed());
        final SecureRandom secureRandom = new SecureRandom();
        final ECKeyGenerationParameters keyParams = new ECKeyGenerationParameters(domainParams, secureRandom);
        final ECKeyPairGenerator generator = new ECKeyPairGenerator();
        generator.init(keyParams);
        return generator.generateKeyPair();
    }

    @Override
    public AsymmetricCipherKeyPair loadKeyPair(final InputStream publicKeyIn, final InputStream privateKeyIn) {

        byte[] publicBytes = ByteUtils.lastNBytes(readPemFile(publicKeyIn), 33);
        final ECPublicKeyParameters ecPublicKeyParameters = toPublicKey(publicBytes);

        final byte[] secretBytes = ByteUtils.lastNBytes(readPemFile(privateKeyIn), 32);
        final ECPrivateKeyParameters ecPrivateKeyParameters = toPrivateKey(secretBytes);

        return new AsymmetricCipherKeyPair(ecPublicKeyParameters, ecPrivateKeyParameters);
    }

    @Override
    public byte[] signWithPrivateKey(final AsymmetricKeyParameter privateKey, final byte[] toSign) {

        final ECPrivateKeyParameters ecPrivateKey = (ECPrivateKeyParameters) privateKey;
        final ECDSASigner signer = new ECDSASigner(new HMacDSAKCalculator(new SHA256Digest()));
        signer.init(true, ecPrivateKey);
        final BigInteger[] signature = signer.generateSignature(toSign);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            final DERSequenceGenerator seq = new DERSequenceGenerator(baos);
            seq.addObject(new ASN1Integer(signature[0]));
            seq.addObject(new ASN1Integer(toCanonicalS(signature[1])));
            seq.close();
            return baos.toByteArray();
        } catch (IOException e) {
            throw new SignatureException(e);
        }
    }

    @Override
    public boolean verifySignature(final AsymmetricKeyParameter publicKey, final byte[] toSign, final byte[] signature) {
        final ASN1InputStream asn1 = new ASN1InputStream(signature);
        //noinspection TryFinallyCanBeTryWithResources
        try {
            final ECDSASigner signer = new ECDSASigner();
            signer.init(false, publicKey);

            final DLSequence seq = (DLSequence) asn1.readObject();
            final BigInteger r = ((ASN1Integer) seq.getObjectAt(0)).getPositiveValue();
            final BigInteger s = ((ASN1Integer) seq.getObjectAt(1)).getPositiveValue();
            return signer.verifySignature(toSign, r, s);
        } catch (Exception e) {
            return false;
        } finally {
            try {
                asn1.close();
            } catch (IOException ignored) {
                // Don't care here
            }
        }
    }

    private ECPublicKeyParameters toPublicKey(final byte[] publicKey) {
        return new ECPublicKeyParameters(curve.getCurve().decodePoint(publicKey), domain);
    }

    private ECPrivateKeyParameters toPrivateKey(final byte[] privateKey) {
        return new ECPrivateKeyParameters(new BigInteger(privateKey), domain);
    }

    private BigInteger toCanonicalS(final BigInteger s) {
        if (s.compareTo(HALF_CURVE_ORDER) <= 0) {
            return s;
        } else {
            return curve.getN().subtract(s);
        }
    }


}
