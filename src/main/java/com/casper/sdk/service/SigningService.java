package com.casper.sdk.service;

import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.security.*;
import java.security.spec.NamedParameterSpec;
import java.util.Base64;
import java.util.regex.Pattern;

public class SigningService {

    static {
        Security.addProvider(new BouncyCastleProvider());
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

        final File file;
        final String key;


        try {
            file = new File(keyPath);
            if (!file.isFile() || !file.exists()) {
                throw new FileNotFoundException(String.format("Path [%s] invalid", keyPath));
            }

            key = Files.readString(file.toPath(), Charset.forName("ISO_8859_1"));

        } catch (Exception ex) {
            throw new InvalidPathException(String.format("Path [%s] invalid", keyPath), ex.getMessage());
        }

        final Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");
        byte[] privateKeyBytes = Base64.getDecoder().decode(
                parse.matcher(key)
                        .replaceFirst("$1")
                        .replace("\n", "")
                        .replace("\r", "")
                        .getBytes(StandardCharsets.UTF_8)
        );

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
        Ed25519Signer ed25519Signer = new Ed25519Signer();
        ed25519Signer.init(true, privateKeyParameters);
        return ed25519Signer;
    }
}
