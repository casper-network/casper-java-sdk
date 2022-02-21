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
 * Integration tests for installing a contract
 */
//@Disabled // Remove this to run locally
public class HowToInstallAContract {

    /**
     * Test that gives an example of using a
     */
    @Test
    void howToInstallAContract() throws Throwable {

        // Step 1: Set casper node client.
        final CasperSdk casperSdk = new CasperSdk("http://localhost", 11101);

        //final InputStream erc20wasmIn = getWasmIn(getNctlHome() + "/assets/net-1/bin/eco/erc20.wasm");
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
        casperSdk.putDeploy(deploy);
    }
}