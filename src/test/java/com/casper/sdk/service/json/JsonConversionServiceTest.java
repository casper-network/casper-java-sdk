package com.casper.sdk.service.json;

import com.casper.sdk.types.Deploy;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tess for the JsonConversionService
 */
class JsonConversionServiceTest {

    /** The path to the original JSON the model is loaded from */
    private static final String DEPLOY_TRANSFER_JSON = "/com/casper/sdk/service/json/deploy-transfer.json";

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
}