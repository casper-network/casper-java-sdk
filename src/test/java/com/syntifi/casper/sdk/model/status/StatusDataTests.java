package com.syntifi.casper.sdk.model.status;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.syntifi.casper.sdk.model.AbstractJsonTests;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@link StatusData}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class StatusDataTests extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusDataTests.class);

    @Test
    void validateStatusMapping() throws JsonMappingException, JsonProcessingException, IOException, JSONException {
        String inputJson = getPrettyJson(loadJsonFromFile("status-samples/status-info.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StatusData st = OBJECT_MAPPER.readValue(inputJson, StatusData.class);

        assertTrue(st.getLastAddedBlockInfo() instanceof MinimalBlockInfo);

        String expectedJson = getPrettyJson(st);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, false);
    }
}