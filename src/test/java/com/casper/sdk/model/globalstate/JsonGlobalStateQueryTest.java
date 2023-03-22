package com.casper.sdk.model.globalstate;

import com.casper.sdk.model.AbstractJsonTests;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


/**
 * Unit tests for {@link GlobalStateData }
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
public class JsonGlobalStateQueryTest extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonGlobalStateQueryTest.class);

    @Test
    void validateJsonGlobalState() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("globalstate-samples/globalstate.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final GlobalStateData data = OBJECT_MAPPER.readValue(inputJson, GlobalStateData.class);

        final String expectedJson = getPrettyJson(data);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }
}
