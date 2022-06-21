package com.casper.sdk.how_to;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.KeyPairStreams;
import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.service.serialization.util.CollectionUtils;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.time.Instant;
import java.util.Map;

import static com.casper.sdk.how_to.HowToUtils.getFaucetKeyPair;
import static com.casper.sdk.how_to.HowToUtils.getWasmIn;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

public class HowToUseMaps {

    /** Create new instance of the SDK with default NCTL url and port */
    final CasperSdk casperSdk = new CasperSdk("http://localhost", 11101);


    @Test
    void clMapUsage() throws Exception {

        final KeyPairStreams faucetKeyPair = getFaucetKeyPair();
        final KeyPair operatorKeyPair = casperSdk.loadKeyPair(faucetKeyPair.getPublicKeyIn(), faucetKeyPair.getPrivateKeyIn());


        final Digest contractHash = installContract();

        assertThat(contractHash, is(notNullValue()));

      /*  CLValue key1 = CLValueBuilder.byteArray(contractHash);
        CLValue value1 = CLValueBuilder.u256(50000);

        CLMap clMap = new CLMap((String) null, new CLMapTypeInfo(key1.getCLTypeInfo(), value1.getCLTypeInfo()), null);
        clMap.put(key1, value1);
*/
        final Map<CLValue, CLValue> clMap = CollectionUtils.Map.of(CLValueBuilder.byteArray(contractHash), CLValueBuilder.u256(50000));

        final Deploy deploy = casperSdk.makeDeploy(
                new DeployParams(
                        // platformKeyPair.getPublic(),
                        operatorKeyPair.getPublic(),
                        "integration-test",
                        1,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null),
                new StoredContractByHash(
                        new ContractHash(contractHash.getHash()),
                        "set_state",
                        new DeployNamedArgBuilder()
                                .add("token_id", CLValueBuilder.string("A-TOKEN-ID"))
                                .add("instrument_id", CLValueBuilder.string("A-CONTRACT-ID"))
                                .add("asset_decimals", CLValueBuilder.u256(10))
                                .add("asset_units", CLValueBuilder.u256(50000))
                                .add("asset_holders", CLValueBuilder.map(clMap))
                                .add("liability_decimals", CLValueBuilder.u256(10))
                                .add("liability_units", CLValueBuilder.u256(40000))
                                .add("liability_holders", CLValueBuilder.map(clMap))
                                .build()),
                casperSdk.standardPayment(new BigInteger("10000000000"))
        );
    }


    public Digest installContract() throws FileNotFoundException {

        final InputStream erc20wasmIn = getWasmIn("/com/casper/sdk/how_to/erc20.wasm");
        final String chainName = "casper-net-1";
        final Number payment = 50e9;
        final int tokenDecimals = 11;
        final String tokenName = "Acme Token";
        final Number tokenTotalSupply = 1e15;
        final String tokenSymbol = "ACME";

        // Get contract operator.
        final KeyPairStreams faucetKeyPair = getFaucetKeyPair();
        final KeyPair operatorKeyPair = casperSdk.loadKeyPair(faucetKeyPair.getPublicKeyIn(), faucetKeyPair.getPrivateKeyIn());

        // Set deploy.
        final Deploy deploy = casperSdk.makeInstallContract(
                new DeployParams(
                        operatorKeyPair.getPublic(),
                        chainName,
                        null,
                        null,
                        null,
                        null
                ),
                payment,
                erc20wasmIn,
                tokenDecimals,
                tokenName,
                tokenSymbol,
                tokenTotalSupply
        );

        // Approve deploy.
        casperSdk.signDeploy(deploy, operatorKeyPair);

        // Dispatch deploy to a node.
        return casperSdk.putDeploy(deploy);

    }
}
