package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.DeployExecutable;
import com.casper.sdk.types.StoredContractByHash;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

class DeployExecutableJsonDeserializerTest {

    private static final String JSON =
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


    @Test
    void parseStoredContractByHashFromJson() throws IOException {

        final ObjectMapper mapper = new ObjectMapper();
        final DeployExecutable deployExecutable = mapper.reader().readValue(JSON, DeployExecutable.class);
        assertThat(deployExecutable, is(instanceOf(StoredContractByHash.class)));

        final StoredContractByHash storedContractByHash = (StoredContractByHash) deployExecutable;
        assertThat(storedContractByHash.getHash().toString(), is("0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f"));
        assertThat(storedContractByHash.getEntryPoint(), is("pclphXwfYmCmdITj8hnh"));
        assertThat(storedContractByHash.getArgs().size(), is(1));
    }
}