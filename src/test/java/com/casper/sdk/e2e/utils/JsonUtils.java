package com.casper.sdk.e2e.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NumericNode;

/**
 * JSON Util Class
 *
 * @author ian@meywood.com
 */
public class JsonUtils {
    public static <T> T getJsonValue(final JsonNode jsonNode, final String jsonPath) {

        final JsonNode at = jsonNode.at(jsonPath);
        if (at instanceof NumericNode) {
            //noinspection unchecked
            return (T) at.bigIntegerValue();
        } else {
            //noinspection unchecked
            return (T) at.asText();
        }
    }
}
