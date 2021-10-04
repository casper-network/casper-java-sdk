package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.SignatureAlgorithm;
import com.casper.sdk.types.CLPublicKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests for the PublicKeyJsonDeserializer
 */
class CLPublicKeyJsonDeserializerTest {

    private static final String JSON = /* \"signer\":*/ "\"017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537\"";

    /**
     * Tests that a PublicKey can be deserialized from JSON
     */
    @Test
    void deserializePublicKey() throws IOException {

        final ObjectMapper mapper = new ObjectMapper();
        final CLPublicKey publicKey = mapper.reader().readValue(JSON, CLPublicKey.class);
        assertThat(publicKey, is(notNullValue()));
        assertThat(
                publicKey.getBytes(),
                is(CLPublicKey.fromString("7f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"))
        );

        assertThat(publicKey.getKeyAlgorithm(), is(SignatureAlgorithm.ED25519));

        assertThat(publicKey.toAccountHex(), is("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"));
    }
}