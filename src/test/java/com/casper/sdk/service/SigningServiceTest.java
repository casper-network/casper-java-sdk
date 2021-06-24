package com.casper.sdk.service;

import org.junit.jupiter.api.Test;

import java.net.URL;
import java.security.PrivateKey;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

class SigningServiceTest {

    private static final String PRIVATE_KEY = "/account/user-1/secret_key.pem";
    private static final String PUBLIC_KEY = "/account/user-1/public_key.pem";

    private final SigningService signingService = new SigningService();


    @Test
    void testSignWithPrivateKeyPemFile() {

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
        final URL publicKeyUrl = SigningServiceTest.class.getResource(PRIVATE_KEY);
        assertThat(signingService.verifySignature(publicKeyUrl.getFile(), toSign, signed), is(true));
    }

    @Test
    void generateEdDSAKey() {
        final PrivateKey privateKey = signingService.generateEdDSAKey();
        assertThat(privateKey, is(notNullValue()));
        assertThat(privateKey.getAlgorithm(), is("Ed25519"));
    }

    @Test
    void signWithPrivateKey() {

        final byte[] privateKeyBytes = {
                (byte) 239, (byte) 239, (byte) 126, (byte) 80, (byte) 139, (byte) 163, (byte) 76, (byte) 205, (byte) 21,
                (byte) 144, (byte) 177, (byte) 126, (byte) 19, (byte) 67, (byte) 59, (byte) 27, (byte) 139, (byte) 186,
                (byte) 172, (byte) 191, (byte) 195, (byte) 81, (byte) 196, (byte) 111, (byte) 213, (byte) 199,
                (byte) 93, (byte) 37, (byte) 50, (byte) 228, (byte) 225, (byte) 199, (byte) 22, (byte) 117, (byte) 181,
                (byte) 87, (byte) 53, (byte) 175, (byte) 221, (byte) 34, (byte) 18, (byte) 147, (byte) 236, (byte) 64,
                (byte) 57, (byte) 231, (byte) 187, (byte) 253, (byte) 206, (byte) 131, (byte) 79, (byte) 31, (byte) 131,
                (byte) 134, (byte) 220, (byte) 43, (byte) 235, (byte) 197, (byte) 108, (byte) 77, (byte) 35, (byte) 110,
                (byte) 71, (byte) 76
        };

        // Copy of a deploy hash
        final byte[] toSign = {
                (byte) 153, (byte) 144, (byte) 19, (byte) 83, (byte) 219, (byte) 161, (byte) 143, (byte) 137, (byte) 59,
                (byte) 67, (byte) 187, (byte) 238, (byte) 65, (byte) 111, (byte) 80, (byte) 243, (byte) 142, (byte) 77,
                (byte) 113, (byte) 46, (byte) 2, (byte) 166, (byte) 121, (byte) 118, (byte) 34, (byte) 205, (byte) 123,
                (byte) 14, (byte) 215, (byte) 85, (byte) 234, (byte) 161
        };

        final byte[] signed = signingService.signWithPrivateKey(privateKeyBytes, toSign);
        assertThat(signed, is(notNullValue()));
        assertThat(signed.length, is(64));
    }


}