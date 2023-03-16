package com.casper.sdk.model.balance;

import com.casper.sdk.model.AbstractJsonTests;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link GetBalanceData}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
public class JsonGetBalanceDataTest extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonGetBalanceDataTest.class);

    @Test
    void validateJsonGetBalance() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("balance/balance.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final GetBalanceData getBalanceData = OBJECT_MAPPER.readValue(inputJson, GetBalanceData.class);

        assertNotNull(getBalanceData.getValue());

        final String expectedJson = getPrettyJson(getBalanceData);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, false);
    }
}
