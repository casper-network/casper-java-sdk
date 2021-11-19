package com.casper.sdk.how_to;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.KeyPairStreams;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.DeployParams;
import com.casper.sdk.types.URef;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.security.KeyPair;
import java.security.PublicKey;

import static com.casper.sdk.how_to.HowToUtils.getNctlHome;
import static com.casper.sdk.how_to.HowToUtils.getNodeKeyPair;

@Disabled // Remove this comment to test against a network
public class HowToUnstake {

    /** Create new instance of the SDK with default NCTL url and port */
    final CasperSdk casperSdk = new CasperSdk("http://localhost", 40101);

    @Test
    void howToUnstake() throws Exception {

        final String pathToWasm = getNctlHome() + "/assets/net-1/bin/auction/withdraw_bid.wasm";
        final KeyPairStreams validatorKeyPairSteam = getNodeKeyPair(1);

        // Set validator key.
        final KeyPair validatorKeyPair = casperSdk.loadKeyPair(validatorKeyPairSteam.getPublicKeyIn(), validatorKeyPairSteam.getPrivateKeyIn());

        final DeployParams deployParams = null;
        final Number amount = null;
        final PublicKey validatorPublicKey = validatorKeyPair.getPublic();
        final InputStream wasmIn = getClass().getResourceAsStream(pathToWasm);

        // Set validator unbond purse.
        final URef unbondPurse = casperSdk.getAccountMainPurseURef(validatorPublicKey);

        // Set deploy.
        final Deploy deploy = casperSdk.makeValidatorAuctionBidWithdrawal(
                deployParams,
                amount,
                validatorPublicKey,
                wasmIn,
                unbondPurse
        );

        // Approve deploy.
        casperSdk.signDeploy(deploy, validatorKeyPair);

        // Dispatch deploy.
        casperSdk.putDeploy(deploy);
    }
}
