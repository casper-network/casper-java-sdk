package com.casper.sdk.service.json.serialize;

import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.CLValue;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.casper.sdk.service.json.JsonTestUtils.writeToJsonString;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Test that a cl_type of Key can be correctly serialized to JSON.
 */
public class CLKeyValueJsonSerializerTest {

    private static final String KEY_HEX = "0fe62e5a3afe9792da5c2154d0c295c108dab251f542273563d4d4bffd600945";

    /**
     * Tests that a CLKey Account value can be converted to JSON
     */
    @Test
    void clAccountKeyToJson() throws IOException {

        final byte[] key = ByteUtils.decodeHex(KEY_HEX);

        final CLValue clKeyValue = CLValueBuilder.accountKey(key);

        final String json = writeToJsonString(clKeyValue);

        assertThat(json, is(notNullValue()));

        assertThat(json, hasJsonPath("$.cl_type", is("Key")));
        assertThat(json, hasJsonPath("$.bytes", is("00" + KEY_HEX)));
        assertThat(json, hasJsonPath("$.parsed.Hash", is("hash-" + KEY_HEX)));
    }

    /**
     * Tests that a CLKey  Hash value can be converted to JSON
     */
    @Test
    void clHashKeyToJson() throws IOException {

        String keyHex = "0fe62e5a3afe9792da5c2154d0c295c108dab251f542273563d4d4bffd600945";
        final byte[] key = ByteUtils.decodeHex(keyHex);

        final CLValue clKeyValue = CLValueBuilder.hashKey(key);

        final String json = writeToJsonString(clKeyValue);

        assertThat(json, is(notNullValue()));

        assertThat(json, hasJsonPath("$.cl_type", is("Key")));
        assertThat(json, hasJsonPath("$.bytes", is("01" + keyHex)));
        assertThat(json, hasJsonPath("$.parsed.Hash", is("hash-" + keyHex)));
    }

    /**
     * Tests that a CLKey URef value can be converted to JSON
     */
    @Test
    void clURefKeyToJson() throws IOException {

        String keyHex = "0fe62e5a3afe9792da5c2154d0c295c108dab251f542273563d4d4bffd600945";
        final byte[] key = ByteUtils.decodeHex(keyHex);

        final CLValue clKeyValue = CLValueBuilder.uRefKey(key);

        final String json = writeToJsonString(clKeyValue);

        assertThat(json, is(notNullValue()));

        assertThat(json, hasJsonPath("$.cl_type", is("Key")));
        assertThat(json, hasJsonPath("$.bytes", is("02" + keyHex)));
        assertThat(json, hasJsonPath("$.parsed.Hash", is("hash-" + keyHex)));
    }
}
