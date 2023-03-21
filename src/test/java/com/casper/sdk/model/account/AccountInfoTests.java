package com.casper.sdk.model.account;

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
 * Unit tests for {@link AccountData}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class AccountInfoTests extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountInfoTests.class);

    @Test
    void validateAccountInfoMapping() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("account-info-samples/account-info.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final AccountData ad = OBJECT_MAPPER.readValue(inputJson, AccountData.class);

        assertNotNull(ad.getAccount());

        final String expectedJson = getPrettyJson(ad);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }
}