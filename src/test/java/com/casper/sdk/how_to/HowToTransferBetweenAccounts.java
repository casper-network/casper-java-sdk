package com.casper.sdk.how_to;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.KeyPairStreams;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.KeyPair;
import java.time.Instant;

import static com.casper.sdk.how_to.HowToUtils.getNodeKeyPair;
import static com.casper.sdk.how_to.HowToUtils.getUserKeyPairStreams;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Uses Local Network Testing network/node control to demonstrate transferring between accounts
 *
 * @see <a href="https://docs.casperlabs.io/en/latest/dapp-dev-guide/setup-nctl.html"></a>
 */
@Disabled // Remove this to run locally
public class HowToTransferBetweenAccounts {

    /** Create new instance of the SDK with default NCTL url and port */
    final CasperSdk casperSdk = new CasperSdk("http://localhost", 40101);

    @Test
    public void testDeploy() throws Throwable {

        final KeyPairStreams nodeKeyOneStream = getNodeKeyPair(1);
        final KeyPair nodeOneKeyPair = casperSdk.loadKeyPair(
                nodeKeyOneStream.getPublicKeyIn(), nodeKeyOneStream.getPrivateKeyIn()
        );

        final KeyPairStreams nodeKeyTwoStream = getUserKeyPairStreams(2);
        final KeyPair nodeTwoKeyPair = casperSdk.loadKeyPair(
                nodeKeyTwoStream.getPublicKeyIn(), nodeKeyTwoStream.getPrivateKeyIn()
        );

        // Make the session, a transfer from user one to user two
        final Transfer transfer = casperSdk.newTransfer(
                new BigInteger("2500000000"),
                nodeTwoKeyPair.getPublic(),
                1
        );

        // Make a payment
        final ModuleBytes payment = casperSdk.standardPayment(new BigInteger("10000000000"));

        // Create the transfer
        final Deploy deploy = casperSdk.makeTransferDeploy(
                new DeployParams(
                        nodeOneKeyPair.getPublic(),
                        "casper-net-1",
                        10,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null
                ),
                transfer,
                payment
        );

        casperSdk.signDeploy(deploy, nodeOneKeyPair);
        casperSdk.signDeploy(deploy, nodeTwoKeyPair);

        final String json = casperSdk.deployToJson(deploy);
        assertThat(json, is(notNullValue()));

        final Digest digest = casperSdk.putDeploy(deploy);
        assertThat(digest, is(notNullValue()));
    }
}
