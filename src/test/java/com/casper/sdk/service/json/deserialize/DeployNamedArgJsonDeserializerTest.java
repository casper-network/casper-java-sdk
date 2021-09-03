package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.CLType;
import com.casper.sdk.types.CLValue;
import com.casper.sdk.types.DeployNamedArg;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests for the DeployNamedArgJsonDeserializer
 */
class DeployNamedArgJsonDeserializerTest {

    // This is funky JSON!
    private static final String JSON = "[\n" +
                                       "   \"amount\",\n" +
                                       "   {\n" +
                                       "      \"cl_type\": \"U512\",\n" +
                                       "      \"bytes\": \"05005550b405\",\n" +
                                       "      \"parsed\": \"24500000000\"\n" +
                                       "   }\n" +
                                       "]";

    /**
     *
     * Tests that a single DeployNamedArg can be deserialized from JSON
     */
    @Test
    void deserializeDeployU512NamedArg() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final DeployNamedArg deployNamedArg = mapper.reader().readValue(JSON, DeployNamedArg.class);

        assertThat(deployNamedArg, is(notNullValue()));
        assertThat(deployNamedArg.getName(), is("amount"));
        assertThat(deployNamedArg.getValue(), is(notNullValue()));
        assertThat(deployNamedArg.getValue().getCLTypeInfo().getType(), is(CLType.U512));
        assertThat(deployNamedArg.getValue().getBytes(), is(CLValue.fromString("05005550b405")));
        assertThat(deployNamedArg.getValue().getParsed(), is("24500000000"));
    }
}