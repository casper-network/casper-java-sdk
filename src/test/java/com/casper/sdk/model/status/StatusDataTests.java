package com.casper.sdk.model.status;

import com.casper.sdk.model.AbstractJsonTests;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    void validateStatusMapping() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("status-samples/status-info.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final StatusData st = OBJECT_MAPPER.readValue(inputJson, StatusData.class);

        assertNotNull(st.getLastAddedBlockInfo());

        final String expectedJson = getPrettyJson(st);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }
}