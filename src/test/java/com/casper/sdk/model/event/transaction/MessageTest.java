package com.casper.sdk.model.event.transaction;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for {@link Message}
 *
 * @author ian@meywood.com
 */
class MessageTest {

    @Test
    void messageJson() throws IOException {

        Message message = new ObjectMapper().readValue(getClass().getResource("/sse/message.json"), Message.class);
        assertThat(message.getEntityAddr(), is("1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1"));
        assertThat(message.getMessage().getMessage(), is("The quick brown fox jumps over the lazy dog"));
        assertThat(message.getTopicName(), is("irDXU1I3k7xqsJpmLzrssIwZIBexhtnokJxA7yggsKR8w5vUNi0hNaCPvgEMcImvDfTv0FGovtrTiu9iCdNx7Ql"));
        assertThat(message.getTopicNameHash(), is("1a2b3c4d5e6f7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3e4f5a6b7c8d9e0f1"));
        assertThat(message.getTopicIndex(), is(12345));
        assertThat(message.getBlockIndex(), is(987654321L));

    }
}
