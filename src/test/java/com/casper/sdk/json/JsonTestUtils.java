package com.casper.sdk.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Test utility class
 */
public class JsonTestUtils {

    /**
     * Utility method to convert an object to a JSON string using Jackson APIs
     *
     * @param value the value to convert to JSON
     * @return the JSON
     */
    public static String writeToJsonString(final Object value) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        new ObjectMapper().writer().writeValue(out, value);
        return IOUtils.toString(out.toByteArray(), StandardCharsets.UTF_8.name());
    }
}
