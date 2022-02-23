package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

class DeployExecutableJsonDeserializerTest {

    private static final String STORED_CONTRACT_BY_HASH_JSON =
            "{\n" +
            "    \"StoredContractByHash\": {\n" +
            "      \"hash\": \"0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f\",\n" +
            "      \"entry_point\": \"pclphXwfYmCmdITj8hnh\",\n" +
            "      \"args\": [\n" +
            "        [\n" +
            "            \"amount\",\n" +
            "          {\n" +
            "            \"cl_type\": \"U512\",\n" +
            "            \"bytes\": \"0400ca9a3b\",\n" +
            "            \"parsed\": \"1000000000\"\n" +
            "          }\n" +
            "        ]\n" +
            "      ]\n" +
            "    }\n" +
            "}";

    private static final String STORED_CONTRACT_BY_NAME_JSON =
            "{\n" +
            "    \"StoredContractByName\": {\n" +
            "      \"name\": \"foo-bar\",\n" +
            "      \"entry_point\": \"pclphXwfYmCmdITj8hnh\",\n" +
            "      \"args\": [\n" +
            "        [\n" +
            "            \"amount\",\n" +
            "          {\n" +
            "            \"cl_type\": \"U512\",\n" +
            "            \"bytes\": \"0400ca9a3b\",\n" +
            "            \"parsed\": \"1000000000\"\n" +
            "          }\n" +
            "        ]\n" +
            "      ]\n" +
            "    }\n" +
            "}";

    private static final String STORED_VERSIONED_CONTRACT_BY_NAME_JSON =
            "{\n" +
            "    \"StoredVersionedContractByName\": {\n" +
            "      \"name\": \"foo-bar\",\n" +
            "      \"version\": \"123456\",\n" +
            "      \"entry_point\": \"pclphXwfYmCmdITj8hnh\",\n" +
            "      \"args\": [\n" +
            "        [\n" +
            "            \"amount\",\n" +
            "          {\n" +
            "            \"cl_type\": \"U512\",\n" +
            "            \"bytes\": \"0400ca9a3b\",\n" +
            "            \"parsed\": \"1000000000\"\n" +
            "          }\n" +
            "        ]\n" +
            "      ]\n" +
            "    }\n" +
            "}";

    private static final String STORED_VERSIONED_CONTRACT_BY_HASH_JSON =
            "{\n" +
            "    \"StoredVersionedContractByHash\": {\n" +
            "      \"hash\": \"c4c411864f7b717c27839e56f6f1ebe5da3f35ec0043f437324325d65a22afa4\",\n" +
            "      \"version\": \"123456\",\n" +
            "      \"entry_point\": \"pclphXwfYmCmdITj8hnh\",\n" +
            "      \"args\": [\n" +
            "        [\n" +
            "            \"amount\",\n" +
            "          {\n" +
            "            \"cl_type\": \"U512\",\n" +
            "            \"bytes\": \"0400ca9a3b\",\n" +
            "            \"parsed\": \"1000000000\"\n" +
            "          }\n" +
            "        ]\n" +
            "      ]\n" +
            "    }\n" +
            "}";


    @Test
    void parseStoredContractByHashFromJson() throws IOException {

        final ObjectMapper mapper = new ObjectMapper();
        final DeployExecutable deployExecutable = mapper.reader().readValue(STORED_CONTRACT_BY_HASH_JSON, DeployExecutable.class);
        assertThat(deployExecutable, is(instanceOf(StoredContractByHash.class)));

        final StoredContractByHash storedContractByHash = (StoredContractByHash) deployExecutable;
        assertThat(storedContractByHash.getHash().toString(), is("0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f"));
        assertThat(storedContractByHash.getEntryPoint(), is("pclphXwfYmCmdITj8hnh"));
        assertThat(storedContractByHash.getArgs().size(), is(1));
    }

    @Test
    void parseStoredContractByNameFromJson() throws IOException {

        final ObjectMapper mapper = new ObjectMapper();
        final DeployExecutable deployExecutable = mapper.reader().readValue(STORED_CONTRACT_BY_NAME_JSON, DeployExecutable.class);
        assertThat(deployExecutable, is(instanceOf(StoredContractByName.class)));

        final StoredContractByName storedContractByName = (StoredContractByName) deployExecutable;
        assertThat(storedContractByName.getName(), is("foo-bar"));
        assertThat(storedContractByName.getEntryPoint(), is("pclphXwfYmCmdITj8hnh"));
        assertThat(storedContractByName.getArgs().size(), is(1));
    }

    @Test
    void parseStoredVersionedContractByNameFromJson() throws IOException {

        final ObjectMapper mapper = new ObjectMapper();
        final DeployExecutable deployExecutable = mapper.reader().readValue(STORED_VERSIONED_CONTRACT_BY_NAME_JSON, DeployExecutable.class);
        assertThat(deployExecutable, is(instanceOf(StoredVersionedContractByName.class)));

        final StoredVersionedContractByName storedVersionedContractByName = (StoredVersionedContractByName) deployExecutable;
        assertThat(storedVersionedContractByName.getName(), is("foo-bar"));
        assertThat(storedVersionedContractByName.getVersion().isPresent(), is(true));
        assertThat(storedVersionedContractByName.getVersion().get(), is(123456L));
        assertThat(storedVersionedContractByName.getEntryPoint(), is("pclphXwfYmCmdITj8hnh"));
        assertThat(storedVersionedContractByName.getArgs().size(), is(1));
    }

    @Test
    void parseStoredVersionedContractByHashFromJson() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final DeployExecutable deployExecutable = mapper.reader().readValue(STORED_VERSIONED_CONTRACT_BY_HASH_JSON, DeployExecutable.class);
        assertThat(deployExecutable, is(instanceOf(StoredVersionedContractByHash.class)));

        final StoredVersionedContractByHash storedVersionedContractByHash = (StoredVersionedContractByHash) deployExecutable;
        assertThat(storedVersionedContractByHash.getHash(), is(new ContractHash("c4c411864f7b717c27839e56f6f1ebe5da3f35ec0043f437324325d65a22afa4")));
        assertThat(storedVersionedContractByHash.getVersion().isPresent(), is(true));
        assertThat(storedVersionedContractByHash.getVersion().get(), is(123456L));
        assertThat(storedVersionedContractByHash.getEntryPoint(), is("pclphXwfYmCmdITj8hnh"));
        assertThat(storedVersionedContractByHash.getArgs().size(), is(1));
    }
}