package com.casper.sdk.service;

import com.casper.sdk.service.serialization.util.ByteUtils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.*;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.NamedParameterSpec;

public class SigningService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Loads the key pairs from the provide file
     *
     * @param publicKeyFile  the public key .pem file
     * @param privateKeyFile the private key .pem file
     * @return the files loaded into a AsymmetricCipherKeyPair
     * @throws IOException if the is a problem loading the files
     */
    public AsymmetricCipherKeyPair loadKeyPair(final File publicKeyFile, final File privateKeyFile) throws IOException {
        return loadKeyPair(new FileInputStream(publicKeyFile), new FileInputStream(privateKeyFile));
    }

    /**
     * Loads the key pairs from the provide streams
     *
     * @param publicKeyIn  the public key .pem file input stream
     * @param privateKeyIn the private key .pem file input stream
     * @return the files loaded into a AsymmetricCipherKeyPair
     * @throws IOException if the is a problem loading the files
     */
    public AsymmetricCipherKeyPair loadKeyPair(final InputStream publicKeyIn, final InputStream privateKeyIn) throws IOException {

        final byte[] publicBytes = ByteUtils.truncateBytes(this.readPemFile(publicKeyIn), 32);
        final byte[] secretBytes = ByteUtils.truncateBytes(this.readPemFile(privateKeyIn), 32);

        return new AsymmetricCipherKeyPair(
                new Ed25519PublicKeyParameters(publicBytes),
                new Ed25519PrivateKeyParameters(secretBytes)
        );
    }


    public AsymmetricCipherKeyPair generateEdDSAKey() {

        SecureRandom RANDOM = new SecureRandom();
        Ed25519KeyPairGenerator keyPairGenerator = new Ed25519KeyPairGenerator();
        keyPairGenerator.init(new Ed25519KeyGenerationParameters(RANDOM));
        return keyPairGenerator.generateKeyPair();
    }

    public byte[] signWithPrivateKey(final AsymmetricKeyParameter privateKey, final byte[] toSign) {

        try {
            Signer signer = new Ed25519Signer();
            signer.init(true, privateKey);
            signer.update(toSign, 0, toSign.length);
            return signer.generateSignature();

        } catch (CryptoException | DataLengthException e) {
            throw new IllegalArgumentException("Failed to generate signature :", e.getCause());
        }
    }

    private Signer generateEdDSAKey(byte[] privateKeyBytes) {

        final Ed25519PrivateKeyParameters privateKeyParameters = new Ed25519PrivateKeyParameters(privateKeyBytes, 0);
        final Ed25519Signer ed25519Signer = new Ed25519Signer();
        ed25519Signer.init(true, privateKeyParameters);
        return ed25519Signer;
    }

    public boolean verifySignature(final AsymmetricKeyParameter publicKeyParameters, final byte[] toSign, final byte[] signature) {

        final Signer verifier = new Ed25519Signer();
        verifier.init(false, publicKeyParameters);
        verifier.update(toSign, 0, toSign.length);
        return verifier.verifySignature(signature);
    }


    byte[] readPemFile(final InputStream keyStream) throws IOException {
        final PemReader pemReader = new PemReader(new InputStreamReader(keyStream));
        final PemObject pemObject = pemReader.readPemObject();
        return pemObject.getContent();
    }
}
