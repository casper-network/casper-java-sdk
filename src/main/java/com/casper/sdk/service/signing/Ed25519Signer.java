package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import com.casper.sdk.service.serialization.util.ByteUtils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;

import java.io.InputStream;
import java.security.SecureRandom;

/**
 * Signer for the Ed25519 algorithm
 */ class Ed25519Signer extends AbstractSigner {

    Ed25519Signer() {
        super(SignatureAlgorithm.ED25519);
    }

    @Override
    public AsymmetricCipherKeyPair generateKeyPair() {
        final SecureRandom RANDOM = new SecureRandom();
        final Ed25519KeyPairGenerator keyPairGenerator = new Ed25519KeyPairGenerator();
        keyPairGenerator.init(new Ed25519KeyGenerationParameters(RANDOM));
        return keyPairGenerator.generateKeyPair();
    }

    @Override
    public AsymmetricCipherKeyPair loadKeyPair(final InputStream publicKeyIn, final InputStream privateKeyIn) {

        final byte[] publicBytes = ByteUtils.lastNBytes(this.readPemFile(publicKeyIn), 32);
        final byte[] secretBytes = ByteUtils.lastNBytes(this.readPemFile(privateKeyIn), 32);

        return new AsymmetricCipherKeyPair(
                new Ed25519PublicKeyParameters(publicBytes),
                new Ed25519PrivateKeyParameters(secretBytes)
        );
    }

    @Override
    public byte[] signWithPrivateKey(final AsymmetricKeyParameter privateKey, final byte[] toSign) {
        try {
            org.bouncycastle.crypto.Signer signer = new org.bouncycastle.crypto.signers.Ed25519Signer();
            signer.init(true, privateKey);
            signer.update(toSign, 0, toSign.length);
            return signer.generateSignature();

        } catch (CryptoException | DataLengthException e) {
            throw new SignatureException("Failed to generate signature :", e.getCause());
        }
    }

    @Override
    public boolean verifySignature(final AsymmetricKeyParameter publicKey, final byte[] toSign, final byte[] signature) {
        final org.bouncycastle.crypto.Signer verifier = new org.bouncycastle.crypto.signers.Ed25519Signer();
        verifier.init(false, publicKey);
        verifier.update(toSign, 0, toSign.length);
        return verifier.verifySignature(signature);
    }

}
