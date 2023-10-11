package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.helper.CasperTransferHelper;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.model.account.AccountData;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.Entry;
import com.casper.sdk.model.deploy.SpeculativeDeployData;
import com.casper.sdk.model.deploy.executionresult.Success;
import com.casper.sdk.model.deploy.transform.*;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.service.CasperService;
import com.syntifi.crypto.key.AbstractPrivateKey;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.hamcrest.Matchers;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static com.casper.sdk.e2e.utils.AssetUtils.getFaucetPrivateKey;
import static com.casper.sdk.e2e.utils.AssetUtils.getUserPrivateKey;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * The steps for the speculative execution feature.
 */
public class SpeculativeExecutionSteps {

    private final CasperService speculauaCasperService = CasperClientProvider.getInstance().getSpeculauaCasperService();
    private final CasperService casperService = CasperClientProvider.getInstance().getCasperService();
    private SpeculativeDeployData speculativeDeployData;
    private Entry transform;
    private Deploy deploy;

    @Given("that the {string} account transfers {long} to user-{int} account with a gas payment amount of {long} using the speculative_exec RPC API")
    public void thatTheFaucetAccountTransfersToUserAccountUsingTheSpeculative_execRPCAPI(final String faucet,
                                                                                         final long transferAmount,
                                                                                         final int userId,
                                                                                         final long paymentAmount) throws Exception {
        final AbstractPrivateKey faucetPrivateKey = getPrivateKey(faucet);
        final PublicKey userPublicKey = PublicKey.fromAbstractPublicKey(getPrivateKey(userId).derivePublicKey());

        deploy = CasperTransferHelper.buildTransferDeploy(
                faucetPrivateKey,
                userPublicKey,
                BigInteger.valueOf(transferAmount),
                "casper-net-1",
                Math.abs(new Random().nextLong()),
                BigInteger.valueOf(paymentAmount),
                1L,
                Ttl.builder().ttl("30m").build(),
                new Date(),
                new ArrayList<>());

        speculativeDeployData = speculauaCasperService.speculativeExec(deploy);
    }

    @Then("a valid speculative_exec_result will be returned with {int} transforms")
    public void aValidSpeculative_exec_resultWillBeReturned(final int transformCount) {
        assertThat(speculativeDeployData, is(notNullValue()));
        assertThat(speculativeDeployData.getExecutionResult(), is(instanceOf(Success.class)));
        assertThat(speculativeDeployData.getExecutionResult().getEffect().getTransforms(), hasSize(transformCount));
    }

    @And("the speculative_exec has an api_version of {string}")
    public void theSpeculative_execHasAnApi_versionOf(String apiVersion) {
        assertThat(speculativeDeployData.getApiVersion(), is(apiVersion));
    }

    @And("the speculative_exec has a valid block_hash")
    public void theSpeculative_execHasAValidBlock_hash() {
        assertThat(speculativeDeployData.getBlockHash().length(), is(64));
    }

    @And("the execution_results contains a cost of {int}")
    public void theExecution_resultsContainsACostOf(int cost) {
        assertThat(speculativeDeployData.getExecutionResult().getCost(), is(BigInteger.valueOf(cost)));
    }

    @And("the speculative_exec has a valid execution_result")
    public void theSpeculative_execHasAValidExecution_result() {

        final String key = ((Success) speculativeDeployData.getExecutionResult()).getTransfers().get(0);
        final Optional<Entry> transform = getTransform(key);

        assertThat(transform.isPresent(), is(true));
        this.transform = transform.get();
    }

    @And("the speculative_exec execution_result transform wth the transfer key contains the deploy_hash")
    public void theSpeculative_execExecution_resultContainsAValidDeployHash() {
        assertThat(this.transform.getTransform(), is(instanceOf(WriteTransfer.class)));
        final WriteTransfer writeTransfer = (WriteTransfer) this.transform.getTransform();
        assertThat(writeTransfer.getTransfer().getDeployHash(), is(deploy.getHash().toString()));
    }

    @And("the speculative_exec execution_result transform with the transfer key has the amount of {long}")
    public void theSpeculative_execExecution_resultContainsAValidTransferTransform(final long transferAmount) {
        assertThat(this.transform.getTransform(), is(instanceOf(WriteTransfer.class)));

        final WriteTransfer writeTransfer = (WriteTransfer) this.transform.getTransform();
        assertThat(writeTransfer.getTransfer().getAmount(), is(BigInteger.valueOf(transferAmount)));
    }

    @And("the speculative_exec execution_result transform with the transfer key has the {string} field set to the {string} account hash")
    public void theSpeculative_execExecution_resultContainsAValidTransferFromField(final String fieldName, final String accountId) throws IOException {

        final String accountHash = getAccountHash(accountId);

        final WriteTransfer writeTransfer = (WriteTransfer) this.transform.getTransform();
        if ("to".equals(fieldName)) {
            assertThat(writeTransfer.getTransfer().getTo(), is(accountHash));
        } else {
            assertThat(writeTransfer.getTransfer().getFrom(), is(accountHash));
        }
    }

    @And("the speculative_exec execution_result transform with the transfer key has the {string} field set to the purse uref of the {string} account")
    public void theSpeculative_execExecution_resultContainsAValidTransferSourceField(final String fieldName, final String accountId) throws IOException {

        String mainPurse = getAccountInfo(accountId).getAccount().getMainPurse();
        final WriteTransfer writeTransfer = (WriteTransfer) this.transform.getTransform();
        if ("source".equals(fieldName)) {
            assertThat(writeTransfer.getTransfer().getSource(), is(mainPurse));
        } else {
            assertThat(writeTransfer.getTransfer().getTarget().split("-")[0], is(mainPurse.split("-")[0]));
            assertThat(writeTransfer.getTransfer().getTarget().split("-")[1], is(mainPurse.split("-")[1]));
        }
    }

    @And("the speculative_exec execution_result transform with the deploy key has the deploy_hash of the transfer's hash")
    public void theSpeculative_execExecution_resultContainsAValidDeployTransformHash() {
        final String key = "deploy-" + deploy.getHash().toString();
        final Optional<Entry> transform = getTransform(key);
        assertThat(transform.isPresent(), is(true));

        assertThat(transform.get().getKey(), is(key));

        final WriteDeployInfo writeDeployInfo = (WriteDeployInfo) transform.get().getTransform();
        assertThat(writeDeployInfo.getDeployInfo().getHash(), is(deploy.getHash().toString()));

    }

    @And("the speculative_exec execution_result transform with a deploy key has a gas field of {long}")
    public void theSpeculative_execExecution_resultContainsAValidDeployTransformGas(final long gas) {
        final WriteDeployInfo writeDeployInfo = getDeployTransform();
        assertThat(writeDeployInfo.getDeployInfo().getGas(), is(BigInteger.valueOf(gas)));
    }

    @And("the speculative_exec execution_result transform with a deploy key has {int} transfer with a valid transfer hash")
    public void theSpeculative_execExecution_resultContainsAValidDeployTransformTransfers(final int transfers) {
        final WriteDeployInfo writeDeployInfo = getDeployTransform();
        assertThat(writeDeployInfo.getDeployInfo().getTransfers(), hasSize(transfers));
        assertThat(writeDeployInfo.getDeployInfo().getTransfers().get(0), containsString("transfer-"));
        assertThat(writeDeployInfo.getDeployInfo().getTransfers().get(0).length(), is(73));
    }

    @And("the speculative_exec execution_result transform with a deploy key has as from field of the {string} account hash")
    public void theSpeculative_execExecution_resultContainsAValidDeployTransformFrom(final String faucet) throws IOException {
        final WriteDeployInfo writeDeployInfo = getDeployTransform();
        assertThat(writeDeployInfo.getDeployInfo().getFrom(), is(getAccountHash(faucet)));
    }

    @And("the speculative_exec execution_result transform with a deploy key has as source field of the {string} account purse uref")
    public void theSpeculative_execExecution_resultContainsAValidDeployTransformSource(final String faucet) throws IOException {
        final WriteDeployInfo writeDeployInfo = getDeployTransform();
        assertThat(writeDeployInfo.getDeployInfo().getSource().getJsonURef(), is(getAccountInfo(faucet).getAccount().getMainPurse()));
    }

    @And("the speculative_exec execution_result contains at least {int} valid balance transforms")
    public void theSpeculative_execExecution_resultContainsAValidBalanceTransform(final int min) throws IOException {
        final List<Entry> transforms = getFaucetBalanceTransforms();
        assertThat(transforms.size(), is(greaterThanOrEqualTo(min)));
    }

    @And("the speculative_exec execution_result {int}st balance transform is an Identity transform")
    public void theSpeculative_execExecution_resultContainsAValidBalanceIdentity(final int first) throws IOException {
        final List<Entry> transforms = getFaucetBalanceTransforms();
        final Entry entry = transforms.get(first - 1);
        Transform transform = entry.getTransform();
        assertThat(transform, is(instanceOf(WriteContract.class)));
        assertThat(((WriteContract) transform).name(), is("IDENTITY"));
    }

    @And("the speculative_exec execution_result last balance transform is an Identity transform is as WriteCLValue of type {string}")
    public void theSpeculative_execExecution_resultContainsAValidBalanceLast(final String type) throws IOException {
        final List<Entry> transforms = getFaucetBalanceTransforms();
        final Entry entry = transforms.get(transforms.size() - 1);
        final Transform transform = entry.getTransform();
        assertThat(transform, is(instanceOf(WriteCLValue.class)));
        assertThat(((WriteCLValue) transform).getClvalue().getClType().getTypeName(), is(type));
        final BigInteger value = (BigInteger) ((WriteCLValue) transform).getClvalue().getValue();
        assertThat(value, is(greaterThan(BigInteger.valueOf(9999))));
    }

    @And("the speculative_exec execution_result contains a valid AddUInt512 transform with a value of {long}")
    public void theSpeculative_execExecution_resultContainsAValidAddUIntTransform(long value) {
        final Entry lastEntry = speculativeDeployData.getExecutionResult().getEffect().getTransforms().get(
                speculativeDeployData.getExecutionResult().getEffect().getTransforms().size() - 1
        );

        // Assert the last transform is the addition of the balance transfer
        final Transform transform = lastEntry.getTransform();
        assertThat(transform, is(Matchers.instanceOf(AddUInt512.class)));
        assertThat(((AddUInt512) transform).getU512(), is(BigInteger.valueOf(value)));
    }

    private List<Entry> getFaucetBalanceTransforms() throws IOException {
        final String mainPurse = getAccountInfo("faucet").getAccount().getMainPurse().split("-")[1];
        return speculativeDeployData.getExecutionResult()
                .getEffect()
                .getTransforms()
                .stream()
                .filter(transform -> transform.getKey().equals("balance-" + mainPurse))
                .collect(Collectors.toList());
    }

    private Optional<Entry> getTransform(final String key) {
        return speculativeDeployData.getExecutionResult()
                .getEffect()
                .getTransforms()
                .stream().filter(transform -> transform.getKey().equals(key)).findFirst();
    }

    private AbstractPrivateKey getPrivateKey(final Object id) throws IOException {
        if ("faucet".equals(id)) {
            return getFaucetPrivateKey();
        } else {
            return getUserPrivateKey(getUserId(id));
        }
    }

    private int getUserId(Object id) {
        return id instanceof Number ? (Integer) id : new Integer(id.toString().split("-")[1]);
    }

    private String getAccountHash(Object accountId) throws IOException {
        return PublicKey.fromAbstractPublicKey(getPrivateKey(accountId).derivePublicKey()).generateAccountHash(true);
    }


    private AccountData getAccountInfo(final Object accountId) throws IOException {
        return casperService.getStateAccountInfo(
                PublicKey.fromAbstractPublicKey(getPrivateKey(accountId).derivePublicKey()).getAlgoTaggedHex(),
                HashBlockIdentifier.builder().hash(speculativeDeployData.getBlockHash()).build()
        );
    }

    private WriteDeployInfo getDeployTransform() {
        final String key = "deploy-" + deploy.getHash().toString();
        final Optional<Entry> transform = getTransform(key);
        return (WriteDeployInfo) transform
                .orElseThrow(() -> new IllegalArgumentException("Missing deploy transform"))
                .getTransform();
    }
}
