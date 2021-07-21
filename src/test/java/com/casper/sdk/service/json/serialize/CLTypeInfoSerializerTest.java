package com.casper.sdk.service.json.serialize;

import com.casper.sdk.types.CLByteArrayInfo;
import com.casper.sdk.types.CLType;
import com.casper.sdk.types.CLTypeInfo;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.casper.sdk.service.json.JsonTestUtils.writeToJsonString;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the CLTypeInfo type class
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
