package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.AssetUtils;
import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.e2e.utils.SimpleRcpClient;
import com.casper.sdk.e2e.utils.TestProperties;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.purse.MainPurseUnderAccountHash;
import com.casper.sdk.identifier.purse.MainPurseUnderPublickey;
import com.casper.sdk.identifier.purse.PurseIdentifier;
import com.casper.sdk.identifier.purse.PurseUref;
import com.casper.sdk.model.account.AccountData;
import com.casper.sdk.model.balance.QueryBalanceData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.service.CasperService;
import com.fasterxml.jackson.databind.JsonNode;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Step definitions for the query_balance.feature
 */
public class QueryBalanceStepDefinitions {

    private final CasperService casperService = CasperClientProvider.getInstance().getCasperService();
    private final TestProperties testProperties = new TestProperties();
    private final SimpleRcpClient simpleRcpClient = new SimpleRcpClient(testProperties.getHostname(), testProperties.getRcpPort());
    private QueryBalanceData queryBalanceData;
    private JsonNode queryBalanceJson;

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
        queryBalanceJson = simpleRcpClient.queryBalance("main_purse_under_account_hash", publicKey.generateAccountHash(true));
    }

    @Given("that a query balance is obtained by main purse uref")
    public void thatAQueryBalanceIsObtainedByMainPurseUref() throws Exception {

        final PublicKey publicKey = getFaucetPublicKey();

        final AccountData accountInfo = casperService.getStateAccountInfo(publicKey.getAlgoTaggedHex(), new HashBlockIdentifier(casperService.getBlock().getBlock().getHash().toString()));
        final String uref = accountInfo.getAccount().getMainPurse();
        final PurseIdentifier purseIdentifier = PurseUref.builder()
                .purseURef(URef.fromString(uref))
                .build();

        queryBalanceData = casperService.queryBalance(null, purseIdentifier);
        queryBalanceJson = simpleRcpClient.queryBalance("purse_uref", uref);
    }

    private static PublicKey getFaucetPublicKey() throws IOException {
        final URL faucetPrivateKeyUrl = AssetUtils.getFaucetAsset(1, "secret_key.pem");
        assertThat(faucetPrivateKeyUrl, is(notNullValue()));
        final Ed25519PrivateKey privateKey = new Ed25519PrivateKey();
        privateKey.readPrivateKey(faucetPrivateKeyUrl.getFile());
        return PublicKey.fromAbstractPublicKey(privateKey.derivePublicKey());
    }
}
