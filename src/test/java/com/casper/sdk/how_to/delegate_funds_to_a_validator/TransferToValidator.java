package com.casper.sdk.how_to.delegate_funds_to_a_validator;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.KeyPairStreams;
import com.casper.sdk.how_to.common.Methods;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class TransferToValidator extends Methods {

    //Path to the NCTL utilities, change to mach your implementation
    private final static String NCTL_HOME = "~/casper-node/utils/nctl";
    //Create new instance of the SDK with default NCTL url and port
    final CasperSdk casperSdk = new CasperSdk("http://localhost", 40101);

    @Test
    public void testTransferToValidator() throws IOException {

        final KeyPairStreams nodeKeyOneStream = super.getNodeKeyPair(1, NCTL_HOME);
        final KeyPair delegator = casperSdk.loadKeyPair(nodeKeyOneStream.getPublicKeyIn(), nodeKeyOneStream.getPrivateKeyIn());

        final KeyPairStreams nodeKeyTwoStream = super.getUserKeyPairStreams(2, NCTL_HOME);
        final KeyPair validator = casperSdk.loadKeyPair(nodeKeyTwoStream.getPublicKeyIn(), nodeKeyTwoStream.getPrivateKeyIn());

        final CLPublicKey toPublicKey = casperSdk.toCLPublicKey(validator.getPublic());

        // Make the session, a transfer from user one to user two
        final com.casper.sdk.types.Transfer transfer = casperSdk.newTransfer(
                new BigInteger("2500000000"),
                toPublicKey,
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
