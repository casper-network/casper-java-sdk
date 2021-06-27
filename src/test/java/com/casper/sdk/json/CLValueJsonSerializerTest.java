package com.casper.sdk.json;

import com.casper.sdk.domain.CLByteArrayInfo;
import com.casper.sdk.domain.CLValue;
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
}
