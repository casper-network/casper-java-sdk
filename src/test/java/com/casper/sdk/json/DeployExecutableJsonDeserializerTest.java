package com.casper.sdk.json;

import com.casper.sdk.domain.DeployExecutable;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Tests a DeployExecutable can be parsed from a JSON string
 */
public class DeployExecutableJsonDeserializerTest {

    final String SESSION_JSON = /* "session": */ "{\n" +
            "    \"Transfer\": {\n" +  // Transfer is used to indicate the type
            "      \"args\": [\n" +
            "        [\n" +
            "          \"amount\",\n" +
            "          {\n" +
            "            \"cl_type\": \"U512\",\n" +
            "            \"bytes\": \"05005550b405\",\n" +
            "            \"parsed\": \"24500000000\"\n" +
            "          }\n" +
            "        ],\n" +
            "        [\n" +
            "          \"target\",\n" +
            "          {\n" +
            "            \"cl_type\": {\n" +
            "              \"ByteArray\": 32\n" +
            "            },\n" +
            "            \"bytes\": \"0101010101010101010101010101010101010101010101010101010101010101\",\n" +
            "            \"parsed\": \"0101010101010101010101010101010101010101010101010101010101010101\"\n" +
            "          }\n" +
            "        ],\n" +
            "        [\n" +
            "          \"id\",\n" +
            "          {\n" +
            "            \"cl_type\": \"U64\",\n" +
            "            \"bytes\": \"01e703000000000000\",\n" +
            "            \"parsed\": 999\n" +
            "          }\n" +
            "        ],\n" +
            "        [\n" +
            "          \"additional_info\",\n" +
            "          {\n" +
            "            \"cl_type\": \"String\",\n" +
            "            \"bytes\": \"1000000074686973206973207472616e73666572\",\n" +
            "            \"parsed\": \"this is transfer\"\n" +
            "          }\n" +
            "        ]\n" +
            "      ]\n" +
            "    }\n" +
            "  }";

    private DeployExecutable deployExecutable;

    @BeforeEach
    void setUp() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        // TODO
       // deployExecutable = mapper.reader().readValue(SESSION_JSON, DeployExecutable.class);
    }


    @Test
    void testParseDeployExecutableFromJson() {

      //  assertThat(deployExecutable, Is.is(notNullValue()));

    }
}
