package com.casper.sdk.model.transfer;

import com.casper.sdk.model.AbstractJsonTests;
import com.casper.sdk.model.status.StatusDataTests;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Unit tests for {@link TransferData}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class TransferDataTest extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusDataTests.class);

    @Test
    void validateDictionaryMapping() throws IOException, JSONException {
        // curl -X POST -H 'Content-Type: application/json' -d
        // '{"id":"0","jsonrpc":"2.0","method":"chain_get_block_transfers","params":{"block_identifier":{"Height":198148}}}'
        // http://195.201.142.76:7777/rpc
        final String inputJson = getPrettyJson(loadJsonFromFile("transfer-samples/transfer.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final TransferData transfer = OBJECT_MAPPER.readValue(inputJson, TransferData.class);

        assertNotNull(transfer.getTransfers().get(0));

        final String expectedJson = getPrettyJson(transfer);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, false);
    }
}