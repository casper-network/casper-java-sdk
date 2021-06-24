package com.casper.sdk.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import org.junit.jupiter.api.Test;
import com.casper.sdk.service.serialization.util.ByteUtils;

import java.net.URL;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

class SigningServiceTest {

    private static final String PRIVATE_KEY = "/account/user-1/secret_key.pem";
    private static final String PUBLIC_KEY = "/account/user-1/public_key.pem";

    private final SigningService signingService = new SigningService();


    @Test
    void loadKey() throws Exception {

        byte [] expectedKeyBytes = ByteUtils.decodeHex("01d30f6a241199e68217cb05abcefc7c8267c5226b8e644f1f8d0a79b87ed04f07");

        //byte[] keyBytes = signingService.loadKeyBytes(SigningServiceTest.class.getResource(PUBLIC_KEY).getFile());
//      RSAPublicKey keyBytes = signingService.readPublicKey(SigningServiceTest.class.getResource(PUBLIC_KEY).getFile());
        RSAPublicKey keyBytes = signingService.readPublicKeyBC(SigningServiceTest.class.getResource(PUBLIC_KEY).getFile());

        assertThat(keyBytes, is(expectedKeyBytes));
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