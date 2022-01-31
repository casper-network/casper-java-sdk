package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for CLMap Deserialization
 */
public class CLMapJsonDeserializerTest {

    private static final String CL_MAP_JSON =
            "{\n" +
            "  \"cl_type\": {\n" +
            "    \"Map\": {\n" +
            "      \"key\": {\n" +
            "        \"ByteArray\": 32\n" +
            "      },\n" +
            "      \"value\": \"U256\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"bytes\": \"02000000e07ca98f1b5c15bc9ce75e8adb8a3b4d334a1b1fa14dd16cfd3320bf77cc3aab03801a06bbf348055524243e10605e534c952043042e219d6305cc948a1bdcbc767cc97003c02709\",\n" +
            "  \"parsed\": [\n" +
            "    {\n" +
            "      \"key\": \"e07cA98F1b5C15bC9ce75e8adB8a3b4D334A1B1Fa14DD16CfD3320bf77Cc3aAb\",\n" +
            "      \"value\": \"400000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"key\": \"Bbf348055524243E10605e534C952043042E219d6305CC948A1bDcbc767CC970\",\n" +
            "      \"value\": \"600000\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Test
    void deserializeCLMap() throws IOException {

        final ObjectMapper mapper = new ObjectMapper();
        final CLMap clMap = mapper.reader().readValue(CL_MAP_JSON, CLMap.class);

        byte[] expectedBytes = ByteUtils.decodeHex("02000000e07ca98f1b5c15bc9ce75e8adb8a3b4d334a1b1fa14dd16cfd3320bf77cc3aab03801a06bbf348055524243e10605e534c952043042e219d6305cc948a1bdcbc767cc97003c02709");
        assertThat(clMap.getBytes(), is(expectedBytes));

        assertThat(clMap.getCLType(), is(CLType.MAP));

        assertThat(clMap.getKeyType(), is(new CLByteArrayInfo(32)));
        assertThat(clMap.getValueType(), is(new CLTypeInfo(CLType.U256)));
        assertThat(clMap.size(), is(2));

        final CLValue key1 = CLValueBuilder.byteArray("e07cA98F1b5C15bC9ce75e8adB8a3b4D334A1B1Fa14DD16CfD3320bf77Cc3aAb");
        final CLValue expectedValue1 = CLValueBuilder.u256("400000");
        assertThat(clMap.get(key1), is(expectedValue1));

        final CLValue key2 = CLValueBuilder.byteArray("Bbf348055524243E10605e534C952043042E219d6305CC948A1bDcbc767CC970");
        final CLValue expectedValue2 = CLValueBuilder.u256("600000");
        assertThat(clMap.get(key2), is(expectedValue2));
    }
}
