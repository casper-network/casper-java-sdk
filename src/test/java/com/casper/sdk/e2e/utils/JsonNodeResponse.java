package com.casper.sdk.e2e.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.function.Function;

/**
 * Default execute response function the converts the response string to a JsonNode.
 */
class JsonNodeResponse implements Function<String, JsonNode> {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public JsonNode apply(final String response) {
        try {
            return mapper.readTree(response);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
