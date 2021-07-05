package com.casper.sdk.controller;

import com.casper.sdk.domain.*;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Casper SDK integration tests. The NCTL test nodes must be running for these tests to execute.
 */
class CasperSdkIntegrationTest {

    private final Logger logger = LoggerFactory.getLogger(CasperSdkIntegrationTest.class);

    /** Path the nctl folder can be overridden with -Dnctl.home=some-path */
    private final String NCTL_HOME = "~/Documents/casper/casper-node/utils/nctl";
    /** The SDK under test the NCTL test nodes must be running for these tests to execute */
    private CasperSdk casperSdk;

    @BeforeEach
    void setUp() {
        casperSdk = new CasperSdk("http://localhost", 11101);
    }

    @Test
    void getAccountInfo() throws Throwable {
        final String accountInfo = casperSdk.getAccountInfo(getPublicKeyAccountHex(geUserKeyPair(1)));
        assertThat(accountInfo, is(notNullValue()));
    }

    @Test
    void getAccountHash() throws Throwable {
        final String accountHash = casperSdk.getAccountHash(getPublicKeyAccountHex(geUserKeyPair(1)));
        assertThat(accountHash, is(notNullValue()));
    }

    @Test
    void getAccountBalance() throws Throwable {
        final String accountBalance = casperSdk.getAccountBalance(getPublicKeyAccountHex(geUserKeyPair(1)));
        assertThat(accountBalance, is(notNullValue()));
    }

    @Test
    void getAccountMainPurseURef() throws Throwable {
        final String accountMainPurseURef = casperSdk.getAccountMainPurseURef(getPublicKeyAccountHex(geUserKeyPair(1)));
        assertThat(accountMainPurseURef, is(notNullValue()));
    }

    @Test
    void getStateRootHash() throws Throwable {
        final String rootHash = casperSdk.getStateRootHash();
        assertThat(rootHash, is(notNullValue()));
    }

    @Test
    void getAuctionInfo() throws Throwable {
        final String auctionInfo = casperSdk.getAuctionInfo();
        assertThat(auctionInfo, is(notNullValue()));
    }

    @Test
    void getNodeStatus() throws Throwable {
        final String nodeStatus = casperSdk.getNodeStatus();
        assertThat(nodeStatus, is(notNullValue()));
    }

    @Test
    void getNodePeers() throws Throwable {
        final String nodePeers = casperSdk.getNodePeers();
        assertThat(nodePeers, is(notNullValue()));
    }

    @Test
    void putDeploy() throws Throwable {

        final AsymmetricCipherKeyPair userOneKeyPair = geUserKeyPair(1);
        final AsymmetricCipherKeyPair userTwoKeyPair = geUserKeyPair(2);

        final AsymmetricCipherKeyPair nodeOneKeyPair = getNodeKeyPair(1);

        final PublicKey fromPublicKey = new PublicKey(((Ed25519PublicKeyParameters) nodeOneKeyPair.getPublic()).getEncoded(), KeyAlgorithm.ED25519);
        final PublicKey toPublicKey = new PublicKey(((Ed25519PublicKeyParameters) userTwoKeyPair.getPublic()).getEncoded(), KeyAlgorithm.ED25519);

        // Make the session, a transfer from user one to user two
        final Transfer transfer = casperSdk.newTransfer(new BigInteger("2500000000"),
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

        final Deploy signedDeploy = casperSdk.signDeploy(deploy, nodeOneKeyPair);
        String json = casperSdk.deployToJson(signedDeploy);

        System.out.println(json);
        logger.info(json);

        final Digest digest = casperSdk.putDeploy(signedDeploy);

        assertThat(digest, is(notNullValue()));
    }

    private String getPublicKeyAccountHex(final AsymmetricCipherKeyPair keyPair) {
        final PublicKey publicKey = new PublicKey(((Ed25519PublicKeyParameters) keyPair.getPublic()).getEncoded(), KeyAlgorithm.ED25519);
        return publicKey.toAccountHex();
    }


    private AsymmetricCipherKeyPair geUserKeyPair(int userNumber) throws IOException {

        final String userNPath = String.format("%s/assets/net-1/users/user-%d", getNctlHome(), userNumber);
        final FileInputStream publicKeyIn = new FileInputStream(new File(userNPath, "public_key.pem"));
        final FileInputStream privateKeyIn = new FileInputStream(new File(userNPath, "secret_key.pem"));

        return casperSdk.loadKeyPair(publicKeyIn, privateKeyIn);
    }


    private AsymmetricCipherKeyPair getNodeKeyPair(int nodeNumber) throws IOException {

        final String userNPath = String.format("%s/assets/net-1/nodes/node-%d/keys", getNctlHome(), nodeNumber);
        final FileInputStream publicKeyIn = new FileInputStream(new File(userNPath, "public_key.pem"));
        final FileInputStream privateKeyIn = new FileInputStream(new File(userNPath, "secret_key.pem"));

        return casperSdk.loadKeyPair(publicKeyIn, privateKeyIn);
    }

    private String getNctlHome() {
        String nctlHome = System.getProperty("nctl.home");
        if (nctlHome == null) {
            nctlHome = NCTL_HOME;
        }

        // Replace user home '~' tilda with full path to user home directory
        nctlHome = nctlHome.replaceFirst("^~", System.getProperty("user.home"));

        return nctlHome;
    }
}