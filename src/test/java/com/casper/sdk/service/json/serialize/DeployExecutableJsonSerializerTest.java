package com.casper.sdk.service.json.serialize;

import com.casper.sdk.service.json.JsonTestUtils;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class DeployExecutableJsonSerializerTest {

    private String json;
    private StoredContractByHash storedContractByHash;

    @BeforeEach
    void setUp() throws IOException {

        storedContractByHash = new StoredContractByHash(
                new ContractHash("0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f"),
                "pclphXwfYmCmdITj8hnh",
                Arrays.asList(
                        new DeployNamedArg("foo", new CLValue("bar".getBytes(StandardCharsets.UTF_8), new CLTypeInfo(CLType.STRING), "bar")),
                        new DeployNamedArg("amount", new CLValue("05005550b405", new CLTypeInfo(CLType.U64), "24500000000"))
                ));

        json = JsonTestUtils.writeToJsonString(storedContractByHash);
    }

    @Test
    void testStoredContractFromHashToJson() {
        assertThat(json, hasJsonPath("$.StoredContractByHash"));
        assertThat(json, hasJsonPath("$.StoredContractByHash.hash", is(storedContractByHash.getHash().toString())));
        assertThat(json, hasJsonPath("$.StoredContractByHash.entry_point", is(storedContractByHash.getEntryPoint())));
        assertThat(json, hasJsonPath("$.StoredContractByHash.args"));
        assertThat(json, hasJsonPath("$.StoredContractByHash.args[0]"));
        assertThat(json, hasJsonPath("$.StoredContractByHash.args[1]"));

        assertThat(json, hasJsonPath("$.StoredContractByHash.args[0].[0]", is("foo")));
        assertThat(json, hasJsonPath("$.StoredContractByHash.args[1].[0]", is("amount")));

        assertThat(json, hasJsonPath("$.StoredContractByHash.args[0].[1].cl_type", is("String")));
        assertThat(json, hasJsonPath("$.StoredContractByHash.args[0].[1].bytes", is("626172")));
        assertThat(json, hasJsonPath("$.StoredContractByHash.args[0].[1].parsed", is("bar")));

        assertThat(json, hasJsonPath("$.StoredContractByHash.args[1].[1].cl_type", is("U64")));
        assertThat(json, hasJsonPath("$.StoredContractByHash.args[1].[1].bytes", is("05005550b405")));
        assertThat(json, hasJsonPath("$.StoredContractByHash.args[1].[1].parsed", is("24500000000")));
    }
}