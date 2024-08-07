package com.casper.sdk.model.balance;

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
 * Unit tests for {@link QueryBalanceData}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
public class JsonQueryBalanceDataTest extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonQueryBalanceDataTest.class);

    @Test
    void validateJsonQueryBalance() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("balance/query_balance.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final QueryBalanceData balanceData = OBJECT_MAPPER.readValue(inputJson, QueryBalanceData.class);

        assertNotNull(balanceData.getBalance());

        final String expectedJson = getPrettyJson(balanceData);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }
}
