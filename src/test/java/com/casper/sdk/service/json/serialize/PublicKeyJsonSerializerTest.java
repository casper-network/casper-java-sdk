package com.casper.sdk.service.json.serialize;

import com.casper.sdk.types.Algorithm;
import com.casper.sdk.types.CLPublicKey;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.casper.sdk.service.json.JsonTestUtils.writeToJsonString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class PublicKeyJsonSerializerTest {

    /**
     * Tests that a public key can be serialized to JSON and is prefixed with its algorithm type
     */
    @Test
    void serialize() throws IOException {

        final CLPublicKey secKey = new CLPublicKey("12345678901234567890123456789012", Algorithm.SECP256K1);
        String json = writeToJsonString(secKey);
        assertThat(json, is("\"0212345678901234567890123456789012\""));

        final CLPublicKey edKey = new CLPublicKey("12345678901234567890123456789012", Algorithm.ED25519);
        json = writeToJsonString(edKey);
        assertThat(json, is("\"0112345678901234567890123456789012\""));
    }
}