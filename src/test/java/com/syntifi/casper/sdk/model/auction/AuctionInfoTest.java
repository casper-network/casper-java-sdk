package com.syntifi.casper.sdk.model.auction;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
 * Unit tests for {@link AuctionData}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class AuctionInfoTest extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusDataTests.class);

    @Test
    void validateAuctionInfoMapping() throws JsonMappingException, JsonProcessingException, IOException, JSONException {
        String inputJson = getPrettyJson(loadJsonFromFile("auction-info-samples/auction-info.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        AuctionData ad = OBJECT_MAPPER.readValue(inputJson, AuctionData.class);

        assertTrue(ad.getAuctionState() instanceof AuctionState);

        String expectedJson = getPrettyJson(ad);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, false);
    }
}