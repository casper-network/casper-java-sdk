package com.casper.sdk.how_to;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.KeyPairStreams;
import com.casper.sdk.types.Deploy;
import com.casper.sdk.types.DeployParams;
import com.casper.sdk.types.Digest;
import com.casper.sdk.types.ModuleBytes;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.time.Instant;

import static com.casper.sdk.how_to.HowToUtils.getNodeKeyPair;
import static com.casper.sdk.how_to.HowToUtils.getUserKeyPairStreams;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class HowToTransferToValidator {

    /** Create new instance of the SDK with default NCTL url and port */
    final CasperSdk casperSdk = new CasperSdk("http://localhost", 40101);

    @Test
    public void testTransferToValidator() throws IOException {

        final KeyPairStreams nodeKeyOneStream = getNodeKeyPair(1);
        final KeyPair delegator = casperSdk.loadKeyPair(nodeKeyOneStream.getPublicKeyIn(), nodeKeyOneStream.getPrivateKeyIn());

        final KeyPairStreams nodeKeyTwoStream = getUserKeyPairStreams(2);
        final KeyPair validator = casperSdk.loadKeyPair(nodeKeyTwoStream.getPublicKeyIn(), nodeKeyTwoStream.getPrivateKeyIn());

        // Make the session, a transfer from user one to user two
        final com.casper.sdk.types.Transfer transfer = casperSdk.newTransfer(
                new BigInteger("2500000000"),
                validator.getPublic(),
                1
        );

        // Make a payment
        final ModuleBytes payment = casperSdk.standardPayment(new BigInteger("10000000000"));

        // Create the transfer
        final Deploy deploy = casperSdk.makeTransferDeploy(
                new DeployParams(
                        delegator.getPublic(),
                        "casper-net-1",
                        10,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null),
                transfer,
                payment
        );

        casperSdk.signDeploy(deploy, delegator);
        casperSdk.signDeploy(deploy, validator);

        final String json = casperSdk.deployToJson(deploy);
        assertThat(json, is(notNullValue()));

        final Digest digest = casperSdk.putDeploy(deploy);
        assertThat(digest, is(notNullValue()));
    }
}
