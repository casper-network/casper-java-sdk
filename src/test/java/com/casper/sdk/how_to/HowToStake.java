package com.casper.sdk.how_to;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.KeyPairStreams;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.DeployParams;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.security.KeyPair;

import static com.casper.sdk.how_to.HowToUtils.*;

/**
 * Example of how to stake
 */
@Disabled // Remove this comment to test against a network
public class HowToStake {

    /** Create new instance of the SDK with default NCTL url and port */
    final CasperSdk casperSdk = new CasperSdk("http://localhost", 40101);

    @Test
    void howToStake() throws Exception {

        final Number amount = 2.5e9;
        final int delegationRate = 2;
        final String chainName = "casper-net-1";
        final String pathToContract = getNctlHome() + "/assets/net-1/bin/auction/add_bid.wasm";
        final InputStream wasmIn = getWasmIn(pathToContract);

        // Set validator key.
        final KeyPairStreams nodeKeyPair = getNodeKeyPairStreams(1);
        final KeyPair validatorKeyPair = casperSdk.loadKeyPair(nodeKeyPair.getPublicKeyIn(), nodeKeyPair.getPrivateKeyIn());

        final DeployParams deployParams = new DeployParams(
                validatorKeyPair.getPublic(),
                chainName,
                null,
                null,
                null,
                null
        );

        // Set deploy
        Deploy deploy = casperSdk.makeValidatorAuctionBid(
                deployParams,
                amount,
                delegationRate,
                validatorKeyPair.getPublic(),
                wasmIn
        );

        // Approve Deploy
        deploy = casperSdk.signDeploy(deploy, validatorKeyPair);

        // Dispatch deploy to a node.
        casperSdk.putDeploy(deploy);
    }
}
