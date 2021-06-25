package com.casper.sdk.service;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.security.*;
import java.security.spec.NamedParameterSpec;

public class SigningService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public AsymmetricCipherKeyPair loadKeyPair(final String publicKeyPath, final String privateKeyPath) throws IOException {

        final byte[] publicBytes = this.truncateTo32Bytes(this.readPemFile(publicKeyPath));
        final byte[] secretBytes = this.truncateTo32Bytes(this.readPemFile(privateKeyPath));

        return new AsymmetricCipherKeyPair(
                new Ed25519PublicKeyParameters(publicBytes),
                new Ed25519PrivateKeyParameters(secretBytes)
        );
    }

    private byte[] truncateTo32Bytes(byte[] content) {
        byte[] secretBytes = new byte[32];
        int pstart = content.length - 32;
        System.arraycopy(content, pstart, secretBytes, 0, 32);
        return secretBytes;
    }

    public byte[] signWithKey(final PrivateKey privateKey, byte[] data) {

        try {
            signWithPrivateKey(privateKey.getEncoded(), data);
            final Signature signature = Signature.getInstance("Ed25519");
            signature.initSign(privateKey);
            signature.update(data);
            return signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new com.casper.sdk.exceptions.SignatureException("Error signing data", e);
        }
    }

    public byte[] signWithPath(final String keyPath, final byte[] toSign) {

        final byte[] privateKeyBytes = readPemFile(keyPath);

        return signWithPrivateKey(privateKeyBytes, toSign);
    }

    public PrivateKey generateEdDSAKey() {

        try {
            return KeyPairGenerator.getInstance(NamedParameterSpec.ED25519.getName(), BouncyCastleProvider.PROVIDER_NAME).generateKeyPair().getPrivate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to generate signature :", e.getCause());
        }
    }

    public byte[] signWithPrivateKey(final byte[] privateKeyBytes, final byte[] toSign) {

        try {
            final Signer privateKey = generateEdDSAKey(privateKeyBytes);
            privateKey.update(toSign, 0, toSign.length);
            return privateKey.generateSignature();

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to generate signature :", e.getCause());
        }
    }

    private Signer generateEdDSAKey(byte[] privateKeyBytes) {

        final Ed25519PrivateKeyParameters privateKeyParameters = new Ed25519PrivateKeyParameters(privateKeyBytes, 0);
        final Ed25519Signer ed25519Signer = new Ed25519Signer();
        ed25519Signer.init(true, privateKeyParameters);
        return ed25519Signer;
    }

    public boolean verifySignature(final String publicKeyPath, final byte[] toSign, final byte[] signature) {

        final byte[] publicKeyBytes = readPemFile(publicKeyPath);
        final Ed25519PublicKeyParameters publicKeyParameters = new Ed25519PublicKeyParameters(publicKeyBytes, 0);
        final Signer verifier = new Ed25519Signer();
        verifier.init(false, publicKeyParameters);
        verifier.update(toSign, 0, toSign.length);
        return verifier.verifySignature(signature);
    }




    byte[] readPemFile(final String keyPath) {

        try {
            final File file = new File(keyPath);

            if (!file.isFile() || !file.exists()) {
                throw new FileNotFoundException(String.format("Path [%s] invalid", keyPath));
            }

            final FileReader keyReader = new FileReader(keyPath);
            final PemReader pemReader = new PemReader(keyReader);
            final PemObject pemObject = pemReader.readPemObject();
            return pemObject.getContent();

        } catch (IOException ex) {
            throw new InvalidPathException(String.format("Path [%s] invalid", keyPath), ex.getMessage());
        }
    }
}
