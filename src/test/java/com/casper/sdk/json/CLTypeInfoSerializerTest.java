package com.casper.sdk.json;

import com.casper.sdk.domain.CLByteArrayInfo;
import com.casper.sdk.domain.CLType;
import com.casper.sdk.domain.CLTypeInfo;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.casper.sdk.json.JsonTestUtils.writeToJsonString;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the CLTypeInfo domain class
 */
class CLTypeInfoSerializerTest {

    /**
     * Tests a CLByteArrayInfo can be serialized to JSON "cl_type": { "ByteArray": 32 },
     */
    @Test
    void serializeCLByteArrayInfoToJson() throws IOException {

        final String json = writeToJsonString(new CLByteArrayInfo(32));

        assertThat(json, hasJsonPath("$.ByteArray"));
        assertThat(json, hasJsonPath("$.ByteArray", is(32)));
    }

    /**
     * Tests the basic CL type can be written to JSON
     */
    @Test
    void serializeCLTypeBasicTypesToJson() throws IOException {

        for (CLType type : CLType.values()) {
            if (type != CLType.BYTE_ARRAY) {
                final String json = writeToJsonString(new CLTypeInfo(type));
                assertThat(json, is("\"" + type.getJsonName() + "\""));
            }
        }
    }
}