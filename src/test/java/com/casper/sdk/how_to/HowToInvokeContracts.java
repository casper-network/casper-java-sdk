package com.casper.sdk.how_to;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.KeyPairStreams;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.DeployParams;
import com.casper.sdk.types.Digest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.security.KeyPair;

import static com.casper.sdk.how_to.HowToUtils.getFaucetKeyPair;
import static com.casper.sdk.how_to.HowToUtils.getUserKeyPairStreams;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

/**
 * Integration tests for invoking a contract
 */
@Disabled // Remove this to run locally
public class HowToInvokeContracts {

    /**
     * Test that gives an example of using a
     */
    @Test
    void howToInvokeAContract() throws Throwable {

        // Step 1: Set casper node client.
        final CasperSdk casperSdk = new CasperSdk("http://localhost", 11101);

        // Step 2: Set contract operator key pair.
        final KeyPair recipientKeyPair = geUserKeyPair(casperSdk, 2);

        final String chainName = "casper-net-1";
        final Number payment = 1e9;
        final Number amount = 2e9;

        // Get contract operator.
        final KeyPairStreams faucetKeyPair = getFaucetKeyPair();
        final KeyPair operatorKeyPair = casperSdk.loadKeyPair(faucetKeyPair.getPublicKeyIn(), faucetKeyPair.getPrivateKeyIn());

        // Make deploy
        Deploy deploy = casperSdk.makeInvokeContract(
                new DeployParams(
                        operatorKeyPair.getPublic(),
                        chainName,
                        null,
                        null,
                        null,
                        null
                ),
                payment,
                amount,
                operatorKeyPair.getPublic(),
                recipientKeyPair.getPublic()
        );

        // Sign deploy.
        deploy = casperSdk.signDeploy(deploy, operatorKeyPair);

        // Dispatch deploy to a node.
        final Digest digest = casperSdk.putDeploy(deploy);

        // Assert Deploy dispatched to node
        assertThat(digest, is(notNullValue()));
    }

    private KeyPair geUserKeyPair(final CasperSdk casperSdk, final int userNumber) throws IOException {
        final KeyPairStreams streams = getUserKeyPairStreams(userNumber);
        return casperSdk.loadKeyPair(streams.getPublicKeyIn(), streams.getPrivateKeyIn());
    }
}
