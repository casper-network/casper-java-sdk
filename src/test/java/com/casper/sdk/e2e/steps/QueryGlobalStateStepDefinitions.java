package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.event.EventHandler;
import com.casper.sdk.e2e.matcher.BlockAddedMatchers;
import com.casper.sdk.e2e.matcher.ExpiringMatcher;
import com.casper.sdk.e2e.utils.*;
import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.helper.CasperTransferHelper;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.global.BlockHashIdentifier;
import com.casper.sdk.identifier.global.StateRootHashIdentifier;
import com.casper.sdk.model.block.JsonBlockData;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployInfo;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;
import com.casper.sdk.model.event.blockadded.BlockAdded;
import com.casper.sdk.model.globalstate.GlobalStateData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.stateroothash.StateRootHashData;
import com.casper.sdk.model.storedvalue.StoredValueDeployInfo;
import com.casper.sdk.service.CasperService;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import com.syntifi.crypto.key.Ed25519PublicKey;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Cucumber step definitions for query_global_state RCP method calls.
 *
 * @author ian@meywood.com
 */
public class QueryGlobalStateStepDefinitions {

    private final ContextMap contextMap = ContextMap.getInstance();
    public final CasperService casperService = CasperClientProvider.getInstance().getCasperService();
    private final Logger logger = LoggerFactory.getLogger(QueryGlobalStateStepDefinitions.class);
    private static final EventHandler eventHandler = new EventHandler(EventTarget.POJO);
    private final Nctl nctl = new Nctl(new TestProperties().getDockerName());

    @After
    public static void after() {
        eventHandler.close();
    }

    @Given("that a valid block hash is known")
    public void thatAValidBlockHashIsKnown() throws Exception {

        // Create a transfer
        createTransfer();

        // Wait for a block to be added for the transfer
        waitForBlockAdded();

        logger.info("Given that a valid block hash is known");

        assertThat(contextMap.get(StepConstants.LAST_BLOCK_ADDED), is(notNullValue()));
    }


    @When("the query_global_state RCP method is invoked with the block hash as the query identifier")
    public void theQuery_global_stateRCPMethodIsInvokedWithTheBlockHashAsTheQueryIdentifier() {

        logger.info("And the query_global_state RCP method is invoked with the block hash as the query identifier");

        final Digest blockHash = ((BlockAdded) contextMap.get(StepConstants.LAST_BLOCK_ADDED)).getBlockHash();
        final BlockHashIdentifier globalStateIdentifier = new BlockHashIdentifier(blockHash.toString());

        final DeployResult deployResult = contextMap.get(StepConstants.DEPLOY_RESULT);
        final String key = "deploy-" + deployResult.getDeployHash();

        final GlobalStateData globalStateData = casperService.queryGlobalState(globalStateIdentifier, key, new String[0]);
        assertThat(globalStateData, is(notNullValue()));

        contextMap.put(StepConstants.GLOBAL_STATE_DATA, globalStateData);
    }


    @Then("a valid query_global_state_result is returned")
    public void aValidQuery_global_state_resultIsReturned() {

        logger.info("Then a valid query_global_state_result is returned");

        final GlobalStateData globalStateData = contextMap.get(StepConstants.GLOBAL_STATE_DATA);
        assertThat(globalStateData, is(notNullValue()));
        assertThat(globalStateData.getApiVersion(), is("1.0.0"));
        assertThat(globalStateData.getMerkleProof(), is(notNullValue()));
        assertThat(globalStateData.getHeader().getTimeStamp(), is(notNullValue()));
        assertThat(globalStateData.getHeader().getEraId(), is(greaterThan(0L)));
        assertThat(globalStateData.getHeader().getAccumulatedSeed().isValid(), is(true));
        assertThat(globalStateData.getHeader().getBodyHash().isValid(), is(true));
        assertThat(globalStateData.getHeader().getParentHash().isValid(), is(true));
    }

    @And("the query_global_state_result contains a valid deploy info stored value")
    public void theQuery_global_state_resultContainsAValidStoredValue() {
        logger.info("And the query_global_state_result contains a valid deploy info stored value");
        final GlobalStateData globalStateData = contextMap.get(StepConstants.GLOBAL_STATE_DATA);
        assertThat(globalStateData.getStoredValue(), is(instanceOf(StoredValueDeployInfo.class)));
    }

    @And("the query_global_state_result's stored value from is the user-{int} account hash")
    public void theQuery_global_state_resultSStoredValueSFromIsTheUserAccountHash(int userId) {

        logger.info("And the query_global_state_result's stored value from is the user-{int} account hash");
        final String accountHash = nctl.getAccountHash(userId);
        final DeployInfo storedValueDeployInfo = getGlobalDataDataStoredValue();
        assertThat(storedValueDeployInfo.getFrom(), is(accountHash));
    }


    @And("the query_global_state_result's stored value contains a gas price of {long}")
    public void theQuery_global_state_resultSStoredValueContainsAGasPriceOf(long gasPrice) {
        logger.info("And the query_global_state_result's stored value contains a gas price of {long}");
        final DeployInfo storedValueDeployInfo = getGlobalDataDataStoredValue();
        assertThat(storedValueDeployInfo.getGas(), is(BigInteger.valueOf(gasPrice)));
    }

    @And("the query_global_state_result stored value contains the transfer hash")
    public void theQuery_global_state_resultSStoredValueContainsTheTransferHash() {
        logger.info("And the query_global_state_result stored value contains the transfer hash");
        final DeployInfo storedValueDeployInfo = getGlobalDataDataStoredValue();
        assertThat(storedValueDeployInfo.getTransfers().get(0), startsWith("transfer-"));
    }

    @And("the query_global_state_result stored value contains the transfer source uref")
    public void theQuery_global_state_resultSStoredValueContainsTheTransferSource() {
        logger.info("And the query_global_state_result stored value contains the transfer source uref");
        final DeployInfo storedValueDeployInfo = getGlobalDataDataStoredValue();
        final String accountMainPurse = nctl.getAccountMainPurse(1);
        assertThat(storedValueDeployInfo.getSource().getJsonURef(), is(accountMainPurse));
    }


    @Given("that the state root hash is known")
    public void thatTheStateRootHashIsKnown() {

        logger.info("Given that the state root hash is known");
        final StateRootHashData stateRootHash = casperService.getStateRootHash();
        assertThat(stateRootHash, is(notNullValue()));
        assertThat(stateRootHash.getStateRootHash(), notNullValue());
        contextMap.put(StepConstants.STATE_ROOT_HASH, stateRootHash);
    }

    @When("the query_global_state RCP method is invoked with the state root hash as the query identifier and an invalid key")
    public void theQuery_global_stateRCPMethodIsInvokedWithTheStateRootHashAsTheQueryIdentifier() {

        logger.info("When the query_global_state RCP method is invoked with the state root hash as the query identifier");
        final StateRootHashData stateRootHash = contextMap.get(StepConstants.STATE_ROOT_HASH);
        StateRootHashIdentifier globalStateIdentifier = new StateRootHashIdentifier(stateRootHash.getStateRootHash());
        // Need to invoke nctl-view-faucet-account to get uref
        final String key = "uref-d0343bb766946f9f850a67765aae267044fa79a6cd50235ffff248a37534";
        try {
            casperService.queryGlobalState(globalStateIdentifier, key, new String[0]);
        } catch (Exception e) {
            if (e instanceof CasperClientException) {
                contextMap.put(StepConstants.CLIENT_EXCEPTION, e);
                return;
            } else {
                throw new RuntimeException(e);
            }
        }
        fail("Should have thrown a CasperClientException");
    }

    @Then("an error code of {int} is returned")
    public void anAnErrorCodeOfIsReturned(final int errorCode) {

        final CasperClientException clientException = contextMap.get(StepConstants.CLIENT_EXCEPTION);
        assertThat(clientException.toString(), containsString("code: " + errorCode));
    }

    @And("an error message of {string} is returned")
    public void anErrorMessageOfIsReturned(String errorMessage) {

        final CasperClientException clientException = contextMap.get(StepConstants.CLIENT_EXCEPTION);
        assertThat(clientException.toString(), containsString(errorMessage));
    }

    @Given("the query_global_state RCP method is invoked with an invalid block hash as the query identifier")
    public void theQuery_global_stateRCPMethodIsInvokedWithAnInvalidBlockHashAsTheQueryIdentifier() {

        final BlockHashIdentifier globalStateIdentifier = new BlockHashIdentifier("00112233441343670f71afb96018ab193855a85adc412f81571570dea34f2ca6500");
        final String key = "deploy-80fbb9c25eebda88e5d2eb9a0f7053ad6098d487aff841dc719e1526e0f59728";
        try {
            casperService.queryGlobalState(globalStateIdentifier, key, new String[0]);
        } catch (Exception e) {
            if (e instanceof CasperClientException) {
                contextMap.put(StepConstants.CLIENT_EXCEPTION, e);
                return;
            } else {
                throw new RuntimeException(e);
            }
        }
        fail("Should have thrown a CasperClientException");
    }

    void createTransfer() throws Exception {

        logger.info("createTransfer");

        final Date timestamp = new Date();
        final Ed25519PrivateKey senderKey = new Ed25519PrivateKey();
        final Ed25519PublicKey receiverKey = new Ed25519PublicKey();

        senderKey.readPrivateKey(AssetUtils.getUserKeyAsset(1, 1, StepConstants.SECRET_KEY_PEM).getFile());
        receiverKey.readPublicKey(AssetUtils.getUserKeyAsset(1, 2, StepConstants.PUBLIC_KEY_PEM).getFile());

        final Deploy deploy = CasperTransferHelper.buildTransferDeploy(
                senderKey,
                PublicKey.fromAbstractPublicKey(receiverKey),
                BigInteger.valueOf(2500000000L),
                "casper-net-1",
                Math.abs(new Random().nextLong()),
                BigInteger.valueOf(100000000L),
                1L,
                Ttl.builder().ttl("30m").build(),
                timestamp,
                new ArrayList<>());

        contextMap.put(StepConstants.PUT_DEPLOY, deploy);

        contextMap.put(StepConstants.DEPLOY_RESULT, casperService.putDeploy(deploy));
    }

    void waitForBlockAdded() throws Exception {

        logger.info("waitForBlockAdded");

        final DeployResult deployResult = contextMap.get(StepConstants.DEPLOY_RESULT);

        final ExpiringMatcher<Event<BlockAdded>> matcher = (ExpiringMatcher<Event<BlockAdded>>) eventHandler.addEventMatcher(
                EventType.MAIN,
                BlockAddedMatchers.hasTransferHashWithin(
                        deployResult.getDeployHash(),
                        blockAddedEvent -> contextMap.put(StepConstants.LAST_BLOCK_ADDED, blockAddedEvent.getData())
                )
        );

        assertThat(matcher.waitForMatch(400), is(true));

        eventHandler.removeEventMatcher(EventType.MAIN, matcher);

        final Digest matchingBlockHash = ((BlockAdded) contextMap.get(StepConstants.LAST_BLOCK_ADDED)).getBlockHash();
        assertThat(matchingBlockHash, is(notNullValue()));

        final JsonBlockData block = CasperClientProvider.getInstance().getCasperService().getBlock(new HashBlockIdentifier(matchingBlockHash.toString()));
        assertThat(block, is(notNullValue()));
        final List<String> transferHashes = block.getBlock().getBody().getTransferHashes();
        assertThat(transferHashes, hasItem(deployResult.getDeployHash()));
    }

    private <T> T getGlobalDataDataStoredValue() {
        final GlobalStateData globalStateData = contextMap.get(StepConstants.GLOBAL_STATE_DATA);
        //noinspection unchecked
        return (T) globalStateData.getStoredValue().getValue();
    }
}
