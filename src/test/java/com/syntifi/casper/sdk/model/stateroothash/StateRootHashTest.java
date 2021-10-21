package com.syntifi.casper.sdk.model.stateroothash;

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
 * Unit tests for {@link StateRootHashData}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class StateRootHashTest extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusDataTests.class);

    @Test
    void validateStateRootHashMapping() throws JsonMappingException, JsonProcessingException, IOException, JSONException {
        // curl -X POST -H 'Content-Type: application/json' -d
        // '{"id":"1132050564","jsonrpc":"2.0","method":"chain_get_state_root_hash","params":{"block_identifier":{"Height":0}}}'
        // http://nodeIP:7777/rpc
        String inputJson = getPrettyJson(loadJsonFromFile("block-samples/state-root-hash-block0.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        StateRootHashData root = OBJECT_MAPPER.readValue(inputJson, StateRootHashData.class);

        String expectedJson = getPrettyJson(root);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, false);
    }
}