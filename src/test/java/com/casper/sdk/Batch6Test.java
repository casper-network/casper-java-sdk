package com.casper.sdk;

import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.service.serialization.util.CollectionUtils;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.PublicKey;
import java.time.Instant;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class Batch6Test {

    private CasperSdk casperSdk;

    @BeforeEach
    void setUp() {
        casperSdk = new CasperSdk("http://localhost", 11101);
    }

    /**
     * Tests that a StoredContractByHash can be made with CLMap parameters
     */
    @Test
    void testMakeDeployWithCLMap() throws IOException {

        final PublicKey platformPublicKey = casperSdk.loadKey(CasperSdkIntegrationTest.class.getResourceAsStream("/track-4/assets/keys/wm/public_key.pem"));

        final String contractHash = "D5f63F80B885B849443Ef758FdC97f69910B84440Ff41463B4ab3F4bE02Ad16a";
        final String contractuuid = "ISIN:DE000XXB2UL2";

        final CLValue key1 = CLValueBuilder.byteArray("e07cA98F1b5C15bC9ce75e8adB8a3b4D334A1B1Fa14DD16CfD3320bf77Cc3aAb");
        final CLValue value = CLValueBuilder.u256(0.4e6);
        final CLValue key2 = CLValueBuilder.byteArray("e3D394334Ce46C6043BCd33E4686D2B7a369C606BfCce4C26ca14d2C73Fac824");

        final CLValue key3 = CLValueBuilder.byteArray("Bbf348055524243E10605e534C952043042E219d6305CC948A1bDcbc767CC970");
        final CLValue value3 = CLValueBuilder.u256(600000);

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
                                .add("instrument_id", CLValueBuilder.string(contractuuid))
                                .add("asset_decimals", CLValueBuilder.u256(10))
                                .add("asset_units", CLValueBuilder.u256(1000000))
                                .add("asset_holders", CLValueBuilder.map(CollectionUtils.Map.of(key1, value, key3, value3)))
                                .add("liability_decimals", CLValueBuilder.u256(1))
                                .add("liability_units", CLValueBuilder.u256(40000))
                                .add("liability_holders", CLValueBuilder.map(CollectionUtils.Map.of(key2, value)))
                                .build()),
                casperSdk.standardPayment(new BigInteger("10000000000"))
        );

        assertThat(deploy, is(notNullValue()));

        final String json = casperSdk.deployToJson(deploy);

        assertThat(json, is(notNullValue()));

        assertThat(json, hasJsonPath("$.header"));
        assertThat(json, hasJsonPath("$.header.account", is("01f56e258a89bd12cea9d1d77cd2dd367f5134da564572e1c330fcae5579ad0613")));
        // TODO once all data same
        // assertThat(json, hasJsonPath("$.header.body_hash", is("73c5c15e8e2e45a2b0fae319ba6e916587ea9ae4a4311705551cbff68c70bf1d")));
        assertThat(json, hasJsonPath("$.header.ttl", is("30m")));
        assertThat(json, hasJsonPath("$.header.timestamp"));
        assertThat(json, hasJsonPath("$.header.chain_name", is("integration-test")));
        assertThat(json, hasJsonPath("$.header.gas_price", is(1)));

        assertThat(json, hasJsonPath("$.session"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[0]"));

        // token_id
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[0].[0]", is("token_id")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[0].[1].cl_type", is("String")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[0].[1].bytes", is("4000000065653763393334326564653166306331373430613538616332653837353138666339616137653239666132666462656466373361393735626335363665326433")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[0].[1].parsed", is("ee7c9342ede1f0c1740a58ac2e87518fc9aa7e29fa2fdbedf73a975bc566e2d3")));

        // instrument_id
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[0]", is("instrument_id")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].cl_type", is("String")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].bytes", is("110000004953494e3a444530303058584232554c32")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[1].[1].parsed", is("ISIN:DE000XXB2UL2")));

        // asset_decimals
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[2].[0]", is("asset_decimals")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[2].[1].cl_type", is("U256")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[2].[1].bytes", is("010a")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[2].[1].parsed", is(10)));

        // asset_units
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[3].[0]", is("asset_units")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[3].[1].cl_type", is("U256")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[3].[1].bytes", is("0340420f")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[3].[1].parsed", is(1000000)));

        // asset_holders
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[4].[0]", is("asset_holders")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[4].[1].cl_type"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[4].[1].cl_type.Map"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[4].[1].cl_type.Map.key"));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[4].[1].cl_type.Map.key.ByteArray", is(32)));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[4].[1].cl_type.Map.value", is("U256")));

        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[4].[1].parsed[0].key", is("e07cA98F1b5C15bC9ce75e8adB8a3b4D334A1B1Fa14DD16CfD3320bf77Cc3aAb")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[4].[1].parsed[0].value", is(400000.0)));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[4].[1].parsed[1].key", is("Bbf348055524243E10605e534C952043042E219d6305CC948A1bDcbc767CC970")));
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[4].[1].parsed[1].value", is(600000)));

        // EXPECTED == 02000000e07ca98f1b5c15bc9ce75e8adb8a3b4d334a1b1fa14dd16cfd3320bf77cc3aab03801a06bbf348055524243e10605e534c952043042e219d6305cc948a1bdcbc767cc97003c02709
        assertThat(json, hasJsonPath("$.session.StoredContractByHash.args[4].[1].bytes", is("02000000e07ca98f1b5c15bc9ce75e8adb8a3b4d334a1b1fa14dd16cfd3320bf77cc3aab03801a06bbf348055524243e10605e534c952043042e219d6305cc948a1bdcbc767cc97003c02709")));
    }
}
