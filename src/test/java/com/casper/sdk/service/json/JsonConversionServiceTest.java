package com.casper.sdk.service.json;

import com.casper.sdk.types.*;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tess for the JsonConversionService
 */
class JsonConversionServiceTest {

    /** The path to the original JSON the model is loaded from */
    private static final String DEPLOY_TRANSFER_JSON = "/com/casper/sdk/service/json/deploy-transfer.json";
    /** Deploy with a StoredContractByHash session containing CLMaps */
    private static final String STORED_CONTRACT_JSON = "/com/casper/sdk/types/cl-map-test.js";

    /** The service under test */
    private final JsonConversionService jsonConversionService = new JsonConversionService();

    /**
     * Tests the JsonConversionService can convert a JSON stream to Deploy and back to a JSON stream and that the
     * contents of both streams are identical ignoring minor whitespace difference
     */
    @Test
    void fromJsonStreamToJSonSteam() throws IOException {

        // Load the deploy from JSON
        final Deploy deploy = jsonConversionService.fromJson(
                getClass().getResourceAsStream(DEPLOY_TRANSFER_JSON),
                Deploy.class
        );

        // Convert it back to JSON
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        jsonConversionService.toJson(deploy, out);
        final String json = deleteWhitespace((out.toString()));

        final String originalJson = deleteWhitespace(IOUtils.toString(Objects.requireNonNull(getClass()
                .getResourceAsStream(DEPLOY_TRANSFER_JSON)), StandardCharsets.UTF_8));

        // Assert JSON written from CL Object matches original JSON ignoring whitespace
        assertThat(json, is(originalJson));
    }

    /**
     * Tests the JsonConversionService can convert a JSON string to Deploy and back to a JSON string and that the
     * contents of both string are identical ignoring minor whitespace difference
     */
    @Test
    void fromJsonStringToJsonString() throws IOException {

        // Load the deploy from JSON
        final String originalJson = IOUtils.toString(Objects.requireNonNull(getClass()
                .getResourceAsStream(DEPLOY_TRANSFER_JSON)), StandardCharsets.UTF_8);
        final Deploy deploy = jsonConversionService.fromJson(originalJson, Deploy.class);

        // Convert it back to JSON
        final String json = deleteWhitespace(jsonConversionService.toJson(deploy));

        // Assert JSON written from CL Object matches original JSON ignoring whitespace
        assertThat(json, is(deleteWhitespace(originalJson)));
    }

    /**
     * Tests that a session that is a StoredContractByHash containing CLMaps can be loaded from JSON
     */
    @Test
    void storedContractByHashFromJson() throws IOException {
        // Load the deploy from JSON
        final Deploy deploy = jsonConversionService.fromJson(
                getClass().getResourceAsStream(STORED_CONTRACT_JSON),
                Deploy.class
        );

        assertThat(deploy.getSession(), is(notNullValue(StoredContractByHash.class)));

        final StoredContractByHash session = deploy.getSession();
        final DeployNamedArg assetHolders = session.getNamedArg("asset_holders");
        assertThat(assetHolders.getValue().getCLType(), is(CLType.MAP));

        final CLMap map = (CLMap) assetHolders.getValue();
        assertThat(map.getKeyType().getType(), is(CLType.BYTE_ARRAY));
        assertThat(map.getValueType().getType(), is(CLType.U256));
        assertThat(map.size(), is(2));

        final CLValue key = map.keySet().iterator().next();
        assertThat(key.getParsed(), is("e07cA98F1b5C15bC9ce75e8adB8a3b4D334A1B1Fa14DD16CfD3320bf77Cc3aAb"));

        final CLValue value = map.values().iterator().next();
        assertThat(value.getParsed(), is("400000"));
    }
}