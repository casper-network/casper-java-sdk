package com.casper.sdk;

import com.casper.sdk.how_to.HowToUtils;
import com.casper.sdk.service.hash.HashService;
import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.service.serialization.util.CollectionUtils;
import com.casper.sdk.types.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.time.Instant;

import static com.casper.sdk.how_to.HowToUtils.getUserKeyPairStreams;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Casper SDK integration tests. The NCTL test nodes must be running for these tests to execute.
 */
@Disabled // Remove this comment to test against a network
class CasperSdkIntegrationTest {

    /**
     * Path the nctl folder can be overridden with -Dnctl.home=some-path
     */

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
    private final byte[] expectedHash = new byte[]{
            (byte) 10, (byte) 56, (byte) 192, (byte) 58, (byte) 52, (byte) 29, (byte) 185, (byte) 218, (byte) 206,
            (byte) 17, (byte) 135, (byte) 227, (byte) 27, (byte) 127, (byte) 172, (byte) 32, (byte) 150, (byte) 155,
            (byte) 135, (byte) 250, (byte) 107, (byte) 113, (byte) 237, (byte) 101, (byte) 127, (byte) 51,
            (byte) 144, (byte) 81, (byte) 19, (byte) 196, (byte) 35, (byte) 39
    };
    /**
     * The SDK under test the NCTL test nodes must be running for these tests to execute
     */
    private CasperSdk casperSdk;

    @BeforeEach
    void setUp() {
        casperSdk = new CasperSdk("http://localhost", 11101);
    }

    @Test
    void getAccountInfo() throws Throwable {
        final String accountInfo = casperSdk.getAccountInfo(geUserKeyPair(1).getPublic());
        assertThat(accountInfo, is(notNullValue()));
    }

    @Test
    void getAccountHash() throws Throwable {
        final String accountHash = casperSdk.getAccountHash(geUserKeyPair(1).getPublic());
        assertThat(accountHash, is(notNullValue()));
    }

    @Test
    void getNodeMetrics() {
        final String metrics = casperSdk.getNodeMetrics();
        assertThat(metrics, is(notNullValue()));
    }

    @Test
    void getAccountBalance() throws Throwable {
        final BigInteger accountBalance = casperSdk.getAccountBalance(geUserKeyPair(1).getPublic());
        assertThat(accountBalance, is(notNullValue()));
    }

    @Test
    void getAccountMainPurseURef() throws Throwable {
        final URef accountMainPurseURef = casperSdk.getAccountMainPurseURef(geUserKeyPair(1).getPublic());
        assertThat(accountMainPurseURef, is(notNullValue()));
    }

    @Test
    void getStateRootHash() {
        final String rootHash = casperSdk.getStateRootHash();
        assertThat(rootHash, is(notNullValue()));
    }

    @Test
    void getAuctionInfo() {
        final String auctionInfo = casperSdk.getAuctionInfo();
        assertThat(auctionInfo, is(notNullValue()));
    }

    @Test
    void getNodeStatus() {
        final String nodeStatus = casperSdk.getNodeStatus();
        assertThat(nodeStatus, is(notNullValue()));
    }

    @Test
    void getNodePeers() {
        final String nodePeers = casperSdk.getNodePeers();
        assertThat(nodePeers, is(notNullValue()));
    }

    @Test
    void putDeploy() throws Throwable {

        final KeyPair userTwoKeyPair = geUserKeyPair(2);
        final KeyPair nodeOneKeyPair = getNodeKeyPair(1);

        // Make the session, a transfer from user one to user two
        final Transfer transfer = casperSdk.newTransfer(new BigInteger("2500000000"),
                userTwoKeyPair.getPublic(),
                1);

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
                        null),
                transfer,
                payment
        );

        casperSdk.signDeploy(deploy, nodeOneKeyPair);

        final String json = casperSdk.deployToJson(deploy);

        logger.info(json);

        final Digest digest = casperSdk.putDeploy(deploy);

        assertThat(digest, is(notNullValue()));

        final Deploy gotDeploy = casperSdk.getDeploy(digest);

        assertThat(gotDeploy, is(notNullValue()));
    }

    @Test
    void hashBody() {
        final byte[] accountHash = new HashService().getHash(expectedSerializedBody);
        assertThat(accountHash, is(expectedHash));
    }

    @Test
    void getLatestBlockInfo() {

        String blockInfo = casperSdk.getLatestBlockInfo();
        assertThat(blockInfo, is(notNullValue()));
    }

    @Test
    void getBlockInfo() throws JsonProcessingException {

        String blockInfo = casperSdk.getLatestBlockInfo();
        JsonNode jsonNode = new ObjectMapper().readTree(blockInfo);

        Digest hash = new Digest(jsonNode.get("hash").textValue());
        Number height = jsonNode.get("header").get("height").numberValue();
        blockInfo = casperSdk.getBlockInfo(hash);
        assertThat(blockInfo, is(notNullValue()));
        assertThat(blockInfo, hasJsonPath("$.hash", is(hash.toString())));

        blockInfo = casperSdk.getBlockInfoByHeight(height);
        assertThat(blockInfo, is(notNullValue()));
        assertThat(blockInfo, hasJsonPath("$.header.height", is(height)));
    }

    @Test
    void getBlockTransfers() throws JsonProcessingException {

        final String blockTransfers = casperSdk.getBlockTransfers();
        assertThat(blockTransfers, is(notNullValue()));
        assertThat(blockTransfers, hasJsonPath("$.block_hash"));
        assertThat(blockTransfers, hasJsonPath("$.transfers"));

        final JsonNode jsonNode = new ObjectMapper().readTree(blockTransfers);
        final String blockHash = jsonNode.get("block_hash").textValue();

        final String blockTransfersByHash = casperSdk.getBlockTransfers(blockHash);
        assertThat(blockTransfersByHash, hasJsonPath("$.transfers"));
    }

    private KeyPair geUserKeyPair(int userNumber) throws IOException {
        final KeyPairStreams streams = getUserKeyPairStreams(userNumber);
        return casperSdk.loadKeyPair(streams.getPublicKeyIn(), streams.getPrivateKeyIn());
    }

    private KeyPair getNodeKeyPair(final int nodeNumber) throws IOException {
        final KeyPairStreams streams = HowToUtils.getNodeKeyPairStreams(nodeNumber);
        return casperSdk.loadKeyPair(streams.getPublicKeyIn(), streams.getPrivateKeyIn());
    }
}