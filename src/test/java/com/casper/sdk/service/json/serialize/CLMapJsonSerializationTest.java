package com.casper.sdk.service.json.serialize;

import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Map;

import static com.casper.sdk.service.json.JsonTestUtils.writeToJsonString;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests that a CLMap can be serialized to JSON
 */
public class CLMapJsonSerializationTest {

    @Test
    void serializeClMapToJson() throws IOException {

        final CLMap clMap = new CLMap("02000000e07ca98f1b5c15bc9ce75e8adb8a3b4d334a1b1fa14dd16cfd3320bf77cc3aab03801a06bbf348055524243e10605e534c952043042e219d6305cc948a1bdcbc767cc97003c02709",
                new CLMapTypeInfo(new CLByteArrayInfo(32), new CLTypeInfo(CLType.U256)),
                Map.of(
                        CLValueBuilder.byteArray("e3D394334Ce46C6043BCd33E4686D2B7a369C606BfCce4C26ca14d2C73Fac824"),
                        CLValueBuilder.u256("400000"),
                        CLValueBuilder.byteArray("219ac9a617DE3433d6ab1C9fA4aa9FB8D874DBa9A00b2B562d16da5334606575"),
                        CLValueBuilder.u256("600000")
                )
        );

        assertThat(clMap.size(), is(2));

        final String json = writeToJsonString(clMap);

        assertThat(json, hasJsonPath("$.cl_type"));
        assertThat(json, hasJsonPath("$.cl_type.Map"));
        assertThat(json, hasJsonPath("$.cl_type.Map.key.ByteArray", is(32)));
        assertThat(json, hasJsonPath("$.cl_type.Map.value", is("U256")));
        assertThat(json, hasJsonPath("$.bytes", is(ByteUtils.encodeHexString(clMap.getBytes()))));
        assertThat(json, hasJsonPath("$.parsed"));
        assertThat(json, hasJsonPath("$.parsed[0].key", is("e3D394334Ce46C6043BCd33E4686D2B7a369C606BfCce4C26ca14d2C73Fac824")));
        assertThat(json, hasJsonPath("$.parsed[0].value", is("400000")));
        assertThat(json, hasJsonPath("$.parsed[1].key", is("219ac9a617DE3433d6ab1C9fA4aa9FB8D874DBa9A00b2B562d16da5334606575")));
        assertThat(json, hasJsonPath("$.parsed[1].value", is("600000")));
    }
}
