package com.casper.sdk.service.json.serialize;

import com.casper.sdk.types.CLType;
import com.casper.sdk.types.CLTypeInfo;
import com.casper.sdk.types.CLValue;
import com.casper.sdk.types.DeployNamedArg;
import com.casper.sdk.service.json.JsonTestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 *
 */
class DeployNamedArgJsonSerializerTest {

    private String json;

    @BeforeEach
    void setUp() throws IOException {
        final DeployNamedArg amount = new DeployNamedArg(
                "amount",
                new CLValue(CLValue.fromString("0400ca9a3b"), new CLTypeInfo(CLType.U512), "1000000000")
        );

        json = JsonTestUtils.writeToJsonString(amount);
    }

    @Test
    void serializeDeployNamedArg() {
        assertThat(json, hasJsonPath("$.[0]", is("amount")));
        assertThat(json, hasJsonPath("$.[1].cl_type", is("U512")));
        assertThat(json, hasJsonPath("$.[1].bytes", is("0400ca9a3b")));
        assertThat(json, hasJsonPath("$.[1].parsed", is("1000000000")));
    }
}