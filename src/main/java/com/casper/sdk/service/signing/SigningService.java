package com.casper.sdk.service.signing;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.Security;

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
     * @return the files loaded into a AsymmetricCipherKeyPair
     */
    public AsymmetricCipherKeyPair loadKeyPair(final InputStream publicKeyIn,
                                               final InputStream privateKeyIn,
                                               final SignatureAlgorithm algorithm) {

        return getSigner(algorithm).loadKeyPair(publicKeyIn, privateKeyIn);
    }


    public byte[] signWithPrivateKey(final AsymmetricKeyParameter privateKey,
                                     final byte[] toSign,
                                     final SignatureAlgorithm algorithm) {

        return getSigner(algorithm).signWithPrivateKey(privateKey, toSign);
    }


    public boolean verifySignature(final AsymmetricKeyParameter publicKeyParameters,
                                   final byte[] toSign,
                                   final byte[] signature,
                                   final SignatureAlgorithm algorithm) {

        return getSigner(algorithm).verifySignature(publicKeyParameters, toSign, signature);
    }

    private Signer getSigner(SignatureAlgorithm signatureAlgorithm) {
        return signerFactory.getSigner(signatureAlgorithm);
    }
}
