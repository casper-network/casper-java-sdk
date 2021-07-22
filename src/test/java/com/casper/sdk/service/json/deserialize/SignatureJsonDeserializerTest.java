package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.Signature;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests for the SignatureJsonDeserializer
 */
class SignatureJsonDeserializerTest {

    private static final String JSON = /* signature: */ "\"0195a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06\"";

    /**
     * Test a {@link Signature} object can be parsed from JSON
     */
    @Test
    void deserializeSignature() throws IOException {

        final ObjectMapper mapper = new ObjectMapper();
        Signature signature = mapper.reader().readValue(JSON, Signature.class);
        assertThat(signature, is(notNullValue()));
        //noinspection SpellCheckingInspection
        assertThat(signature.getBytes(),
                is(ByteUtils.decodeHex("95a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06"))
        );
    }
}