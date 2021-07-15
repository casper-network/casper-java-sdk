package com.casper.sdk.json.serialize;

import com.casper.sdk.domain.*;
import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.casper.sdk.json.JsonTestUtils.writeToJsonString;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the {@link CLValueJsonSerializer}
 */
class CLValueJsonSerializerTest {

    /**
     * Tests a CLByteArrayInfo can be serialized to JSON
     */
    @Test
    void serializeCLByteArrayValue() throws IOException {

        final String json = writeToJsonString(new CLValue("0101010101010101010101010101010101010101010101010101010101010101",
                new CLByteArrayInfo(32),
                "0101010101010101010101010101010101010101010101010101010101010101")
        );

        assertThat(json, hasJsonPath("$.cl_type"));
        assertThat(json, hasJsonPath("$.cl_type.ByteArray"));
        assertThat(json, hasJsonPath("$.cl_type.ByteArray", is(32)));
        assertThat(json, hasJsonPath("$.bytes", is("0101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(json, hasJsonPath("$.parsed", is("0101010101010101010101010101010101010101010101010101010101010101")));
    }


    /**
     * Tests that CLOptionTypeInfo can be serialized to JSON
     */
    @Test
    void serializeCLOptionValue() throws IOException {

        final CLValue clOptionValue = new CLValue("010100000000000000", new CLOptionTypeInfo(new CLTypeInfo(CLType.U64)), 1);
        final String json = writeToJsonString(clOptionValue);

        assertThat(json, hasJsonPath("$.cl_type"));
        assertThat(json, hasJsonPath("$.cl_type.Option"));
        assertThat(json, hasJsonPath("$.cl_type.Option", is("U64")));
        assertThat(json, hasJsonPath("$.bytes", is("010100000000000000")));
        assertThat(json, hasJsonPath("$.parsed", is(1)));
    }

    @Test
    void serializeStandardValue() throws IOException {
        final CLValue clValue = new CLValue(ByteUtils.decodeHex("0400f90295"), CLType.U512, "2500000000");
        final String json = writeToJsonString(clValue);
        assertThat(json, hasJsonPath("$.cl_type", is("U512")));
        assertThat(json, hasJsonPath("$.bytes", is("0400f90295")));
        assertThat(json, hasJsonPath("$.parsed", is("2500000000")));
    }
}
