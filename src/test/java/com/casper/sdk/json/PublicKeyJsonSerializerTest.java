package com.casper.sdk.json;

import com.casper.sdk.domain.KeyAlgorithm;
import com.casper.sdk.domain.PublicKey;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.casper.sdk.json.JsonTestUtils.writeToJsonString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class PublicKeyJsonSerializerTest {

    /**
     * Tests that a public key can be serialized to JSON and is prefixed with it's algorithm type
     */
    @Test
    void serialize() throws IOException {

        final PublicKey secKey = new PublicKey("12345678901234567890123456789012", KeyAlgorithm.SECP256K1);
        String json = writeToJsonString(secKey);
        assertThat(json, is("\"0212345678901234567890123456789012\""));

        final PublicKey edKey = new PublicKey("12345678901234567890123456789012", KeyAlgorithm.ED25519);
        json = writeToJsonString(edKey);
        assertThat(json, is("\"0112345678901234567890123456789012\""));
    }
}