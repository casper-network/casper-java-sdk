package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import com.casper.sdk.types.Algorithm;
import com.casper.sdk.types.CLPublicKey;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;

import java.io.*;
import java.security.*;

/**
 * The service for signing
 */
public class SigningService {

    /** The name of the BouncyCastleProvider */
    static final String PROVIDER = BouncyCastleProvider.PROVIDER_NAME;

    static {
        // Java 8 does not support the algorithms we need so must use BouncyCastleProvider
        Security.addProvider(new BouncyCastleProvider());
    }

    /** The factory for key pair generation */
    private final KeyPairFactory keyPairFactory = new KeyPairFactory();

    /**
     * Generates a key pair for the specified algorithm.
     *
     * @param algorithm the algorithm of new key pair to generate
     * @return a new key pair of the specified algorithm
     */
    public KeyPair generateKeyPair(final Algorithm algorithm) {
        return getKeyPairBuilder(algorithm).generateKeyPair();
    }

    /**
     * Loads the key pairs from the provide file
     *
     * @param publicKeyFile  the public key .pem file
     * @param privateKeyFile the private key .pem file
     * @return the files loaded into a AsymmetricCipherKeyPair
     */
    public KeyPair loadKeyPair(final File publicKeyFile, final File privateKeyFile) {
        try {
            return loadKeyPair(new FileInputStream(publicKeyFile), new FileInputStream(privateKeyFile));
        } catch (FileNotFoundException e) {
            throw new com.casper.sdk.exceptions.SignatureException(e);
        }
    }

    /**
     * Loads the key pairs from the provided streams
     *
     * @param publicKeyIn  the public key .pem file input stream
     * @param privateKeyIn the private key .pem file input stream
     * @return the files loaded into a AsymmetricCipherKeyPair
     */
    public KeyPair loadKeyPair(final InputStream publicKeyIn, final InputStream privateKeyIn) {
        return new KeyPair(toPublicKey(publicKeyIn), toPrivateKey(privateKeyIn));
    }

    /**
     * Signs a message using the provided key
     *
     * @param privateKey the private key to sign with
     * @param toSign     the message to sign
     * @return the signed message
     */
    public byte[] signWithPrivateKey(final PrivateKey privateKey, final byte[] toSign) {

        try {
            final Signature sig = Signature.getInstance(privateKey.getAlgorithm(), PROVIDER);
            sig.initSign(privateKey);
            sig.update(toSign);
            return sig.sign();
        } catch (Exception e) {
            throw new SignatureException(e);
        }
    }

    /**
     * Obtains the java security public keys raw and converts to a casper labs public key.
     *
     * @param publicKey the public key to obtain the raw bytes from
     * @return the raw bytes
     */
    public CLPublicKey toClPublicKey(final PublicKey publicKey) {
        return new CLPublicKey(getKeyPairBuilderForPublicKey(publicKey).getPublicKeyRawBytes(publicKey));
    }

    /**
     * Obtains the java security public keys raw and converts to a casper labs public key.
     *
     * @param publicKey the public key to obtain the raw bytes from
     * @return the raw bytes
     */
    public PublicKey fromClPublicKey(final CLPublicKey publicKey) {
        return getKeyPairBuilder(publicKey.getAlgorithm()).createPublicKey(publicKey.getBytes());
    }

    /**
     * Verifies a signed message
     *
     * @param publicKey the public key to  verify signature with
     * @param toSign    the message to sign
     * @param signature the signed message
     * @return true if the signature is valid otherwise false
     */
    public boolean verifySignature(final PublicKey publicKey, final byte[] toSign, final byte[] signature) {

        try {
            final Signature sig = Signature.getInstance(publicKey.getAlgorithm(), PROVIDER);
            sig.initVerify(publicKey);
            sig.update(toSign);
            return sig.verify(signature);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Writes a key to a PEM file
     *
     * @param out the stream to write a key to
     * @param key the  key to write in a .PEM file
     */
    public void writeKey(final OutputStream out, final Key key) {

        try {
            final JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(new OutputStreamWriter(out));
            jcaPEMWriter.writeObject(key);
            jcaPEMWriter.flush();
            jcaPEMWriter.close();
        } catch (IOException e) {
            throw new SignatureException(e);
        }
    }

    PrivateKey toPrivateKey(final InputStream privateKeyIn) {

        try {
            final PEMParser pemParser = new PEMParser(new InputStreamReader(privateKeyIn));
            final Object object = pemParser.readObject();
            final JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(PROVIDER);
            if (object instanceof PrivateKeyInfo) {
                return converter.getPrivateKey((PrivateKeyInfo) object);
            } else if (object instanceof PEMKeyPair) {
                return converter.getPrivateKey(((PEMKeyPair) object).getPrivateKeyInfo());
            } else {
                throw new SignatureException("Unknown object type " + object);
            }
        } catch (IOException e) {
            throw new SignatureException(e);
        }
    }

    PublicKey toPublicKey(final InputStream publicKeyIn) {
        try {
            final PEMParser pemParser = new PEMParser(new InputStreamReader(publicKeyIn));
            final Object object = pemParser.readObject();
            final JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider(PROVIDER);
            return converter.getPublicKey((SubjectPublicKeyInfo) object);
        } catch (IOException e) {
            throw new SignatureException(e);
        }
    }

    private KeyPairBuilder getKeyPairBuilder(final Algorithm algorithm) {
        return keyPairFactory.getKeyPairBuilder(algorithm);
    }

    private KeyPairBuilder getKeyPairBuilderForPublicKey(final PublicKey publicKey) {
        return keyPairFactory.getKeyPairBuilderForPublicKey(publicKey);
    }
}
