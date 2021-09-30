package com.casper.sdk.service.signing;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.Security;

/**
 * The service for signing
 */
public class SigningService {

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private final SignerFactory signerFactory = new SignerFactory();

    public AsymmetricCipherKeyPair generateKeyPair(final SignatureAlgorithm algorithm) {
        return getSigner(algorithm).generateKeyPair();
    }

    /**
     * Loads the key pairs from the provide file
     *
     * @param publicKeyFile  the public key .pem file
     * @param privateKeyFile the private key .pem file
     * @param algorithm      the algorithm of the signature
     * @return the files loaded into a AsymmetricCipherKeyPair
     */
    public AsymmetricCipherKeyPair loadKeyPair(final File publicKeyFile,
                                               final File privateKeyFile,
                                               final SignatureAlgorithm algorithm) {
        try {
            return getSigner(algorithm).loadKeyPair(new FileInputStream(publicKeyFile), new FileInputStream(privateKeyFile));
        } catch (FileNotFoundException e) {
            throw new com.casper.sdk.exceptions.SignatureException(e);
        }
    }

    /**
     * Loads the key pairs from the provided streams
     *
     * @param publicKeyIn  the public key .pem file input stream
     * @param privateKeyIn the private key .pem file input stream
     * @param algorithm    the algorithm of the key pair to load
     * @return the files loaded into a AsymmetricCipherKeyPair
     */
    public AsymmetricCipherKeyPair loadKeyPair(final InputStream publicKeyIn,
                                               final InputStream privateKeyIn,
                                               final SignatureAlgorithm algorithm) {

        return getSigner(algorithm).loadKeyPair(publicKeyIn, privateKeyIn);
    }


    /**
     * Signs a message using the provided key
     *
     * @param privateKey the private key to sign with
     * @param toSign     the message to sign
     * @param algorithm  the algorithm of the keypair to sign with
     * @return the signed message
     */
    public byte[] signWithPrivateKey(final AsymmetricKeyParameter privateKey,
                                     final byte[] toSign,
                                     final SignatureAlgorithm algorithm) {

        return getSigner(algorithm).signWithPrivateKey(privateKey, toSign);
    }


    /**
     * Verifies a signed message
     *
     * @param publicKeyParameters the public key to  verify signature with
     * @param toSign              the message to sign
     * @param signature           the signed message
     * @param algorithm           the algorithm of the keypair to sign with
     * @return true if the signature is valid otherwise false
     */
    public boolean verifySignature(final AsymmetricKeyParameter publicKeyParameters,
                                   final byte[] toSign,
                                   final byte[] signature,
                                   final SignatureAlgorithm algorithm) {

        return getSigner(algorithm).verifySignature(publicKeyParameters, toSign, signature);
    }

    private Signer getSigner(final SignatureAlgorithm signatureAlgorithm) {
        return signerFactory.getSigner(signatureAlgorithm);
    }
}
