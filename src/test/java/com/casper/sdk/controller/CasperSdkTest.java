package com.casper.sdk.controller;

import com.casper.sdk.domain.*;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

class CasperSdkTest {

    private final static String URL = "http://3.140.179.157";
    private final static String PORT = "7777";

    private CasperSdk casperSdk = new CasperSdk(URL, PORT);

    /**
     * Tests the SDK can create a transfer domain object
     */
    @Test
    void makeTransfer() {

        final Deploy deploy = casperSdk.makeTransferDeploy(
                new DeployParams(
                        new PublicKey("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"),
                        "mainnet",
                        1,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null),
                DeployUtil.makeTransfer(new BigInteger("24500000000"),
                        new PublicKey("0101010101010101010101010101010101010101010101010101010101010101"),
                        new BigInteger("999")),
                DeployUtil.standardPayment(new BigInteger("1000000000"))
        );

        assertThat(deploy, is(notNullValue()));

        assertThat(deploy.isTransfer(), is(true));

        final Transfer transfer =  deploy.getSession();
        assertThat(transfer.getNamedArg("amount").getValue().getParsed(), is("24500000000"));
        assertThat(transfer.getNamedArg("amount").getValue().getCLType(), is(CLType.U512));
    }
}