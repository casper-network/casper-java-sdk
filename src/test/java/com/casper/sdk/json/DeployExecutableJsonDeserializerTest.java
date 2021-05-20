package com.casper.sdk.json;

import com.casper.sdk.domain.DeployExecutable;
import com.casper.sdk.domain.DeployNamedArg;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Tests a DeployExecutable can be parsed from a JSON string
 */
class DeployExecutableJsonDeserializerTest {

    // Transfer is used to indicate the type
    private static final String SESSION_JSON = /* "session": */ """
            {
              "Transfer": {
                "args": [
                  [
                    "amount",
                    {
                      "cl_type": "U512",
                      "bytes": "05005550b405",
                      "parsed": "24500000000"
                    }
                  ],
                  [
                    "target",
                    {
                      "cl_type": {
                        "ByteArray": 32
                      },
                      "bytes": "0101010101010101010101010101010101010101010101010101010101010101",
                      "parsed": "0101010101010101010101010101010101010101010101010101010101010101"
                    }
                  ],
                  [
                    "id",
                    {
                      "cl_type": "U64",
                      "bytes": "01e703000000000000",
                      "parsed": 999
                    }
                  ],
                  [
                    "additional_info",
                    {
                      "cl_type": "String",
                      "bytes": "1000000074686973206973207472616e73666572",
                      "parsed": "this is transfer"
                    }
                  ]
                ]
              }
            }""";

    private DeployExecutable deployExecutable;

    @BeforeEach
    void setUp() throws IOException {
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(DeployExecutable.class, new DeployExecutableJsonDeserializer());
        module.addDeserializer(DeployNamedArg.class, new DeployNamedArgJsonDeserializer());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        deployExecutable = mapper.reader().readValue(SESSION_JSON, DeployExecutable.class);
    }


    @Test
    void testParseDeployExecutableFromJson() {
        assertThat(deployExecutable, is(notNullValue()));
    }
}
