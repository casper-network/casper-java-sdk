package com.casper.sdk.model.deploy.executabledeploy;

import com.casper.sdk.model.clvalue.CLValueI32;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.deploy.NamedArg;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests for the StoredVersionedContractByName class
 *
 * @author ian@meywood.com
 */
class StoredVersionedContractByNameTest {

    @Test
    void jsonSerializeStoredVersionedContractByName() throws ValueSerializationException, JsonProcessingException {

        final StoredVersionedContractByName versionedContractByName = new StoredVersionedContractByName();

        versionedContractByName.setName("counter");
        versionedContractByName.setEntryPoint("counter_inc");
        versionedContractByName.setVersion(1L);
        versionedContractByName.setArgs(Arrays.asList(
                new NamedArg<>("one", new CLValueI32(1)),
                new NamedArg<>("two", new CLValueI32(2))
        ));

        final String json = new ObjectMapper().writeValueAsString(versionedContractByName);

        assertThat(json, is(notNullValue()));

        JsonNode root = new ObjectMapper().reader().readTree(json);
        assertThat(root.at("/StoredVersionedContractByName/name").asText(), is("counter"));
        assertThat(root.at("/StoredVersionedContractByName/entry_point").asText(), is("counter_inc"));
        assertThat(root.at("/StoredVersionedContractByName/version").asInt(), is(1));
        assertThat(root.at("/StoredVersionedContractByName/args/0/0").asText(), is("one"));
        assertThat(root.at("/StoredVersionedContractByName/args/0/1/bytes").asText(), is("01000000"));
        assertThat(root.at("/StoredVersionedContractByName/args/0/1/cl_type").asText(), is("I32"));
    }

    @Test
    void serializeStoredVersionedContractByHash() throws Exception {

        final StoredVersionedContractByName versionedContractByName = new StoredVersionedContractByName();

        versionedContractByName.setName("counter");
        versionedContractByName.setEntryPoint("transfer");
        versionedContractByName.setArgs(Arrays.asList(
                new NamedArg<>("one", new CLValueI32(1)),
                new NamedArg<>("two", new CLValueI32(2))
        ));

        final SerializerBuffer serializerBuffer = new SerializerBuffer();
        versionedContractByName.serialize(serializerBuffer, Target.BYTE);
        final String expectedBytes = "0407000000636f756e74657200080000007472616e7366657202000000030000006f6e650400000001000000010300000074776f040000000200000001";
        final String actual = Hex.encode(serializerBuffer.toByteArray());
        assertThat(actual, is(expectedBytes));
    }
}
