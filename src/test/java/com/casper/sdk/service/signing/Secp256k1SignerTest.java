package com.casper.sdk.service.signing;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

class Secp256k1SignerTest {

    private static final String SECRET_KEY = "/SECP256K1/key1_secret_key.pem";
    private static final String PUBLIC_KEY = "/SECP256K1/key1_public_key.pem";

    private final SigningService signingService = new SigningService();


    @Test
    void generateKeyPair() {

        final byte[] message = {
                (byte) 153, (byte) 144, (byte) 19, (byte) 83, (byte) 219, (byte) 161, (byte) 143, (byte) 137, (byte) 59,
                (byte) 67, (byte) 187, (byte) 238, (byte) 65, (byte) 111, (byte) 80, (byte) 243, (byte) 142, (byte) 77,
                (byte) 113, (byte) 46, (byte) 2, (byte) 166, (byte) 121, (byte) 118, (byte) 34, (byte) 205, (byte) 123,
                (byte) 14, (byte) 215, (byte) 85, (byte) 234, (byte) 161
        };

        final AsymmetricCipherKeyPair keyPair = signingService.generateKeyPair(SignatureAlgorithm.SECP256K1);

        assertThat(keyPair, is(notNullValue()));
        assertThat(keyPair.getPublic(), is(notNullValue()));
        assertThat(keyPair.getPrivate(), is(notNullValue()));

        final byte[] signedMessage = signingService.signWithPrivateKey(keyPair.getPrivate(), message, SignatureAlgorithm.SECP256K1);

        assertThat(signingService.verifySignature(keyPair.getPublic(), message, signedMessage, SignatureAlgorithm.SECP256K1), is(true));
    }


    @Test
    @SuppressWarnings("ConstantConditions")
    void loadKeyPair() throws Exception {

        final AsymmetricCipherKeyPair keyPair = signingService.loadKeyPair(
                Secp256k1SignerTest.class.getResource(PUBLIC_KEY).openStream(),
                Secp256k1SignerTest.class.getResource(SECRET_KEY).openStream(),
                SignatureAlgorithm.SECP256K1
        );

        assertThat(keyPair, is(notNullValue()));
        assertThat(keyPair.getPublic(), is(notNullValue()));
        assertThat(keyPair.getPrivate(), is(notNullValue()));
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    void signWithPrivateKeyAndVerifySignature() throws Exception {

        // Copy of a deploy hash
        final byte[] message = {
                (byte) 153, (byte) 144, (byte) 19, (byte) 83, (byte) 219, (byte) 161, (byte) 143, (byte) 137, (byte) 59,
                (byte) 67, (byte) 187, (byte) 238, (byte) 65, (byte) 111, (byte) 80, (byte) 243, (byte) 142, (byte) 77,
                (byte) 113, (byte) 46, (byte) 2, (byte) 166, (byte) 121, (byte) 118, (byte) 34, (byte) 205, (byte) 123,
                (byte) 14, (byte) 215, (byte) 85, (byte) 234, (byte) 161
        };

        final AsymmetricCipherKeyPair keyPair = signingService.loadKeyPair(
                Secp256k1SignerTest.class.getResource(PUBLIC_KEY).openStream(),
                Secp256k1SignerTest.class.getResource(SECRET_KEY).openStream(),
                SignatureAlgorithm.SECP256K1
        );

        final byte[] signedMessage = signingService.signWithPrivateKey(keyPair.getPrivate(), message, SignatureAlgorithm.SECP256K1);

        assertThat(signingService.verifySignature(keyPair.getPublic(), message, signedMessage, SignatureAlgorithm.SECP256K1), is(true));
    }
}