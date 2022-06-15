package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import com.casper.sdk.types.Algorithm;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPublicKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.lang.reflect.Constructor;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * Creates  for the Ed25519 algorithm key paris
 */
class Ed25519KeyPariBuilder extends AbstractKeyPairBuilder {

    public static final String ALGORITHM = "Ed25519";

    Ed25519KeyPariBuilder() {
        super(Algorithm.ED25519);
    }

    @Override
    public KeyPair generateKeyPair(final byte[] seed) {
        return generateKeyPair(ALGORITHM, ALGORITHM, seed);
    }

    @Override
    public boolean isSupportedPublicKey(final PublicKey publicKey) {
        return publicKey instanceof BCEdDSAPublicKey && ALGORITHM.equalsIgnoreCase(publicKey.getAlgorithm());
    }

    @Override
    public PublicKey createPublicKey(final byte[] publicKey) {

        try {
            // Fixme we must be able to do with KeyFactory
            final Constructor<BCEdDSAPublicKey> constructor = BCEdDSAPublicKey.class.getDeclaredConstructor(AsymmetricKeyParameter.class);
            constructor.setAccessible(true);
            return constructor.newInstance(new Ed25519PublicKeyParameters(publicKey));

        } catch (Exception e) {
            throw new SignatureException(e);
        }
    }

    @Override
    public byte[] signWithPrivateKey(final PrivateKey privateKey, final byte[] message) {
        try {
            final Signature sig = Signature.getInstance(privateKey.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
            sig.initSign(privateKey);
            sig.update(message);
            return sig.sign();


        } catch (Exception e) {
            throw new SignatureException(e);
        }
    }

    @Override
    public boolean verifySignature(final PublicKey publicKey, final byte[] message, final byte[] signature) {
        try {
            final Signature sig = Signature.getInstance(publicKey.getAlgorithm(), BouncyCastleProvider.PROVIDER_NAME);
            sig.initVerify(publicKey);
            sig.update(message);
            return sig.verify(signature);
        } catch (Exception e) {
            return false;
        }
    }
}

