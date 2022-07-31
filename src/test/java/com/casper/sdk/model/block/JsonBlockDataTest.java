package com.casper.sdk.model.block;

import com.casper.sdk.model.status.StatusDataTests;
import com.casper.sdk.model.AbstractJsonTests;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link JsonBlockDataTest}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class JsonBlockDataTest extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusDataTests.class);

    @Test
    void validateJsonBlock_EraEndBlock() throws IOException, JSONException {
        // curl -X POST -H 'Content-Type: application/json' -d
        // '{"id":"0","jsonrpc":"2.0","method":"chain_get_block",
        // "params":{"block_identifier":{"Height":"246762"}}}' http://nodeIP:7777/rpc
        final String inputJson = getPrettyJson(loadJsonFromFile("block-samples/block-end-era.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final JsonBlockData block = OBJECT_MAPPER.readValue(inputJson, JsonBlockData.class);

        assertNotNull(block.getBlock());

        final String expectedJson = getPrettyJson(block);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, false);
    }
}