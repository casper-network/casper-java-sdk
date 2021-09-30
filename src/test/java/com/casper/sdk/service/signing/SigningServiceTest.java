package com.casper.sdk.service.signing;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.Signer;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

class SigningServiceTest {

    private static final String PRIVATE_KEY = "/account/user-1/secret_key.pem";
    private static final String PUBLIC_KEY = "/account/user-1/public_key.pem";

    private final SigningService signingService = new SigningService();

    @Test
    void testLoadKeyPair() {

        // Copy of a deploy hash
        final byte[] message = {
                (byte) 153, (byte) 144, (byte) 19, (byte) 83, (byte) 219, (byte) 161, (byte) 143, (byte) 137, (byte) 59,
                (byte) 67, (byte) 187, (byte) 238, (byte) 65, (byte) 111, (byte) 80, (byte) 243, (byte) 142, (byte) 77,
                (byte) 113, (byte) 46, (byte) 2, (byte) 166, (byte) 121, (byte) 118, (byte) 34, (byte) 205, (byte) 123,
                (byte) 14, (byte) 215, (byte) 85, (byte) 234, (byte) 161
        };

        // Load AsymmetricCipherKeyPair from .pem files
        //noinspection ConstantConditions
        final AsymmetricCipherKeyPair asymmetricCipherKeyPair = signingService.loadKeyPair(
                new File(SigningServiceTest.class.getResource(PUBLIC_KEY).getFile()),
                new File(SigningServiceTest.class.getResource(PRIVATE_KEY).getFile()),
                SignatureAlgorithm.ED25519
        );

        // Sign the message using the private key
        final byte[] signedBytes = signingService.signWithPrivateKey(
                asymmetricCipherKeyPair.getPrivate(),
                message,
                SignatureAlgorithm.ED25519
        );

        // Assert the message was signed
        assertThat(signedBytes.length, is(64));

        // Verify the signature
        assertThat(signingService.verifySignature(asymmetricCipherKeyPair.getPublic(), message, signedBytes, SignatureAlgorithm.ED25519), is(true));
    }


    @Test
    void generateEdDSAKey() throws Exception {

        final AsymmetricCipherKeyPair keyPair = signingService.generateKeyPair(SignatureAlgorithm.ED25519);
        assertThat(keyPair.getPublic(), is(notNullValue()));
        assertThat(keyPair.getPrivate(), is(notNullValue()));

        // the message
        byte[] message = "Message to sign".getBytes(StandardCharsets.UTF_8);
        // create the signature
        Signer signer = new Ed25519Signer();
        signer.init(true, keyPair.getPrivate());
        signer.update(message, 0, message.length);
        byte[] signature = signer.generateSignature();
        // verify the signature
        Signer verifier = new Ed25519Signer();
        verifier.init(false, keyPair.getPublic());
        verifier.update(message, 0, message.length);
        assertThat(verifier.verifySignature(signature), is(true));
    }
}