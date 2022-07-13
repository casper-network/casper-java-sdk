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
                                .add("token_id", CLValueBuilder.string("token-id"))
                                .add("instrument_id", CLValueBuilder.string(contractuuid))
                                .add("asset_decimals", CLValueBuilder.u256(1))
                                .add("asset_units", CLValueBuilder.u256(50000))
                                .add("asset_holders", CLValueBuilder.map(CollectionUtils.Map.of(key1, value)))
                                .add("liability_decimals", CLValueBuilder.u256(1))
                                .add("liability_units", CLValueBuilder.u256(40000))
                                .add("liability_holders", CLValueBuilder.map(CollectionUtils.Map.of(key2, value)))
                                .build()),
                casperSdk.standardPayment(new BigInteger("10000000000"))
        );

        assertThat(deploy, is(notNullValue()));

        final String jsonDeploy = casperSdk.deployToJson(deploy);

        assertThat(jsonDeploy, is(notNullValue()));
    }
}
