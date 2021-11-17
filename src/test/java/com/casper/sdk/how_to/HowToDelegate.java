package com.casper.sdk.how_to;


import com.casper.sdk.CasperSdk;
import com.casper.sdk.KeyPairStreams;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.DeployParams;
import com.casper.sdk.types.Digest;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import static com.casper.sdk.Constants.DEFAULT_GAS_PRICE;
import static com.casper.sdk.how_to.HowToUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

//@Disabled // Remove this comment to test against a network
public class HowToDelegate {

    /** Create new instance of the SDK with default NCTL url and port */
    final CasperSdk casperSdk = new CasperSdk("http://localhost", 40101);

    /**
     * This test gives an example of how to validate a delegation
     */
    @Test
    void howToDelegate() throws Exception {

        final String pathToContract = getNctlHome() + "/assets/net-1/bin/auction/delegate.wasm";
        final KeyPairStreams validatorKeyPair = getNodeKeyPair(1);
        final KeyPairStreams delegatorKeyPair = getUserKeyPairStreams(1);
        final PublicKey delegatorPublicKey = casperSdk.loadKey(delegatorKeyPair.getPublicKeyIn());
        final PrivateKey delegatorPrivateKey = casperSdk.loadKey(delegatorKeyPair.getPrivateKeyIn());

        // Create the validation delegation deploy
        Deploy deploy = casperSdk.makeValidatorDelegation(
                new DeployParams(
                        delegatorPublicKey,
                        "casper-net-1",
                        DEFAULT_GAS_PRICE,
                        null,
                        DeployParams.DEFAULT_TTL,
                        null
                ),
                1e9,
                delegatorPublicKey,
                casperSdk.loadKey(validatorKeyPair.getPublicKeyIn()),
                getWasmIn(pathToContract)
        );

        assertThat(deploy, is(notNullValue()));

        // Sign the deploy
        deploy = casperSdk.signDeploy(deploy, new KeyPair(delegatorPublicKey, delegatorPrivateKey));

        // Put the deploy
        final Digest digest = casperSdk.putDeploy(deploy);

        // Assert digest created
        assertThat(digest, is(notNullValue()));
        assertThat(digest.getHash(), is(notNullValue()));
        assertThat(digest.getHash().length, is(32));
    }

}
