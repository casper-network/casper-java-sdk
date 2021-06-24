package com.casper.sdk.service;

import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.NamedParameterSpec;
import java.security.spec.X509EncodedKeySpec;
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

        final byte[] privateKeyBytes = loadKeyBytes(keyPath);

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

        final byte[] publicKeyBytes = loadKeyBytes(publicKeyPath);
        final Ed25519PublicKeyParameters publicKeyParameters = new Ed25519PublicKeyParameters(publicKeyBytes, 0);
        final Signer verifier = new Ed25519Signer();
        verifier.init(false, publicKeyParameters);
        verifier.update(toSign, 0, toSign.length);
        return verifier.verifySignature(signature);
    }


    public RSAPublicKey readPublicKeyBC(String keyPath) throws Exception {

        final File file =new File(keyPath);

        if (!file.isFile() || !file.exists()) {
            throw new FileNotFoundException(String.format("Path [%s] invalid", keyPath));
        }

        KeyFactory factory = KeyFactory.getInstance("RSA");

        try (FileReader keyReader = new FileReader(file);

            PemReader pemReader = new PemReader(keyReader)) {

            PemObject pemObject = pemReader.readPemObject();
            byte[] content = pemObject.getContent();
            X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
            return (RSAPublicKey) factory.generatePublic(pubKeySpec);
        }
    }


    public RSAPublicKey readPublicKey(String keyPath) throws Exception {
        final File file =new File(keyPath);

        if (!file.isFile() || !file.exists()) {
            throw new FileNotFoundException(String.format("Path [%s] invalid", keyPath));
        }

        final String key = Files.readString(file.toPath(), Charset.forName("ISO_8859_1"));

        String publicKeyPEM = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PUBLIC KEY-----", "");

        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    }



    byte[] loadKeyBytes(final String keyPath) {

           try {
            final File file =new File(keyPath);

            if (!file.isFile() || !file.exists()) {
                throw new FileNotFoundException(String.format("Path [%s] invalid", keyPath));
            }

            final String key = Files.readString(file.toPath(), Charset.forName("ISO_8859_1"));

            final Pattern parse = Pattern.compile("(?m)(?s)^---*BEGIN.*---*$(.*)^---*END.*---*$.*");

            String s = parse.matcher(key)
                    .replaceFirst("$1")
                    .replace("\n", "")
                    .replace("\r", "");
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);

            byte[] array = new byte[64];
            Integer decode = Base64.getDecoder().decode(bytes, array);

            byte b = decode.byteValue();

            return Base64.getDecoder().decode(
                    parse.matcher(key)
                            .replaceFirst("$1")
                            .replace("\n", "")
                            .replace("\r", "")
                            .getBytes(StandardCharsets.UTF_8)
            );

        } catch (Exception ex) {
            throw new InvalidPathException(String.format("Path [%s] invalid", keyPath), ex.getMessage());
        }
    }
}
