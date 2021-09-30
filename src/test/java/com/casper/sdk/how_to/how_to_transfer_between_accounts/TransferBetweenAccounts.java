package com.casper.sdk.how_to.how_to_transfer_between_accounts;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.how_to.common.Methods;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.security.KeyPair;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Uses Local Network Testing network/node control to demonstrate transferring between accounts
 *
 * @see <a href="https://docs.casperlabs.io/en/latest/dapp-dev-guide/setup-nctl.html"></a>
 */
@Disabled // Remove this to run locally
public class TransferBetweenAccounts extends Methods {

    //Path to the NCTL utilities, change to mach your implementation
    private final static String NCTL_HOME = "~/casper-node/utils/nctl";
    //Create new instance of the SDK with default NCTL url and port
    final CasperSdk casperSdk = new CasperSdk("http://localhost", 40101);

    @Test
    public void testDeploy() throws Throwable {

        final KeyPair nodeOneKeyPair = super.getNodeKeyPair(1, NCTL_HOME, casperSdk);
        final KeyPair nodeTwoKeyPair = super.getUserKeyPair(2, NCTL_HOME, casperSdk);

        final PublicKey fromPublicKey = new PublicKey(nodeOneKeyPair.getPublic().getEncoded(), KeyAlgorithm.ED25519);
        final PublicKey toPublicKey = new PublicKey(nodeTwoKeyPair.getPublic().getEncoded(), KeyAlgorithm.ED25519);

        // Make the session, a transfer from user one to user two
        final com.casper.sdk.types.Transfer transfer = casperSdk.newTransfer(new BigInteger("2500000000"),
                toPublicKey,
                1);

        // Make a payment
        final ModuleBytes payment = casperSdk.standardPayment(new BigInteger("10000000000"));

        // Create the transfer
        final Deploy deploy = casperSdk.makeTransferDeploy(
                new DeployParams(
                        fromPublicKey,
                        "casper-net-1",
                        10,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null),
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
