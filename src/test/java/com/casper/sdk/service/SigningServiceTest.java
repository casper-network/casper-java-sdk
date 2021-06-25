package com.casper.sdk.service;

import com.casper.sdk.service.serialization.util.ByteUtils;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.EdECPrivateKeySpec;
import java.security.spec.NamedParameterSpec;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

class SigningServiceTest {

    private static final String PRIVATE_KEY = "/account/user-1/secret_key.pem";
    private static final String PUBLIC_KEY = "/account/user-1/public_key.pem";

    private final SigningService signingService = new SigningService();


    @Test
    void generateKeyPair() throws Exception {

        FileReader keyReader = new FileReader(SigningServiceTest.class.getResource(PRIVATE_KEY).getFile());
        PemReader pemReader = new PemReader(keyReader);
        PemObject pemObject = pemReader.readPemObject();

        byte[] content = pemObject.getContent();
        byte[] secretBytes = new byte[32];
        int pstart = content.length - 32;
        System.arraycopy(content, pstart, secretBytes, 0, 32);

        byte[] keyBytes = signingService.readPemFile(SigningServiceTest.class.getResource(PUBLIC_KEY).getFile());
        byte[] publicBytes = new byte[32];
        int start = keyBytes.length - 32;
        System.arraycopy(keyBytes, start, publicBytes, 0, 32);

        final AsymmetricCipherKeyPair asymmetricCipherKeyPair = new AsymmetricCipherKeyPair(
                new Ed25519PublicKeyParameters(publicBytes),
                new Ed25519PrivateKeyParameters(secretBytes)
        );


        Ed25519PrivateKeyParameters privateKey = (Ed25519PrivateKeyParameters) asymmetricCipherKeyPair.getPrivate();
        Ed25519PublicKeyParameters publicKey = (Ed25519PublicKeyParameters) asymmetricCipherKeyPair.getPublic();

        // Copy of a deploy hash
        final byte[] message = {
                (byte) 153, (byte) 144, (byte) 19, (byte) 83, (byte) 219, (byte) 161, (byte) 143, (byte) 137, (byte) 59,
                (byte) 67, (byte) 187, (byte) 238, (byte) 65, (byte) 111, (byte) 80, (byte) 243, (byte) 142, (byte) 77,
                (byte) 113, (byte) 46, (byte) 2, (byte) 166, (byte) 121, (byte) 118, (byte) 34, (byte) 205, (byte) 123,
                (byte) 14, (byte) 215, (byte) 85, (byte) 234, (byte) 161
        };

        // create the signature
        Signer signer = new Ed25519Signer();
        signer.init(true, privateKey);
        signer.update(message, 0, message.length);
        byte[] signature = signer.generateSignature();
        // verify the signature
        Signer verifier = new Ed25519Signer();
        verifier.init(false, publicKey);
        verifier.update(message, 0, message.length);
        boolean shouldVerify = verifier.verifySignature(signature);

        assertThat(shouldVerify, is(true));
        // output
        byte[] privateKeyEncoded = privateKey.getEncoded();
        byte[] publicKeyEncoded = publicKey.getEncoded();
    }

    @Test
    void messWithKeys() throws Exception {

        KeyFactory factory = KeyFactory.getInstance(NamedParameterSpec.ED25519.getName());

        FileReader keyReader = new FileReader(SigningServiceTest.class.getResource(PRIVATE_KEY).getFile());
        PemReader pemReader = new PemReader(keyReader);
        PemObject pemObject = pemReader.readPemObject();
        byte[] content = pemObject.getContent();
        EdECPrivateKeySpec privateKeySpec = new EdECPrivateKeySpec(NamedParameterSpec.ED25519, content);
        KeyFactory keyFactory = KeyFactory.getInstance(NamedParameterSpec.ED25519.getName());
        PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

        byte[] encoded = privateKey.getEncoded();


        byte[] secretBytes = new byte[32];
        int pstart = content.length - 32;
        System.arraycopy(content, pstart, secretBytes, 0, 32);
        privateKeySpec = new EdECPrivateKeySpec(NamedParameterSpec.ED25519, secretBytes);
        privateKey = factory.generatePrivate(privateKeySpec);

        assertThat(privateKey, is(notNullValue()));


        byte[] keyBytes = signingService.readPemFile(SigningServiceTest.class.getResource(PUBLIC_KEY).getFile());
        byte[] publicBytes = new byte[32];
        int start = keyBytes.length - 32;
        System.arraycopy(keyBytes, start, publicBytes, 0, 32);


        //  01d30f6a241199e68217cb05abcefc7c8267c5226b8e644f1f8d0a79b87ed04f07 - 1st 01 is alg
        byte[] expectedKeyBytes = ByteUtils.decodeHex("d30f6a241199e68217cb05abcefc7c8267c5226b8e644f1f8d0a79b87ed04f07");

        //byte[] publicBytes = new byte[32];
        start = keyBytes.length - 32;
        System.arraycopy(keyBytes, start, publicBytes, 0, 32);


        assertThat(publicBytes, is(expectedKeyBytes));


        byte[] privateBytes = signingService.readPemFile(SigningServiceTest.class.getResource(PRIVATE_KEY).getFile());

        // byte [] secretBytes = new byte[32];
        //int pstart = keyBytes.length - 32;
        // System.arraycopy(privateBytes, pstart, secretBytes, 0, 32);

        //  assertThat(secretBytes.length, is(32));


        // Copy of a deploy hash
        final byte[] message = {
                (byte) 153, (byte) 144, (byte) 19, (byte) 83, (byte) 219, (byte) 161, (byte) 143, (byte) 137, (byte) 59,
                (byte) 67, (byte) 187, (byte) 238, (byte) 65, (byte) 111, (byte) 80, (byte) 243, (byte) 142, (byte) 77,
                (byte) 113, (byte) 46, (byte) 2, (byte) 166, (byte) 121, (byte) 118, (byte) 34, (byte) 205, (byte) 123,
                (byte) 14, (byte) 215, (byte) 85, (byte) 234, (byte) 161
        };

        final Ed25519PrivateKeyParameters privateKeyParameters = new Ed25519PrivateKeyParameters(privateBytes, 0);
        final Ed25519Signer ed25519Signer = new Ed25519Signer();
        ed25519Signer.init(true, privateKeyParameters);
        ed25519Signer.update(message, 0, message.length);
        byte[] signature = ed25519Signer.generateSignature();

        final Ed25519PublicKeyParameters publicKeyParameters = new Ed25519PublicKeyParameters(publicBytes, 0);
        final Signer verifier = new Ed25519Signer();
        verifier.init(false, publicKeyParameters);
        verifier.update(message, 0, message.length);
        final boolean verified = verifier.verifySignature(signature);
        assertThat(verified, is(true));

        //  assertThat(signingService.verifySignature(publicKeyUrl.getFile(), toSign, signed), is(true));


    }



    @Test
    void testSignWithPrivateKeyPemFile() {

        // Copy of a deploy hash
        final byte[] toSign = {
                (byte) 153, (byte) 144, (byte) 19, (byte) 83, (byte) 219, (byte) 161, (byte) 143, (byte) 137, (byte) 59,
                (byte) 67, (byte) 187, (byte) 238, (byte) 65, (byte) 111, (byte) 80, (byte) 243, (byte) 142, (byte) 77,
                (byte) 113, (byte) 46, (byte) 2, (byte) 166, (byte) 121, (byte) 118, (byte) 34, (byte) 205, (byte) 123,
                (byte) 14, (byte) 215, (byte) 85, (byte) 234, (byte) 161
        };

        final URL privateKeyUrl = SigningServiceTest.class.getResource(PRIVATE_KEY);
        byte[] signed = signingService.signWithPath(privateKeyUrl.getFile(), toSign);

        assertThat(signed, is(notNullValue()));
        assertThat(signed.length, is(64));

        // Verify signature
        final URL publicKeyUrl = SigningServiceTest.class.getResource(PUBLIC_KEY);
        assertThat(signingService.verifySignature(publicKeyUrl.getFile(), toSign, signed), is(true));
    }

    @Test
    void generateEdDSAKey() {
        final PrivateKey privateKey = signingService.generateEdDSAKey();
        assertThat(privateKey, is(notNullValue()));
        assertThat(privateKey.getAlgorithm(), is("Ed25519"));
    }

}