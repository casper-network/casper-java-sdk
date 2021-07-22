package com.casper.sdk.controller;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.types.*;
import com.casper.sdk.service.HashService;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
@Disabled // TODO Remove this comment to tests against a network
class CasperSdkIntegrationTest {

    private final Logger logger = LoggerFactory.getLogger(CasperSdkIntegrationTest.class);

    private final byte[] expectedSerializedBody = {
            (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 6,
            (byte) 0, (byte) 0, (byte) 0, (byte) 97, (byte) 109, (byte) 111, (byte) 117, (byte) 110, (byte) 116,
            (byte) 6, (byte) 0, (byte) 0, (byte) 0, (byte) 5, (byte) 0, (byte) 228, (byte) 11, (byte) 84, (byte) 2,
            (byte) 8, (byte) 5, (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 6, (byte) 0, (byte) 0, (byte) 0,
            (byte) 97, (byte) 109, (byte) 111, (byte) 117, (byte) 110, (byte) 116, (byte) 5, (byte) 0, (byte) 0,
            (byte) 0, (byte) 4, (byte) 0, (byte) 249, (byte) 2, (byte) 149, (byte) 8, (byte) 6, (byte) 0, (byte) 0,
            (byte) 0, (byte) 116, (byte) 97, (byte) 114, (byte) 103, (byte) 101, (byte) 116, (byte) 32, (byte) 0,
            (byte) 0, (byte) 0, (byte) 79, (byte) 71, (byte) 253, (byte) 195, (byte) 15, (byte) 31, (byte) 250,
            (byte) 148, (byte) 88, (byte) 79, (byte) 11, (byte) 110, (byte) 101, (byte) 160, (byte) 243, (byte) 30,
            (byte) 192, (byte) 170, (byte) 103, (byte) 162, (byte) 47, (byte) 209, (byte) 194, (byte) 240,
            (byte) 59, (byte) 142, (byte) 8, (byte) 119, (byte) 170, (byte) 73, (byte) 170, (byte) 183, (byte) 15,
            (byte) 32, (byte) 0, (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 105, (byte) 100,
            (byte) 9, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
            (byte) 0, (byte) 0, (byte) 0, (byte) 13, (byte) 5
    };

    /** Path the nctl folder can be overridden with -Dnctl.home=some-path */
    private final String NCTL_HOME = "~/Documents/casper/casper-node/utils/nctl";

    private byte[] expectedHash = new byte[]{
            (byte) 10, (byte) 56, (byte) 192, (byte) 58, (byte) 52, (byte) 29, (byte) 185, (byte) 218, (byte) 206,
            (byte) 17, (byte) 135, (byte) 227, (byte) 27, (byte) 127, (byte) 172, (byte) 32, (byte) 150, (byte) 155,
            (byte) 135, (byte) 250, (byte) 107, (byte) 113, (byte) 237, (byte) 101, (byte) 127, (byte) 51,
            (byte) 144, (byte) 81, (byte) 19, (byte) 196, (byte) 35, (byte) 39
    };
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

        //   assertThat(deploy.getHash().getHash(), is(expectedHash));

        casperSdk.signDeploy(deploy, nodeOneKeyPair);
        casperSdk.signDeploy(deploy, userTwoKeyPair);

        final String json = casperSdk.deployToJson(deploy);

        logger.info(json);

        final Digest digest = casperSdk.putDeploy(deploy);

        assertThat(digest, is(notNullValue()));
    }


    @Test
    void hashBody() {
        final byte[] accountHash = new HashService().getHash(expectedSerializedBody);
        assertThat(accountHash, is(expectedHash));
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