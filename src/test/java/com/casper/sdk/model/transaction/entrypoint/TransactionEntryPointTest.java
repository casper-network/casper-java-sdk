package com.casper.sdk.model.transaction.entrypoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author ian@meywood.com
 */
class TransactionEntryPointTest {

    @Test
    void customEntryPoint() throws JsonProcessingException {

        String json = "{\"Custom\":\"Transfer\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        TransactionEntryPoint entryPoint = objectMapper.readValue(json, TransactionEntryPoint.class);
        assertNotNull(entryPoint);
        assertInstanceOf(CustomEntryPoint.class, entryPoint);
        assertThat("Custom", is(entryPoint.getName()));
        assertThat("Transfer", is(((CustomEntryPoint) entryPoint).getCustom()));

        String written = objectMapper.writeValueAsString(entryPoint);
        assertThat(written, is(json));

    }
}
