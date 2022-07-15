package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.service.serialization.util.CollectionUtils;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PublicKey;
import java.time.Instant;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the {@link MapSerializer}
 */
class MapSerializerTest {

    private final MapSerializer serializer = new MapSerializer(new TypesFactory());

    @Test
    void serializeCLMap() {

        final String expected = "020000004ce46c6043bcd33e4686d2b7a369c606bfcce4c26ca14d2c73fac82403801a0617de3433d6ab1c9fa4aa9fb8d874dba9a00b2b562d16da533460657503c02709".toLowerCase();

        final String hexKey1 = "e3D394334Ce46C6043BCd33E4686D2B7a369C606BfCce4C26ca14d2C73Fac824";
        final String hexKey2 = "219ac9a617DE3433d6ab1C9fA4aa9FB8D874DBa9A00b2B562d16da5334606575";

        final CLMap clMap = new CLMap((byte[]) null,
                new CLMapTypeInfo(new CLByteArrayInfo(32), new CLTypeInfo(CLType.U256)),
                CollectionUtils.Map.of(

                        new CLValue(ByteUtils.decodeHex(hexKey1), new CLByteArrayInfo(32), hexKey1),
                        CLValueBuilder.u256("400000"),
                        new CLValue(ByteUtils.decodeHex(hexKey2), new CLByteArrayInfo(32), hexKey2),
                        CLValueBuilder.u256("600000")
                )
        );

        String serialized = ByteUtils.encodeHexString(serializer.serialize(clMap)).toLowerCase();
        assertThat(serialized, is(expected));
    }

    @Test
    void clMapSerializationTest() {

        final CLValue key1 = CLValueBuilder.byteArray("e07cA98F1b5C15bC9ce75e8adB8a3b4D334A1B1Fa14DD16CfD3320bf77Cc3aAb");
        final CLValue value = CLValueBuilder.u256(0.4e6);
        final CLValue key2 = CLValueBuilder.byteArray("Bbf348055524243E10605e534C952043042E219d6305CC948A1bDcbc767CC970");
        final CLValue value2 = CLValueBuilder.u256(600000);

        final CLMap map = CLValueBuilder.map(CollectionUtils.Map.of(key1, value, key2, value2));

        String actual = ByteUtils.encodeHexString(serializer.serialize(map)).toLowerCase();
        String expected = "02000000e07ca98f1b5c15bc9ce75e8adb8a3b4d334a1b1fa14dd16cfd3320bf77cc3aab03801a06bbf348055524243e10605e534c952043042e219d6305cc948a1bdcbc767cc97003c02709".toLowerCase();
        assertThat(actual, is(expected));
    }

    @Test
    void clMapSerializationIssue116() throws IOException {

        CasperSdk casperSdk = new CasperSdk("localhost", 11101);

        final PublicKey platformPublicKey = casperSdk.loadKey(MapSerializerTest.class.getResourceAsStream("/track-4/assets/keys/wm/public_key.pem"));


        final String contractHash = "D5f63F80B885B849443Ef758FdC97f69910B84440Ff41463B4ab3F4bE02Ad16a";
        final CLValue key1 = CLValueBuilder.byteArray("e07cA98F1b5C15bC9ce75e8adB8a3b4D334A1B1Fa14DD16CfD3320bf77Cc3aAb");
        final CLValue value = CLValueBuilder.u256(0.4e6);
        final CLValue key2 = CLValueBuilder.byteArray("Bbf348055524243E10605e534C952043042E219d6305CC948A1bDcbc767CC970");
        final CLValue value2 = CLValueBuilder.u256(600000);

        final Deploy deploy = casperSdk.makeDeploy(
                new DeployParams(
                        platformPublicKey,
                        "integration-test",
                        1,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null),
                new StoredContractByHash(
                        new ContractHash(contractHash),
                        "set_state",
                        new DeployNamedArgBuilder()
                                .add("token_id", CLValueBuilder.string("ee7c9342ede1f0c1740a58ac2e87518fc9aa7e29fa2fdbedf73a975bc566e2d3"))
                                .add("asset_holders", CLValueBuilder.map(CollectionUtils.Map.of(key1, value, key2, value2)))
                                .build()),
                casperSdk.standardPayment(new BigInteger("10000000000"))
        );

        assertThat(deploy, is(notNullValue()));

        final String json = casperSdk.deployToJson(deploy);

        assertThat(json, is(notNullValue()));

        assertThat(json, hasJsonPath("$.session"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[0]"));

        // asset_holders
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[0]", is("asset_holders")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].cl_type"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].cl_type.Map"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].cl_type.Map.key"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].cl_type.Map.key.ByteArray", is(32)));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].cl_type.Map.value", is("U256")));

        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].parsed[0].key", is("e07cA98F1b5C15bC9ce75e8adB8a3b4D334A1B1Fa14DD16CfD3320bf77Cc3aAb")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].parsed[0].value", is(400000.0)));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].parsed[1].key", is("Bbf348055524243E10605e534C952043042E219d6305CC948A1bDcbc767CC970")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].parsed[1].value", is(600000)));

        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].bytes", is("02000000e07ca98f1b5c15bc9ce75e8adb8a3b4d334a1b1fa14dd16cfd3320bf77cc3aab03801a06bbf348055524243e10605e534c952043042e219d6305cc948a1bdcbc767cc97003c02709")));
    }
}