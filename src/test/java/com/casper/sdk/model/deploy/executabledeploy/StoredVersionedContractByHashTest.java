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
 * Unit tests for the StoredVersionedContractByHash class.
 *
 * @author ian@meywood.com
 */
class StoredVersionedContractByHashTest {

    @Test
    void jsonSerializeStoredVersionedContractByHash() throws ValueSerializationException, JsonProcessingException {

        final StoredVersionedContractByHash versionedContractByHash = StoredVersionedContractByHash.builder()
                .hash("92173d49744c790d47e50d011d89e1b5a33ed2d9fae8d9459325224d8f98f3e5")
                .version(1L)
                .entryPoint("counter_inc")
                .args(Arrays.asList(new NamedArg<>("one", new CLValueI32(1)), new NamedArg<>("two", new CLValueI32(2))))
                .build();

        final String json = new ObjectMapper().writeValueAsString(versionedContractByHash);

        assertThat(json, is(notNullValue()));

        JsonNode root = new ObjectMapper().reader().readTree(json);
        assertThat(root.at("/StoredVersionedContractByHash/hash").asText(), is("92173d49744c790d47e50d011d89e1b5a33ed2d9fae8d9459325224d8f98f3e5"));
        assertThat(root.at("/StoredVersionedContractByHash/entry_point").asText(), is("counter_inc"));
        assertThat(root.at("/StoredVersionedContractByHash/version").asInt(), is(1));
        assertThat(root.at("/StoredVersionedContractByHash/args/0/0").asText(), is("one"));
        assertThat(root.at("/StoredVersionedContractByHash/args/0/1/bytes").asText(), is("01000000"));
        assertThat(root.at("/StoredVersionedContractByHash/args/0/1/cl_type").asText(), is("I32"));
    }

    @Test
    void serializeStoredVersionedContractByHashTest() throws Exception {

        final StoredVersionedContractByHash versionedContractByHash = StoredVersionedContractByHash.builder()
                .hash("92173d49744c790d47e50d011d89e1b5a33ed2d9fae8d9459325224d8f98f3e5")
                .version(1L)
                .entryPoint("transfer")
                .args(Arrays.asList(new NamedArg<>("one", new CLValueI32(1)), new NamedArg<>("two", new CLValueI32(2))))
                .build();

        final SerializerBuffer serializerBuffer = new SerializerBuffer();
        versionedContractByHash.serialize(serializerBuffer, Target.BYTE);
        final String expectedBytes = "0392173d49744c790d47e50d011d89e1b5a33ed2d9fae8d9459325224d8f98f3e50101000000080000007472616e7366657202000000030000006f6e650400000001000000010300000074776f040000000200000001";
        final String actual = Hex.encode(serializerBuffer.toByteArray());
        assertThat(actual, is(expectedBytes));
    }
}
