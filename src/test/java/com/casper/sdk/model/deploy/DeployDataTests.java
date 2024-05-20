package com.casper.sdk.model.deploy;

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
 * Unit tests for {@link DeployData}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class DeployDataTests extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeployDataTests.class);

    @Test
    void validateDeployDataMapping_1() throws IOException, JSONException {
        // curl -X POST -H 'Content-Type: application/json' -d
        // '{"jsonrpc":"2.0","id":"1","method":"info_get_deploy",
        // "params":{"deploy_hash":"614030ac705ed2067fed57d30545b3a4974ffc40a1c32f72e3b7b7442d6c83a3"}
        // }' http://nodeIP:7777/rpc
        String inputJson = getPrettyJson(loadJsonFromFile("deploy-samples/deploy-v1.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        DeployData dd = OBJECT_MAPPER.readValue(inputJson, DeployData.class);

        assertNotNull(dd.getDeploy());
        assertNotNull(dd.getExecutionResults().get(0));

        String expectedJson = getPrettyJson(dd);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void validateDeployDataMapping_2() throws IOException, JSONException {
        String inputJson = getPrettyJson(loadJsonFromFile("deploy-samples/deploy-v2.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        DeployData dd = OBJECT_MAPPER.readValue(inputJson, DeployData.class);

        assertNotNull(dd.getDeploy());

        String expectedJson = getPrettyJson(dd);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void validateDeployDataMapping_3() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("deploy-samples/deploy-v3.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final DeployData dd = OBJECT_MAPPER.readValue(inputJson, DeployData.class);

        assertNotNull(dd.getDeploy());

        final String expectedJson = getPrettyJson(dd);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void validateDeployDataMapping_4() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("deploy-samples/deploy-v4.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final DeployData dd = OBJECT_MAPPER.readValue(inputJson, DeployData.class);

        assertNotNull(dd.getDeploy());

        final String expectedJson = getPrettyJson(dd);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void validateDeployDataMapping_5() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("deploy-samples/deploy-v5.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final DeployData dd = OBJECT_MAPPER.readValue(inputJson, DeployData.class);

        assertNotNull(dd.getDeploy());

        final String expectedJson = getPrettyJson(dd);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void validateDeployDataMapping_6() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("deploy-samples/deploy-v6.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final DeployData dd = OBJECT_MAPPER.readValue(inputJson, DeployData.class);


        assertNotNull(dd.getDeploy());

//        CLValueByteArray b = new CLValueByteArray('3f6f2120bf33f221afc10c1257869f8019384949e1be85b8c4b5ef75153e779c')


        final String expectedJson = getPrettyJson(dd);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void validateDeployDataMapping_7() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("deploy-samples/deploy-v7.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final Deploy dd = OBJECT_MAPPER.readValue(inputJson, Deploy.class);

        assertNotNull(dd);

        final String expectedJson = getPrettyJson(dd);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }

    @Test
    void validateDeployResultMapping() throws IOException, JSONException {
        final String inputJson = getPrettyJson(loadJsonFromFile("deploy-samples/deploy-result.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        final DeployResult deploy = OBJECT_MAPPER.readValue(inputJson, DeployResult.class);

        final String expectedJson = getPrettyJson(deploy);

        LOGGER.debug("Serialized JSON: {}", expectedJson);

        JSONAssert.assertEquals(inputJson, expectedJson, JSONCompareMode.NON_EXTENSIBLE);
    }
}
