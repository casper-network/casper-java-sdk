package com.casper.sdk.service.json.serialize;

import com.casper.sdk.service.json.JsonTestUtils;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class DeployExecutableJsonSerializerTest {

    @Test
    void testStoredContractFromHashToJson() throws IOException {
        final StoredContractByHash storedContractByHash = new StoredContractByHash(
                new ContractHash("0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f"),
                "pclphXwfYmCmdITj8hnh",
                Arrays.asList(
                        new DeployNamedArg("foo", new CLValue("bar".getBytes(StandardCharsets.UTF_8), new CLTypeInfo(CLType.STRING), "bar")),
                        new DeployNamedArg("amount", new CLValue("05005550b405", new CLTypeInfo(CLType.U64), "24500000000"))
                )
        );

        final String json = JsonTestUtils.writeToJsonString(storedContractByHash);

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

    @Test
    void testWriteStoredContractByName() throws IOException {
        StoredContractByName storedContractByName = new StoredContractByName(
                "foo-bar",
                "pclphXwfYmCmdITj8hnh",
                Arrays.asList(
                        new DeployNamedArg("foo", new CLValue("bar".getBytes(StandardCharsets.UTF_8), new CLTypeInfo(CLType.STRING), "bar")),
                        new DeployNamedArg("amount", new CLValue("05005550b405", new CLTypeInfo(CLType.U64), "24500000000"))
                )
        );

        final String json = JsonTestUtils.writeToJsonString(storedContractByName);

        assertThat(json, hasJsonPath("$.StoredContractByName"));
        assertThat(json, hasJsonPath("$.StoredContractByName.name", is("foo-bar")));
        assertThat(json, hasJsonPath("$.StoredContractByName.entry_point", is(storedContractByName.getEntryPoint())));
        assertThat(json, hasJsonPath("$.StoredContractByName.args"));
        assertThat(json, hasJsonPath("$.StoredContractByName.args[0]"));
        assertThat(json, hasJsonPath("$.StoredContractByName.args[1]"));
    }

    @Test
    void testWriteStoredVersionedContractByName() throws IOException {
        StoredVersionedContractByName storedVersionedContractByName = new StoredVersionedContractByName( "foo-bar",
                123456,
                "pclphXwfYmCmdITj8hnh",
                Arrays.asList(
                        new DeployNamedArg("foo", new CLValue("bar".getBytes(StandardCharsets.UTF_8), new CLTypeInfo(CLType.STRING), "bar")),
                        new DeployNamedArg("amount", new CLValue("05005550b405", new CLTypeInfo(CLType.U64), "24500000000"))
                ));
        final String json = JsonTestUtils.writeToJsonString(storedVersionedContractByName);

        assertThat(json, hasJsonPath("$.StoredVersionedContractByName"));
        assertThat(json, hasJsonPath("$.StoredVersionedContractByName.name", is("foo-bar")));
        assertThat(json, hasJsonPath("$.StoredVersionedContractByName.version", is(123456)));
        assertThat(json, hasJsonPath("$.StoredVersionedContractByName.entry_point", is("pclphXwfYmCmdITj8hnh")));
        assertThat(json, hasJsonPath("$.StoredVersionedContractByName.args"));
        assertThat(json, hasJsonPath("$.StoredVersionedContractByName.args[0]"));
        assertThat(json, hasJsonPath("$.StoredVersionedContractByName.args[1]"));
    }
}
