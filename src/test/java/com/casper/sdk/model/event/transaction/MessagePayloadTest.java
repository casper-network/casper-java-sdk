package com.casper.sdk.model.event.transaction;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for {@link MessagePayload}
 * @author ian@meywood.com
 */
class MessagePayloadTest {

    @Test
    void stringMessage() throws JsonProcessingException {
        final String json = "{" +
                "  \"String\": \"The quick brown fox jumps over the lazy dog\"\n" +
                "}";

        final MessagePayload messagePayload = new ObjectMapper().readValue(json, MessagePayload.class);
        assertThat(messagePayload, is(instanceOf(StringMessagePayload.class)));
        assertThat(messagePayload.getMessage(), is("The quick brown fox jumps over the lazy dog"));

    }

    @Test
    void bytesMessage() throws JsonProcessingException {
        final String json = "{\n" +
                "  \"Bytes\": \"bb2bbb3681a0c6d3800e8a0b6a8fb12905c53039b80d30889de32d46607a1e2a7f295d13\"\n" +
                "}";
        final MessagePayload messagePayload = new ObjectMapper().readValue(json, MessagePayload.class);
        assertThat(messagePayload, is(instanceOf(BytesMessagePayload.class)));
        assertThat(
                messagePayload.getMessage(),
                is(Hex.decode("bb2bbb3681a0c6d3800e8a0b6a8fb12905c53039b80d30889de32d46607a1e2a7f295d13"))
        );
    }
}
