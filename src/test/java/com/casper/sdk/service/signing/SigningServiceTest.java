package com.casper.sdk.service.signing;

import com.casper.sdk.exceptions.SignatureException;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.Algorithm;
import com.casper.sdk.types.CLPublicKey;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPublicKey;
import org.bouncycastle.jcajce.provider.asymmetric.edec.BCEdDSAPublicKey;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.fail;

class SigningServiceTest {

    private static final String ED25519K_PRIVATE_KEY = "/account/user-1/secret_key.pem";
    private static final String ED25519K_PUBLIC_KEY = "/account/user-1/public_key.pem";
    private static final String SECP256K1_SECRET_KEY = "/SECP256K1/key1_secret_key.pem";
    private static final String SECP256K1_PUBLIC_KEY = "/SECP256K1/key1_public_key.pem";

    private final SigningService signingService = new SigningService();

    @Test
    void loadEd25519KeyPair() {

        // Copy of a deploy hash
        final byte[] message = {
                (byte) 153, (byte) 144, (byte) 19, (byte) 83, (byte) 219, (byte) 161, (byte) 143, (byte) 137, (byte) 59,
                (byte) 67, (byte) 187, (byte) 238, (byte) 65, (byte) 111, (byte) 80, (byte) 243, (byte) 142, (byte) 77,
                (byte) 113, (byte) 46, (byte) 2, (byte) 166, (byte) 121, (byte) 118, (byte) 34, (byte) 205, (byte) 123,
                (byte) 14, (byte) 215, (byte) 85, (byte) 234, (byte) 161
        };

        // Load AsymmetricCipherKeyPair from .pem files
        //noinspection ConstantConditions
        final KeyPair keyPair = signingService.loadKeyPair(
                new File(SigningServiceTest.class.getResource(ED25519K_PUBLIC_KEY).getFile()),
                new File(SigningServiceTest.class.getResource(ED25519K_PRIVATE_KEY).getFile())
        );

        // Sign the message using the private key
        final byte[] signedBytes = signingService.signWithPrivateKey(
                keyPair.getPrivate(),
                message
        );

        // Assert the message was signed
        assertThat(signedBytes.length, is(64));

        // Expect the same bytes for all incarnations
        final byte[] deterministic = new byte[]{
                40, 26, 91, -28, -20, 113, -70, -17, 105, -30, 12, 76, 39, -64, 75, 95, -21, -124, -61, -50, 74, -78,
                100, -77, 90, -67, -55, 37, 10, 25, 119, -49, -54, -82, 59, 99, -33, -95, 75, 82, -4, -39, -31, 9, -34,
                69, 18, -54, -36, -9, -53, -69, 54, 14, 117, -43, 35, -97, -101, 105, 100, 125, -10, 5
        };
        assertThat(signedBytes, is(deterministic));

        // Verify the signature
        assertThat(signingService.verifySignature(keyPair.getPublic(), message, signedBytes), is(true));

        final String expectedRaw = "01d30f6a241199e68217cb05abcefc7c8267c5226b8e644f1f8d0a79b87ed04f07";
        final CLPublicKey publicKey = signingService.toClPublicKey(keyPair.getPublic());
        assertThat(publicKey.toAccountHex(), is(expectedRaw));
    }

    @Test
    void generateEd25519KeyPair() {

        final KeyPair keyPair = signingService.generateKeyPair(Algorithm.ED25519);
        assertThat(keyPair.getPublic(), is(notNullValue()));
        assertThat(keyPair.getPrivate(), is(notNullValue()));

        // the message
        byte[] message = "Message to sign".getBytes(StandardCharsets.UTF_8);

        byte[] signature = signingService.signWithPrivateKey(keyPair.getPrivate(), message);

        assertThat(signingService.verifySignature(keyPair.getPublic(), message, signature), is(true));
    }

    @Test
    void generateEd25519KeyPairFromSeed() {

        final SecureRandom random = new SecureRandom();
        byte[] seed = new byte[20];
        random.nextBytes(seed);

        final KeyPair keyPair = signingService.generateKeyPair(Algorithm.ED25519, seed);
        assertThat(keyPair.getPublic(), is(notNullValue()));
        assertThat(keyPair.getPrivate(), is(notNullValue()));

        // the message
        byte[] message = "Message to sign".getBytes(StandardCharsets.UTF_8);

        byte[] signature = signingService.signWithPrivateKey(keyPair.getPrivate(), message);

        assertThat(signingService.verifySignature(keyPair.getPublic(), message, signature), is(true));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void loadSecp256k1KeyPair() throws Exception {

        final KeyPair keyPair = signingService.loadKeyPair(
                SigningServiceTest.class.getResource(SECP256K1_PUBLIC_KEY).openStream(),
                SigningServiceTest.class.getResource(SECP256K1_SECRET_KEY).openStream()
        );

        assertThat(keyPair, is(notNullValue()));
        assertThat(keyPair.getPublic(), is(notNullValue()));
        assertThat(keyPair.getPrivate(), is(notNullValue()));
    }

    @Test
    void generateSecp256k1KeyPair() {

        final byte[] message = {
                (byte) 153, (byte) 144, (byte) 19, (byte) 83, (byte) 219, (byte) 161, (byte) 143, (byte) 137, (byte) 59,
                (byte) 67, (byte) 187, (byte) 238, (byte) 65, (byte) 111, (byte) 80, (byte) 243, (byte) 142, (byte) 77,
                (byte) 113, (byte) 46, (byte) 2, (byte) 166, (byte) 121, (byte) 118, (byte) 34, (byte) 205, (byte) 123,
                (byte) 14, (byte) 215, (byte) 85, (byte) 234, (byte) 161
        };

        try {
            final KeyPair keyPair = signingService.generateKeyPair(Algorithm.SECP256K1);

            fail("Should have thrown SignatureException");

            assertThat(keyPair, is(notNullValue()));
            assertThat(keyPair.getPublic(), is(notNullValue()));
            assertThat(keyPair.getPrivate(), is(notNullValue()));

            final byte[] signedMessage = signingService.signWithPrivateKey(keyPair.getPrivate(), message);

            assertThat(signingService.verifySignature(keyPair.getPublic(), message, signedMessage), is(true));
        } catch (SignatureException e) {
            assertThat(e.getMessage(), containsString("secp256k1 KeyPair generation is not yet supported"));
        }
    }


    @Test
    void generateSecp256k1KeyPairFromSeed() {

        final SecureRandom random = new SecureRandom();
        byte[] seed = new byte[20];
        random.nextBytes(seed);

        final byte[] message = {
                (byte) 153, (byte) 144, (byte) 19, (byte) 83, (byte) 219, (byte) 161, (byte) 143, (byte) 137, (byte) 59,
                (byte) 67, (byte) 187, (byte) 238, (byte) 65, (byte) 111, (byte) 80, (byte) 243, (byte) 142, (byte) 77,
                (byte) 113, (byte) 46, (byte) 2, (byte) 166, (byte) 121, (byte) 118, (byte) 34, (byte) 205, (byte) 123,
                (byte) 14, (byte) 215, (byte) 85, (byte) 234, (byte) 161
        };

        try {
            final KeyPair keyPair = signingService.generateKeyPair(Algorithm.SECP256K1, seed);

            fail("Should have thrown SignatureException");

            assertThat(keyPair, is(notNullValue()));
            assertThat(keyPair.getPublic(), is(notNullValue()));
            assertThat(keyPair.getPrivate(), is(notNullValue()));

            final byte[] signedMessage = signingService.signWithPrivateKey(keyPair.getPrivate(), message);

            assertThat(signingService.verifySignature(keyPair.getPublic(), message, signedMessage), is(true));
        } catch (SignatureException e) {
            assertThat(e.getMessage(), containsString("secp256k1 KeyPair generation is not yet supported"));
        }
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void signWithSecp256k1PrivateKeyAndVerifySignature() throws Exception {

        final byte[] message = "The quick brown fox jumped over the lazy dog".getBytes(StandardCharsets.UTF_8);

        final byte[] expectedSigned = ByteUtils.decodeHex("898aca19a91a770078ca62dd14627305718a49c8fd2f59782cfe067a60ae3d7b2fa92d6ac504ce68c6d8b492325aaa32907c5eeab33a21262717d7ed1c5b932e");

        final KeyPair keyPair = signingService.loadKeyPair(
                SigningServiceTest.class.getResource(SECP256K1_PUBLIC_KEY).openStream(),
                SigningServiceTest.class.getResource(SECP256K1_SECRET_KEY).openStream()
        );

        final byte[] signedMessage = signingService.signWithPrivateKey(keyPair.getPrivate(), message);

        assertThat(signedMessage, is(expectedSigned));

        assertThat(signingService.verifySignature(keyPair.getPublic(), message, signedMessage), is(true));

        final String expectedRaw = "02035793d9a677ec9cf0d3d2a7a61fb98c173c04b63925cfe387203b19d312fa37b0";
        final CLPublicKey publicKey = signingService.toClPublicKey(keyPair.getPublic());
        assertThat(publicKey.getBytes().length, is(33));
        assertThat(publicKey.toAccountHex(), is(expectedRaw));
    }


    @Test
    void saveSecp256k1PrivatePem() throws IOException {

        final KeyPair keyPair = signingService.loadKeyPair(
                SigningServiceTest.class.getResourceAsStream(SECP256K1_PUBLIC_KEY),
                SigningServiceTest.class.getResourceAsStream(SECP256K1_SECRET_KEY)
        );

        final ByteArrayOutputStream privateOut = new ByteArrayOutputStream();
        signingService.writeKey(privateOut, keyPair.getPrivate());

        // Load the key back and ensure it is valid
        final byte[] pemBytes = privateOut.toByteArray();
        //noinspection ConstantConditions
        final String expectedPrivate = IOUtils.toString(SigningServiceTest.class.getResourceAsStream(SECP256K1_SECRET_KEY), StandardCharsets.UTF_8);
        assertThat(new String(pemBytes, StandardCharsets.UTF_8), is(expectedPrivate));

        final PrivateKey privateKey = signingService.toPrivateKey(new ByteArrayInputStream(pemBytes));
        assertThat(privateKey.getEncoded(), is(keyPair.getPrivate().getEncoded()));
        assertThat(privateKey.getAlgorithm(), is(keyPair.getPrivate().getAlgorithm()));
        assertThat(privateKey.getFormat(), is(keyPair.getPrivate().getFormat()));

        final byte[] rawBytes = ByteUtils.decodeHex("02035793d9a677ec9cf0d3d2a7a61fb98c173c04b63925cfe387203b19d312fa37b0");
        final CLPublicKey publicKey = signingService.toClPublicKey(keyPair.getPublic());
        assertThat(publicKey.getAlgorithm(), is(Algorithm.SECP256K1));
        assertThat(publicKey.toAccount(), is(rawBytes));
    }

    @Test
    void saveED25519PublicPem() {

        final KeyPair keyPair = signingService.loadKeyPair(
                SigningServiceTest.class.getResourceAsStream(ED25519K_PUBLIC_KEY),
                SigningServiceTest.class.getResourceAsStream(ED25519K_PRIVATE_KEY)
        );

        final ByteArrayOutputStream publicOut = new ByteArrayOutputStream();
        signingService.writeKey(publicOut, keyPair.getPublic());

        // Load the key back and ensure it is valid
        byte[] pemBytes = publicOut.toByteArray();

        final java.security.PublicKey publicKey = signingService.toPublicKey(new ByteArrayInputStream(pemBytes));
        assertThat(publicKey.getEncoded(), is(keyPair.getPublic().getEncoded()));
        assertThat(publicKey.getAlgorithm(), is(keyPair.getPublic().getAlgorithm()));
        assertThat(publicKey.getFormat(), is(keyPair.getPublic().getFormat()));

        final String rawBytes = "01d30f6a241199e68217cb05abcefc7c8267c5226b8e644f1f8d0a79b87ed04f07";
        final CLPublicKey clPublicKey = new CLPublicKey(rawBytes);
        assertThat(clPublicKey.getAlgorithm(), is(Algorithm.ED25519));
        assertThat(clPublicKey.toAccountHex(), is(rawBytes));
    }

    @Test
    void eD25519k1FromClPublicKey() {

        final byte[] rawBytes = ByteUtils.decodeHex("01d30f6a241199e68217cb05abcefc7c8267c5226b8e644f1f8d0a79b87ed04f07");
        final CLPublicKey clPublicKey = new CLPublicKey(rawBytes);
        final java.security.PublicKey publicKey = signingService.fromClPublicKey(clPublicKey);

        assertThat(publicKey, is(instanceOf(BCEdDSAPublicKey.class)));
        assertThat(((BCEdDSAPublicKey) publicKey).getPointEncoding(), is(clPublicKey.getBytes()));
    }

    @Test
    void secp256k1FromClPublicKey() {

        final byte[] rawBytes = ByteUtils.decodeHex("02035793d9a677ec9cf0d3d2a7a61fb98c173c04b63925cfe387203b19d312fa37b0");
        final CLPublicKey clPublicKey = new CLPublicKey(rawBytes);
        final java.security.PublicKey publicKey = signingService.fromClPublicKey(clPublicKey);

        assertThat(publicKey, is(instanceOf(BCECPublicKey.class)));
        assertThat(signingService.toClPublicKey(publicKey).getBytes(), is(clPublicKey.getBytes()));
    }

    @Test
    void
    secp256k1SignAndVerifyFromFile() {

        final byte[] message = "The quick brown fox jumped over the lazy dog".getBytes(StandardCharsets.UTF_8);
        final byte[] expectedSigned = ByteUtils.decodeHex("898aca19a91a770078ca62dd14627305718a49c8fd2f59782cfe067a60ae3d7b2fa92d6ac504ce68c6d8b492325aaa32907c5eeab33a21262717d7ed1c5b932e");

        final PrivateKey privateKey = signingService.loadKey(getClass().getResourceAsStream(SECP256K1_SECRET_KEY));
        final byte[] signed = signingService.signWithPrivateKey(privateKey, message);
        assertThat(signed, is(expectedSigned));

        final PublicKey publicKey = signingService.loadKey(getClass().getResourceAsStream(SECP256K1_PUBLIC_KEY));
        assertThat(signingService.verifySignature(publicKey, message, signed), is(true));
    }
}