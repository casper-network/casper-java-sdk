package com.casper.sdk.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.security.*;
import java.security.spec.EdECPrivateKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.NamedParameterSpec;
import java.util.regex.Pattern;

public class SigningService {

    public byte[] signWithKey(final PrivateKey privateKey, byte[] data) {

        try {

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
        byte[] privateKeyBytes = parse.matcher(key).replaceFirst("$1")
                .replace("\n", "").replace("\r", "").getBytes(StandardCharsets.UTF_8);

        try {
            final PrivateKey privateKey = generateEdDSAKey(privateKeyBytes);
            return signWithKey(privateKey, toSign);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new com.casper.sdk.exceptions.SignatureException("signWithPath", e);
        }
    }

    public PrivateKey generateEdDSAKey() {

        try {

            return KeyPairGenerator.getInstance(NamedParameterSpec.ED25519.getName()).generateKeyPair().getPrivate();
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to generate signature :", e.getCause());
        }
    }


    public byte[] signWithPrivateKey(final byte[] privateKeyBytes, final byte[] toSign) {

        try {

            //  byte[] encode = Base64.getEncoder().encode(privateKeyBytes);
            // Ed25519

            final PrivateKey privateKey = generateEdDSAKey(privateKeyBytes);
            final Signature signature = Signature.getInstance(NamedParameterSpec.ED25519.getName());
            signature.initSign(privateKey);
            // the message to sign
            signature.update(toSign);
            return signature.sign();

        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to generate signature :", e.getCause());
        }
    }

    private PrivateKey generateEdDSAKey(byte[] privateKeyBytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        final EdECPrivateKeySpec keySpec = new EdECPrivateKeySpec(NamedParameterSpec.ED25519, privateKeyBytes);
        NamedParameterSpec params = keySpec.getParams();
        return KeyFactory.getInstance(NamedParameterSpec.ED25519.getName()).generatePrivate(keySpec);
    }
}
