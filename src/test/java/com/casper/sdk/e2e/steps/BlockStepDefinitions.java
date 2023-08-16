package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.*;
import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.helper.CasperTransferHelper;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.block.HeightBlockIdentifier;
import com.casper.sdk.model.block.JsonBlockData;
import com.casper.sdk.model.block.JsonProof;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;
import com.casper.sdk.model.event.blockadded.BlockAdded;
import com.casper.sdk.model.event.step.Step;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transfer.TransferData;
import com.casper.sdk.service.CasperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.casper.sdk.e2e.event.EventHandler;
import com.casper.sdk.e2e.matcher.EraMatcher;
import com.casper.sdk.e2e.matcher.ExpiringMatcher;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import com.syntifi.crypto.key.Ed25519PublicKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.casper.sdk.e2e.matcher.BlockAddedMatchers.hasTransferHashWithin;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Step Definitions for Block Cucumber Tests.
 */
public class BlockStepDefinitions {

    private final Logger logger = LoggerFactory.getLogger(BlockStepDefinitions.class);
    private static final String invalidBlockHash = "2fe9630b7790852e4409d815b04ca98f37effcdf9097d317b9b9b8ad658f47c8";
    private static final long invalidHeight = 9999999999L;
    private static final String blockErrorMsg = "No such block";
    private static final String blockErrorCode = "-32001";
    private final ContextMap contextMap = ContextMap.getInstance();
    private static EventHandler blockEventHandler;
    private static EventHandler eraEventHandler;
    private final ObjectMapper mapper = new ObjectMapper();
    private final TestProperties testProperties = new TestProperties();
    private final Nctl nctl = new Nctl(testProperties.getDockerName());

    @BeforeAll
    public static void setUp() {
        blockEventHandler = new EventHandler(EventTarget.POJO);
        eraEventHandler = new EventHandler(EventTarget.RAW);
        ContextMap.getInstance().clear();
    }

    @SuppressWarnings("unused")
    @AfterAll
    void tearDown() {
        blockEventHandler.close();
        eraEventHandler.close();
    }

    private static CasperService getCasperService() {
        return CasperClientProvider.getInstance().getCasperService();
    }


    @Given("that the latest block is requested")
    public void thatTheLatestBlockIsRequested() {

        logger.info("Given that the latest block is requested");

        contextMap.put("blockDataSdk", getCasperService().getBlock());
    }

    @Then("a valid error message is returned")
    public void aValidErrorMessageIsReturned() {

        logger.info("Then a valid error message is returned");

        final CasperClientException csprClientException = contextMap.get("csprClientException");

        assertThat(csprClientException.getMessage(), is(notNullValue()));
        assertThat(csprClientException.getMessage(), containsStringIgnoringCase(blockErrorMsg));
        assertThat(csprClientException.getMessage(), containsStringIgnoringCase(blockErrorCode));
    }


    private void validateBlockHash(final Digest hash) {
        assertThat(hash, is(notNullValue()));
        assertThat(hash.getDigest(), is(notNullValue()));
        assertThat(hash.getClass(), is(Digest.class));
        assertThat(hash.isValid(), is(true));
    }

    @Then("the deploy response contains a valid deploy hash")
    public void theDeployResponseContainsAValidDeployHash() {

        logger.info("Then the deploy response contains a valid deploy hash");

        final DeployResult deployResult = contextMap.get("deployResult");
        assertThat(deployResult, is(notNullValue()));
        assertThat(deployResult.getDeployHash(), is(notNullValue()));

    }

    @Then("request the block transfer")
    public void requestTheBlockTransfer() throws Exception {

        logger.info("Then request the block transfer");

        final DeployResult deployResult = contextMap.get("deployResult");

        final ExpiringMatcher<Event<BlockAdded>> matcher = (ExpiringMatcher<Event<BlockAdded>>) blockEventHandler.addEventMatcher(
                EventType.MAIN,
                hasTransferHashWithin(
                        deployResult.getDeployHash(),
                        blockAddedEvent -> contextMap.put("matchingBlock", blockAddedEvent.getData())
                )
        );

        assertThat(matcher.waitForMatch(300), is(true));
        blockEventHandler.removeEventMatcher(EventType.MAIN, matcher);

        contextMap.put("transferBlockSdk", getCasperService().getBlockTransfers());

    }

    @Then("request the latest block via the test node")
    public void requestTheLatestBlockViaTheTestNode() {
        logger.info("Then request the latest block via the test node");
        contextMap.put("blockDataNode", nctl.getChainBlock(""));
    }

    @Then("request a block by hash via the test node")
    public void requestABlockByHashViaTheTestNode() {
        logger.info("Then request a block by hash via the test node");

        contextMap.put("blockDataNode", nctl.getChainBlock(contextMap.get("latestBlock")));
    }

    @Then("request the returned block from the test node via its hash")
    public void requestTheReturnedBlockFromTheTestNodeViaItsHash() {
        logger.info("Then request the returned block from the test node via its hash");

        //NCTL doesn't have get block via height, so we use the sdk's returned block has
        contextMap.put("blockDataNode", nctl.getChainBlock(contextMap.get("blockHashSdk")));
    }

    @Given("that a test node era switch block is requested")
    public void thatATestNodeEraSwitchBlockIsRequested() {

        logger.info("Given that a test node era switch block is requested");

        contextMap.put("nodeEraSwitchBlockResult", nctl.getChainEraInfo());
    }

    @Then("wait for the the test node era switch block step event")
    public void waitForTheTheTestNodeEraSwitchBlock() throws Exception {
        logger.info("Then wait for the test node era switch block step event");

        final ExpiringMatcher<Event<Step>> matcher = (ExpiringMatcher<Event<Step>>) eraEventHandler.addEventMatcher(
                EventType.MAIN,
                EraMatcher.theEraHasChanged()
        );

        assertThat(matcher.waitForMatch(5000L), is(true));

        final JsonNode result = nctl.getChainEraInfo();

        eraEventHandler.removeEventMatcher(EventType.MAIN, matcher);

        assertThat(result.get("era_summary").get("block_hash").textValue(), is(notNullValue()));
        validateBlockHash(new Digest(result.get("era_summary").get("block_hash").textValue()));

        contextMap.put("nodeEraSwitchBlock", result.get("era_summary").get("block_hash").textValue());
        contextMap.put("nodeEraSwitchData", result);
    }

    @Then("request the block transfer from the test node")
    public void requestTheBlockTransferFromTheTestNode() {

        logger.info("Then request the block transfer from the test node");

        final TransferData transferData = contextMap.get("transferBlockSdk");
        contextMap.put("transferBlockNode", nctl.getChainBlockTransfers(transferData.getBlockHash()));
    }

    @Then("the body of the returned block is equal to the body of the returned test node block")
    public void theBodyOfTheReturnedBlockIsEqualToTheBodyOfTheReturnedTestNodeBlock() throws JsonProcessingException {

        logger.info("Then the body of the returned block is equal to the body of the returned test node block");

        JsonBlockData latestBlockSdk = contextMap.get("blockDataSdk");
        final JsonNode latestBlockNode = mapper.readTree(contextMap.get("blockDataNode").toString());

        if (!latestBlockSdk.getBlock().getHash().toString().equals(latestBlockNode.get("hash").asText())) {
            //Fixes intermittent syncing issues with nctl/sdk latest blocks
            latestBlockSdk = getCasperService().getBlock();
            contextMap.put("blockDataSdk", latestBlockSdk);
        }

        assertThat(latestBlockSdk.getBlock().getBody(), is(notNullValue()));
        assertThat(latestBlockSdk.getBlock().getBody().getProposer().toString(), is(latestBlockNode.get("body").get("proposer").asText()));

        if (latestBlockNode.get("body").get("deploy_hashes").size() == 0) {
            assertThat(latestBlockSdk.getBlock().getBody().getDeployHashes(), is(empty()));
        } else {
            final JsonBlockData deploysLatestBlockSdk = latestBlockSdk;
            latestBlockNode.get("body").findValues("deploy_hashes").forEach(
                    d -> assertThat(deploysLatestBlockSdk.getBlock().getBody().getDeployHashes(), hasItem(d.textValue()))
            );
        }
        if (latestBlockNode.get("body").get("transfer_hashes").size() == 0) {
            assertThat(latestBlockSdk.getBlock().getBody().getTransferHashes(), is(empty()));
        } else {
            final JsonBlockData transfersLatestBlockSdk = latestBlockSdk;
            latestBlockNode.get("body").findValues("transfer_hashes").forEach(
                    t -> assertThat(transfersLatestBlockSdk.getBlock().getBody().getTransferHashes(), hasItem(t.textValue()))
            );
        }

    }

    @And("the hash of the returned block is equal to the hash of the returned test node block")
    public void theHashOfTheReturnedBlockIsEqualToTheHashOfTheReturnedTestNodeBlock() throws JsonProcessingException {
        logger.info("And the hash of the returned block is equal to the hash of the returned test node block");

        final JsonBlockData latestBlockSdk = contextMap.get("blockDataSdk");
        final JsonNode latestBlockNode = mapper.readTree(contextMap.get("blockDataNode").toString());

        assertThat(latestBlockSdk.getBlock().getHash().toString(), is(latestBlockNode.get("hash").asText()));
    }

    @And("the header of the returned block is equal to the header of the returned test node block")
    public void theHeaderOfTheReturnedBlockIsEqualToTheHeaderOfTheReturnedTestNodeBlock() throws JsonProcessingException {
        logger.info("And the header of the returned block is equal to the header of the returned test node block");

        final JsonBlockData latestBlockSdk = contextMap.get("blockDataSdk");
        final JsonNode latestBlockNode = mapper.readTree(contextMap.get("blockDataNode").toString());

        assertThat(latestBlockSdk.getBlock().getHeader().getEraId(), is(latestBlockNode.get("header").get("era_id").asLong()));
        assertThat(latestBlockSdk.getBlock().getHeader().getHeight(), is(latestBlockNode.get("header").get("height").asLong()));
        assertThat(latestBlockSdk.getBlock().getHeader().getProtocolVersion(), is(latestBlockNode.get("header").get("protocol_version").asText()));

        assertThat(latestBlockSdk.getBlock().getHeader().getAccumulatedSeed(),
                is(new Digest(latestBlockNode.get("header").get("accumulated_seed").asText())));
        assertThat(latestBlockSdk.getBlock().getHeader().getBodyHash(),
                is(new Digest(latestBlockNode.get("header").get("body_hash").asText())));
        assertThat(latestBlockSdk.getBlock().getHeader().getStateRootHash(),
                is(new Digest(latestBlockNode.get("header").get("state_root_hash").asText())));
        assertThat(latestBlockSdk.getBlock().getHeader().getTimeStamp(),
                is(new DateTime(latestBlockNode.get("header").get("timestamp").asText()).toDate()));
    }

    @And("the proofs of the returned block are equal to the proofs of the returned test node block")
    public void theProofsOfTheReturnedBlockAreEqualToTheProofsOfTheReturnedTestNodeBlock() throws JsonProcessingException {

        logger.info("And the proofs of the returned block are equal to the proofs of the returned test node block");

        final JsonBlockData latestBlockSdk = contextMap.get("blockDataSdk");
        final JsonNode latestBlockNode = mapper.readTree(contextMap.get("blockDataNode").toString());

        final List<JsonProof> proofsSdk = latestBlockSdk.getBlock().getProofs();
        assertThat(latestBlockNode.get("proofs").findValues("public_key").size(), is(proofsSdk.size()));

        latestBlockNode.get("proofs").findValues("public_key").forEach(
                p -> assertThat((int) proofsSdk.stream().filter(q -> p.asText().equals(q.getPublicKey().toString())).count(), is(1))
        );

        latestBlockNode.get("proofs").findValues("signature").forEach(
                p -> assertThat((int) proofsSdk.stream().filter(q -> p.asText().equals(q.getSignature().toString())).count(), is(1))
        );
    }

    @Given("that chain transfer data is initialised")
    public void thatChainTransferDataIsInitialised() throws IOException {
        logger.info("Given that chain transfer data is initialised");

        final Ed25519PrivateKey senderKey = new Ed25519PrivateKey();
        final Ed25519PublicKey receiverKey = new Ed25519PublicKey();

        senderKey.readPrivateKey(AssetUtils.getUserKeyAsset(1, 1, "secret_key.pem").getFile());
        receiverKey.readPublicKey(AssetUtils.getUserKeyAsset(1, 2, "public_key.pem").getFile());

        contextMap.put("senderKey", senderKey);
        contextMap.put("receiverKey", receiverKey);
        contextMap.put("transferAmount", BigInteger.valueOf(2500000000L));
        contextMap.put("gasPrice", 1L);
        contextMap.put("ttl", Ttl.builder().ttl(30 + "m").build());
    }

    @When("the deploy data is put on chain")
    public void theDeployDataIsPutOnChain() throws NoSuchTypeException, GeneralSecurityException, ValueSerializationException {
        logger.info("When the deploy data is put on chain");

        final Deploy deploy = CasperTransferHelper.buildTransferDeploy(
                contextMap.get("senderKey"),
                PublicKey.fromAbstractPublicKey(contextMap.get("receiverKey")),
                contextMap.get("transferAmount"),
                "casper-net-1",
                Math.abs(new Random().nextLong()),
                BigInteger.valueOf(100000000L),
                contextMap.get("gasPrice"),
                contextMap.get("ttl"),
                new Date(),
                new ArrayList<>());

        final CasperService casperService = CasperClientProvider.getInstance().getCasperService();

        contextMap.put("deployResult", casperService.putDeploy(deploy));
    }

    @And("the returned block contains the transfer hash returned from the test node block")
    public void theReturnedBlockContainsTheTransferHashReturnedFromTheTestNodeBlock() throws JsonProcessingException {

        logger.info("And the returned block contains the transfer hash returned from the test node block");

        final DeployResult deployResult = contextMap.get("deployResult");
        final List<String> transferHashes = new ArrayList<>();

        mapper.readTree(contextMap.get("transferBlockNode").toString()).get("body").get("transfer_hashes").forEach(
                t -> {
                    if (t.textValue().equals(deployResult.getDeployHash())) {
                        transferHashes.add(t.textValue());
                    }
                }
        );

        assertThat(transferHashes.size(), is(greaterThan(0)));
    }

    @Given("that the latest block is requested via the sdk")
    public void thatTheLatestBlockIsRequestedViaTheSdk() {

        logger.info("Given that the latest block is requested via the sdk");

        contextMap.put("blockDataSdk", getCasperService().getBlock());
        contextMap.put("blockHashSdk", getCasperService().getBlock().getBlock().getHash().toString());
    }

    @Given("that a block is returned by hash via the sdk")
    public void thatABlockIsReturnedByHashViaTheSdk() {
        logger.info("Given that a block is returned by hash via the sdk");

        contextMap.put("latestBlock", getCasperService().getBlock().getBlock().getHash().toString());
        contextMap.put("blockDataSdk", getCasperService().getBlock(new HashBlockIdentifier(contextMap.get("latestBlock"))));
    }

    @Given("that a block is returned by height {int} via the sdk")
    public void thatABlockIsReturnedByHeightViaTheSdk(int height) {
        logger.info("Given that a block is returned by height [{}] via the sdk", height);

        contextMap.put("blockDataSdk", getCasperService().getBlock(new HeightBlockIdentifier(height)));
        contextMap.put("blockHashSdk", getCasperService().getBlock(new HeightBlockIdentifier(height)).getBlock().getHash().toString());
    }

    @Given("that an invalid block hash is requested via the sdk")
    public void thatAnInvalidBlockHashIsRequestedViaTheSdk() {
        logger.info("Given that an invalid block hash is requested via the sdk");

        contextMap.put("csprClientException",
                assertThrows(CasperClientException.class,
                        () -> getCasperService().getBlock(new HashBlockIdentifier(invalidBlockHash)))
        );
    }

    @Given("that an invalid block height is requested via the sdk")
    public void thatAnInvalidBlockHeightIsRequestedViaTheSdk() {

        logger.info("Given that an invalid block height is requested");

        contextMap.put("csprClientException",
                assertThrows(CasperClientException.class,
                        () -> getCasperService().getBlock(new HeightBlockIdentifier(invalidHeight)))
        );
    }

}
