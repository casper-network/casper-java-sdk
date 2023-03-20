package com.casper.sdk.model.validator;

import com.casper.sdk.model.AbstractJsonTests;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for {@link ValidatorChangeData}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
public class ValidatorChangesTests extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorChangesTests.class);

    @Test
    void validateDictionaryMapping() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("validator-change-samples/changes.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final ValidatorChangeData change = OBJECT_MAPPER.readValue(inputJson, ValidatorChangeData.class);

        assertTrue(change.getChanges().size() > 0);

        final String expectedJson = getPrettyJson(change);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, false);
    }
}