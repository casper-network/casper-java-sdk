package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.*;
import com.casper.sdk.helper.CasperTransferHelper;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.global.BlockHashIdentifier;
import com.casper.sdk.identifier.global.StateRootHashIdentifier;
import com.casper.sdk.identifier.purse.MainPurseUnderAccountHash;
import com.casper.sdk.identifier.purse.MainPurseUnderPublickey;
import com.casper.sdk.identifier.purse.PurseIdentifier;
import com.casper.sdk.identifier.purse.PurseUref;
import com.casper.sdk.model.account.AccountData;
import com.casper.sdk.model.balance.QueryBalanceData;
import com.casper.sdk.model.block.JsonBlockData;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.executionresult.Success;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.stateroothash.StateRootHashData;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.service.CasperService;
import com.fasterxml.jackson.databind.JsonNode;
import com.syntifi.crypto.key.AbstractPrivateKey;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import com.syntifi.crypto.key.Ed25519PublicKey;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Step definitions for the query_balance.feature
 */
public class QueryBalanceStepDefinitions {

    private final CasperService casperService = CasperClientProvider.getInstance().getCasperService();
    private final TestProperties testProperties = new TestProperties();
    private final SimpleRcpClient simpleRcpClient = new SimpleRcpClient(
            testProperties.getHostname(), testProperties.getRcpPort()
    );
    private QueryBalanceData queryBalanceData;
    private JsonNode queryBalanceJson;
    private DeployData deployData;
    private long transferAmount;
    private long currentBalance;
    private JsonBlockData initialBlock;
    private QueryBalanceData initialBalance;
    private String initialStateRootHash;

    @Given("that a query balance is obtained by main purse public key")
    public void thatAQueryBalanceIsObtainedByMainPursePublicKey() throws Exception {
        final PublicKey publicKey = getFaucetPublicKey();

        final PurseIdentifier purseIdentifier = MainPurseUnderPublickey.builder()
                .publicKey(publicKey)
                .build();

        queryBalanceData = casperService.queryBalance(null, purseIdentifier);
        queryBalanceJson = simpleRcpClient.queryBalance("main_purse_under_public_key", publicKey.getAlgoTaggedHex());
    }

    @Then("a valid query_balance_result is returned")
    public void aValidQuery_balance_resultIsReturned() {
        assertThat(queryBalanceData, is(notNullValue()));
        assertThat(queryBalanceJson, is(notNullValue()));
    }

    @And("the query_balance_result has an API version of {string}")
    public void theQuery_balance_resultHasAnAPIVersionOf(final String apiVersion) {
        assertThat(queryBalanceData.getApiVersion(), is(apiVersion));
    }

    @And("the query_balance_result has a valid balance")
    public void theQuery_balance_resultHasAValidBalance() {
        final BigInteger expected = new BigInteger(queryBalanceJson.at("/result/balance").asText());
        assertThat(queryBalanceData.getBalance(), is(expected));
    }

    @Given("that a query balance is obtained by main purse account hash")
    public void thatAQueryBalanceIsObtainedByMainPurseAccountHash() throws Exception {
        final PublicKey publicKey = getFaucetPublicKey();
        final PurseIdentifier purseIdentifier = MainPurseUnderAccountHash.builder()
                .accountHash(publicKey.generateAccountHash(true))
                .build();

        queryBalanceData = casperService.queryBalance(null, purseIdentifier);
        queryBalanceJson = simpleRcpClient.queryBalance(
                "main_purse_under_account_hash",
                publicKey.generateAccountHash(true)
        );
    }

    @Given("that a query balance is obtained by main purse uref")
    public void thatAQueryBalanceIsObtainedByMainPurseUref() throws Exception {

        final PublicKey publicKey = getFaucetPublicKey();

        final AccountData accountInfo = casperService.getStateAccountInfo(
                publicKey.getAlgoTaggedHex(),
                new HashBlockIdentifier(casperService.getBlock().getBlock().getHash().toString())
        );
        final String uref = accountInfo.getAccount().getMainPurse();
        final PurseIdentifier purseIdentifier = PurseUref.builder()
                .purseURef(URef.fromString(uref))
                .build();

        queryBalanceData = casperService.queryBalance(null, purseIdentifier);
        queryBalanceJson = simpleRcpClient.queryBalance("purse_uref", uref);
    }

    @When("a transfer of {long} is made to user-{int}'s purse")
    public void aTransferOfIsMadeToUserPurse(long amount, int userId) throws Exception {

        this.transferAmount = amount;
        initialBlock = casperService.getBlock();
        this.initialStateRootHash = casperService.getStateRootHash().getStateRootHash();

        final AbstractPrivateKey faucetPrivateKey = getFaucetPrivateKey();

        final PublicKey userPublicKey = getUserPublicKey(userId);

        initialBalance = casperService.queryBalance(
                null,
                MainPurseUnderPublickey.builder().publicKey(userPublicKey).build()
        );

        final Deploy deploy = CasperTransferHelper.buildTransferDeploy(
                faucetPrivateKey,
                userPublicKey,
                BigInteger.valueOf(amount),
                "casper-net-1",
                Math.abs(new Random().nextLong()),
                BigInteger.valueOf(100000000L),
                1L,
                Ttl.builder().ttl("30m").build(),
                new Date(),
                new ArrayList<>());

        final DeployResult deployResult = casperService.putDeploy(deploy);
        deployData = DeployUtils.waitForDeploy(deployResult.getDeployHash(), 300, casperService);

        // Assert successful transfer
        assertThat(deployData.getExecutionResults().size(), is(greaterThan(0)));
        assertThat(deployData.getExecutionResults().get(0).getResult(), is(instanceOf(Success.class)));
        assertThat(deployData.getExecutionResults().get(0).getBlockHash(), is(not(initialBlock.getBlock().getHash().toString())));
    }

    @And("that a query balance is obtained by user-{int}'s main purse public and latest block identifier")
    public void thatAQueryBalanceIsObtainedByMainPursePublicKeyOfUserAndLatestBlockIdentifier(int userId) throws Exception {
        final PublicKey publicKey = getUserPublicKey(userId);
        final PurseIdentifier purseIdentifier = MainPurseUnderPublickey.builder().publicKey(publicKey).build();

        // obtain using block updated in transfer
        queryBalanceData = casperService.queryBalance(
                BlockHashIdentifier.builder().hash(deployData.getExecutionResults().get(0).getBlockHash()).build(),
                purseIdentifier
        );

        this.currentBalance = queryBalanceData.getBalance().longValue();
    }

    @Then("the balance includes the transferred amount")
    public void theBalanceIncludesTheTransferredAmount() {
        assertThat(queryBalanceData.getBalance().longValue(), is(initialBalance.getBalance().longValue() + transferAmount));
    }

    @When("that a query balance is obtained by user-{int}'s main purse public key and previous block identifier")
    public void thatAQueryBalanceIsObtainedByMainPursePublicKeyOfUserAndPreviousBlockIdentifier(int userId) throws Exception {
        final PublicKey publicKey = getUserPublicKey(userId);

        final PurseIdentifier purseIdentifier = MainPurseUnderPublickey.builder()
                .publicKey(publicKey)
                .build();

        // obtain using initial block before transfer
        queryBalanceData = casperService.queryBalance(
                BlockHashIdentifier.builder().hash(initialBlock.getBlock().getHash().toString()).build(),
                purseIdentifier
        );
    }

    @And("that a query balance is obtained by user-{int}'s main purse public and latest state root hash identifier")
    public void thatAQueryBalanceIsObtainedByUserSMainPursePublicAndLatestStateRootHashIdentifier(int userId) throws Exception {
        final PublicKey publicKey = getUserPublicKey(userId);

        final PurseIdentifier purseIdentifier = MainPurseUnderPublickey.builder()
                .publicKey(publicKey)
                .build();

        final StateRootHashData stateRootHash = casperService.getStateRootHash();
        assertThat(stateRootHash.getStateRootHash(), is(not(initialStateRootHash)));

        // obtain using initial block before transfer
        queryBalanceData = casperService.queryBalance(
                StateRootHashIdentifier.builder().hash(stateRootHash.getStateRootHash()).build(),
                purseIdentifier
        );
    }

    @When("that a query balance is obtained by user-{int}'s main purse public key and previous state root hash identifier")
    public void thatAQueryBalanceIsObtainedByUserSMainPursePublicKeyAndPreviousStateRootHashIdentifier(int userId) throws Exception {
        final PublicKey publicKey = getUserPublicKey(userId);

        final PurseIdentifier purseIdentifier = MainPurseUnderPublickey.builder()
                .publicKey(publicKey)
                .build();

        // obtain using initial block before transfer
        queryBalanceData = casperService.queryBalance(
                StateRootHashIdentifier.builder().hash(initialStateRootHash).build(),
                purseIdentifier
        );
    }

    @Then("the balance is the pre transfer amount")
    public void theBalanceIsThePreTransferAmount() {
        assertThat(queryBalanceData.getBalance().longValue(), is(initialBalance.getBalance().longValue()));
    }

    private static PublicKey getFaucetPublicKey() throws IOException {
        return PublicKey.fromAbstractPublicKey(getFaucetPrivateKey().derivePublicKey());
    }

    private static AbstractPrivateKey getFaucetPrivateKey() throws IOException {
        final URL faucetPrivateKeyUrl = AssetUtils.getFaucetAsset(1, "secret_key.pem");
        assertThat(faucetPrivateKeyUrl, is(notNullValue()));
        final Ed25519PrivateKey privateKey = new Ed25519PrivateKey();
        privateKey.readPrivateKey(faucetPrivateKeyUrl.getFile());
        return privateKey;
    }

    private static PublicKey getUserPublicKey(final int userId) throws IOException {
        final URL user1KeyUrl = AssetUtils.getUserKeyAsset(1, userId, "public_key.pem");
        final Ed25519PublicKey publicKey = new Ed25519PublicKey();
        publicKey.readPublicKey(user1KeyUrl.getFile());
        return PublicKey.fromAbstractPublicKey(publicKey);
    }


}
