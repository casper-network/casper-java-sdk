package com.syntifi.casper.sdk.model.dictionary;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.syntifi.casper.sdk.model.AbstractJsonTests;
import com.syntifi.casper.sdk.model.status.StatusDataTests;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit tests for {@link DictionaryData}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class DictionaryDataTest extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusDataTests.class);

    @Test
    void validateDictionaryMapping() throws JsonMappingException, JsonProcessingException, IOException, JSONException {
        String inputJson = getPrettyJson(loadJsonFromFile("dictionary-samples/dictionary1.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        DictionaryData dict = OBJECT_MAPPER.readValue(inputJson, DictionaryData.class);

        String expectedJson = getPrettyJson(dict);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, false);
    }
}


