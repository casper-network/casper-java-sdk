package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.matcher.DeployMatchers;
import com.casper.sdk.e2e.utils.AssetUtils;
import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.helper.CasperTransferHelper;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.model.block.JsonBlockData;
import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.cltype.CLTypeU512;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.*;
import com.casper.sdk.model.deploy.executabledeploy.ExecutableDeployItem;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;
import com.casper.sdk.model.event.blockadded.BlockAdded;
import com.casper.sdk.model.event.deployaccepted.DeployAccepted;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.service.CasperService;
import com.casper.sdk.e2e.event.EventHandler;
import com.casper.sdk.e2e.matcher.ExpiringMatcher;
import com.casper.sdk.e2e.utils.ContextMap;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import com.syntifi.crypto.key.Ed25519PublicKey;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;

import static com.casper.sdk.e2e.steps.StepConstants.*;
import static com.casper.sdk.e2e.matcher.BlockAddedMatchers.hasTransferHashWithin;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Step Definitions for Deploy Cucumber Tests.
 *
 * @author ian@meywood.com
 */
public class DeployStepDefinitions {

    private final ContextMap contextMap = ContextMap.getInstance();
    private final Logger logger = LoggerFactory.getLogger(DeployStepDefinitions.class);
    private static EventHandler eventHandler;

    @BeforeAll
    public static void setUp() {
        ContextMap.getInstance().clear();
        eventHandler = new EventHandler(EventTarget.POJO);
    }

    @SuppressWarnings("unused")
    @AfterAll
    void tearDown() {
        eventHandler.close();
    }

    @Given("that user-{int} initiates a transfer to user-{int}")
    public void thatUserCreatesATransferOfToUser(final int senderId, final int receiverId) throws IOException {

        logger.info("Given that user-{} initiates a transfer to user-{} ", senderId, receiverId);

        final Ed25519PrivateKey senderKey = new Ed25519PrivateKey();
        final Ed25519PublicKey receiverKey = new Ed25519PublicKey();

        senderKey.readPrivateKey(AssetUtils.getUserKeyAsset(1, senderId, SECRET_KEY_PEM).getFile());
        receiverKey.readPublicKey(AssetUtils.getUserKeyAsset(1, receiverId, PUBLIC_KEY_PEM).getFile());

        contextMap.put(SENDER_KEY, senderKey);
        contextMap.put(RECEIVER_KEY, receiverKey);
    }

    @And("the deploy is given a ttl of {int}m")
    public void theDeployIsGivenATtlOfM(final int ttlMinutes) {

        logger.info("And the deploy has a ttl of {}m", ttlMinutes);

        contextMap.put(TTL, Ttl.builder().ttl(ttlMinutes + "m").build());
    }

    @And("the transfer amount is {long}")
    public void theTransferAmountIs(final long amount) {

        logger.info("And the transfer amount is {}", amount);

        contextMap.put(TRANSFER_AMOUNT, BigInteger.valueOf(amount));
    }

    @And("the transfer gas price is {long}")
    public void theTransferPriceIs(final long price) {

        logger.info("And the transfer gas price is {}", price);

        contextMap.put(GAS_PRICE, price);
    }

    @When("the deploy is put on chain {string}")
    public void theDeployIsPut(final String chainName) throws Exception {

        logger.info("When the deploy is put on chain {}", chainName);

        final Date timestamp = new Date();
        contextMap.put(DEPLOY_TIMESTAMP, timestamp);

        final Deploy deploy = CasperTransferHelper.buildTransferDeploy(
                contextMap.get(SENDER_KEY),
                PublicKey.fromAbstractPublicKey(contextMap.get(RECEIVER_KEY)),
                contextMap.get(TRANSFER_AMOUNT),
                chainName,
                Math.abs(new Random().nextLong()),
                BigInteger.valueOf(100000000L),
                contextMap.get(GAS_PRICE),
                contextMap.get(TTL),
                timestamp,
                new ArrayList<>());

        contextMap.put(PUT_DEPLOY, deploy);

        final CasperService casperService = CasperClientProvider.getInstance().getCasperService();

        contextMap.put(DEPLOY_RESULT, casperService.putDeploy(deploy));
    }


    @Then("the deploy response contains a valid deploy hash of length {int} and an API version {string}")
    public void theValidDeployHashIsReturned(final int hashLength, final String apiVersion) {

        logger.info("Then the deploy response contains a valid deploy hash of length {} and an API version {}", hashLength, apiVersion);

        DeployResult deployResult = contextMap.get(DEPLOY_RESULT);
        assertThat(deployResult, is(notNullValue()));
        assertThat(deployResult.getDeployHash(), is(notNullValue()));
        assertThat(deployResult.getDeployHash().length(), is((hashLength)));
        assertThat(deployResult.getApiVersion(), is((apiVersion)));

        logger.info("deployResult.getDeployHash() {}", deployResult.getDeployHash());
    }

    @Then("wait for a block added event with a timeout of {long} seconds")
    public void waitForABlockAddedEventWithATimeoutOfSeconds(final long timeout) throws Exception {

        logger.info("Then wait for a block added event with a timeout of {} seconds", timeout);

        final DeployResult deployResult = contextMap.get(DEPLOY_RESULT);

        final ExpiringMatcher<Event<BlockAdded>> matcher = (ExpiringMatcher<Event<BlockAdded>>) eventHandler.addEventMatcher(
                EventType.MAIN,
                hasTransferHashWithin(
                        deployResult.getDeployHash(),
                        blockAddedEvent -> contextMap.put(LAST_BLOCK_ADDED, blockAddedEvent.getData())
                )
        );

        assertThat(matcher.waitForMatch(timeout), is(true));

        eventHandler.removeEventMatcher(EventType.MAIN, matcher);

        final Digest matchingBlockHash = ((BlockAdded) contextMap.get(LAST_BLOCK_ADDED)).getBlockHash();
        assertThat(matchingBlockHash, is(notNullValue()));

        final JsonBlockData block = CasperClientProvider.getInstance().getCasperService().getBlock(new HashBlockIdentifier(matchingBlockHash.toString()));
        assertThat(block, is(notNullValue()));
        final List<String> transferHashes = block.getBlock().getBody().getTransferHashes();
        assertThat(transferHashes, hasItem(deployResult.getDeployHash()));
    }

    @And("the Deploy is accepted")
    public void theDeployIsAccepted() throws Exception {

        final DeployResult deployResult = contextMap.get(DEPLOY_RESULT);
        logger.info("the Deploy {} is accepted", deployResult.getDeployHash());

        final ExpiringMatcher<Event<DeployAccepted>> matcher = (ExpiringMatcher<Event<DeployAccepted>>) eventHandler.addEventMatcher(
                EventType.DEPLOYS,
                DeployMatchers.theDeployIsAccepted(
                        deployResult.getDeployHash(),
                        event -> contextMap.put(DEPLOY_ACCEPTED, event.getData())
                )
        );

        assertThat(matcher.waitForMatch(5000L), is(true));

        eventHandler.removeEventMatcher(EventType.DEPLOYS, matcher);
    }

    @Given("that a Transfer has been successfully deployed")
    public void thatATransferHasBeenDeployed() {

        logger.info("Given that a Transfer has been deployed");

        final DeployResult deployResult = contextMap.get(DEPLOY_RESULT);
        assertThat(deployResult, is(notNullValue()));
    }

    @When("a deploy is requested via the info_get_deploy RCP method")
    public void whenTheDeployIsRequestedAValidDeployDataIsReturned() {

        final DeployResult deployResult = contextMap.get(DEPLOY_RESULT);
        final CasperService casperService = CasperClientProvider.getInstance().getCasperService();
        final DeployData deploy = casperService.getDeploy(deployResult.getDeployHash());
        assertThat(deploy, is(notNullValue()));
        contextMap.put(INFO_GET_DEPLOY, deploy);
        assertThat(deploy.getExecutionResults().size(), is(greaterThan(0)));
    }

    @Then("the deploy data has an API version of {string}")
    public void theDeployDataHasAnAPIVersionOf(final String apiVersion) {

        logger.info("Then the deploy data has an API version of {}", apiVersion);

        assertThat(getDeployData().getApiVersion(), is(apiVersion));
    }

    @And("the deploy has a session type of {string}")
    public void theDeploySSessionIsA(final String sessionType) {
        final String actualSessionType = getDeployData().getDeploy().getSession().getClass().getSimpleName().toLowerCase();
        final String expectedSessionType = sessionType.replace(" ", "").toLowerCase();
        assertThat(actualSessionType, is(expectedSessionType));
    }

    @And("the deploy is approved by user-{int}")
    public void theDeployIsSignedByUser(final int userId) throws IOException {

        final List<Approval> approvals = getDeployData().getDeploy().getApprovals();
        assertThat(approvals, hasSize(1));

        final Ed25519PublicKey approvalKey = new Ed25519PublicKey();
        final URL userKeyAsset = AssetUtils.getUserKeyAsset(1, userId, PUBLIC_KEY_PEM);
        approvalKey.readPublicKey(userKeyAsset.getFile());
        PublicKey publicKey = PublicKey.fromAbstractPublicKey(approvalKey);

        assertThat(approvals.get(0).getSigner(), is(publicKey));
    }

    @And("the deploy execution result has {string} block hash")
    public void theDeployExecutionResultHasBlockHash(final String blockName) {

        final BlockAdded blockAdded = contextMap.get(blockName);
        assertThat(
                getDeployData().getExecutionResults().get(0).getBlockHash(),
                is(blockAdded.getBlockHash().toString())
        );
    }

    @And("the deploy execution has a cost of {long} motes")
    public void theDeployExecutionResultHasACostOf(final long cost) {
        assertThat(getDeployData().getExecutionResults().get(0).getResult().getCost(), is(BigInteger.valueOf(cost)));
    }

    @And("the deploy header has a gas price of {long}")
    public void theDeployHeaderHasAGasPriceOf(final long gasPrice) {
        assertThat(getDeployData().getDeploy().getHeader().getGasPrice(), is(gasPrice));
    }

    @And("the deploy header has a chain name of {string}")
    public void theDeployHeaderHasAChainNameOf(final String chainName) {
        assertThat(getDeployData().getDeploy().getHeader().getChainName(), is(chainName));
    }

    @And("the deploy has a gas price of {long}")
    public void theDeployHasAGasPriceOf(long gasPrice) {
        assertThat(getDeployData().getDeploy().getHeader().getGasPrice(), is(gasPrice));
    }

    @And("the deploy session has a {string} argument value of type {string}")
    public void theDeploySessionHasAArgumentOfType(final String argName, final String argType) {
        final ExecutableDeployItem session = getDeployData().getDeploy().getSession();
        final NamedArg<?> namedArg = getNamedArg(session.getArgs(), argName);
        assertThat(namedArg.getClValue().getClass().getSimpleName(), is("CLValue" + argType));
    }

    @And("the deploy session has a {string} argument with a numeric value of {long}")
    public void theDeploySessionHasAArgumentWithAValueOf(String argName, long value) {
        final ExecutableDeployItem session = getDeployData().getDeploy().getSession();
        final NamedArg<?> namedArg = getNamedArg(session.getArgs(), argName);
        if (namedArg.getClValue().getClType() instanceof CLTypeU512) {
            assertThat(namedArg.getClValue().getValue(), is(BigInteger.valueOf(value)));
        } else {
            throw new IllegalArgumentException(namedArg.getClValue().getClType().getClass().getSimpleName() + " not yet implemented");
        }
    }

    @And("the deploy session has a {string} argument with the public key of user-{int}")
    public void theDeploySessionHasAArgumentWithThePublicKeyOfUser(String argName, int userId) throws IOException {
        final ExecutableDeployItem session = getDeployData().getDeploy().getSession();

        final Ed25519PublicKey publicKey = new Ed25519PublicKey();
        publicKey.readPublicKey(AssetUtils.getUserKeyAsset(1, userId, PUBLIC_KEY_PEM).getFile());

        final NamedArg<?> namedArg = getNamedArg(session.getArgs(), argName);
        final CLValuePublicKey clValue = (CLValuePublicKey) namedArg.getClValue();
        assertThat(clValue.getValue(), is(PublicKey.fromAbstractPublicKey(publicKey)));
    }

    @And("the deploy has a ttl of {int}m")
    public void theDeployHasATtlOfM(int ttlMinutes) {
        assertThat(getDeployData().getDeploy().getHeader().getTtl().getTtl(), is(Ttl.builder().ttl(ttlMinutes + "m").build().getTtl()));
    }

    @And("the deploy has a valid body hash")
    public void theDeployHasAValidBodyHash() {
        assertThat(getDeployData().getDeploy().getHeader().getBodyHash().isValid(), is(true));

        // Compare body hash of put deploy with
        final Deploy deploy = contextMap.get(PUT_DEPLOY);
        assertThat(deploy.getHeader().getBodyHash(), is(deploy.getHeader().getBodyHash()));
    }

    @And("the deploy has a payment amount of {long}")
    public void theDeployHasAPaymentAmountOf(long amount) {
        final NamedArg<?> amountArg = getNamedArg(getDeployData().getDeploy().getPayment().getArgs(), AMOUNT);
        assertThat(amountArg.getClValue(), instanceOf(CLValueU512.class));
        assertThat(amountArg.getClValue().getValue(), is(BigInteger.valueOf(amount)));
    }

    @And("the deploy has a valid hash")
    public void theDeployHasAValidHash() {
        assertThat(getDeployData().getDeploy().getHash().isValid(), is(true));

        // Obtain the hash of the put deploy and compare to one obtained with info_get_deploy
        final DeployResult deployResult = contextMap.get(DEPLOY_RESULT);
        assertThat(getDeployData().getDeploy().getHash().toString(), is(deployResult.getDeployHash()));
    }

    @And("the deploy has a valid timestamp")
    public void theDeployHasAValidTimestamp() {
        Date timestamp = contextMap.get(DEPLOY_TIMESTAMP);
        assertThat(getDeployData().getDeploy().getHeader().getTimeStamp(), is(timestamp));
    }

    private DeployData getDeployData() {
        return contextMap.get(INFO_GET_DEPLOY);
    }

    private NamedArg<?> getNamedArg(final List<NamedArg<?>> namedArgs, final String argName) {
        final Optional<NamedArg<?>> optionalNamedArg = namedArgs.stream()
                .filter(namedArg -> namedArg.getType().equals(argName))
                .findFirst();
        assertThat(optionalNamedArg.isPresent(), is(true));
        return optionalNamedArg.get();
    }
}
