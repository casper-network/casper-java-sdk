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
import static com.casper.sdk.e2e.utils.AssetUtils.getUserPublicKey;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
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
    private PublicKey userPublicKey;
    private AbstractPrivateKey faucetPrivateKey;

    @Given("that a deploy is executed against a node using the speculative_exec RPC API")
    public void thatADeployIsExecutedAgainstANodeUsingTheSpeculative_execRPCAPI() throws Exception {

        faucetPrivateKey = getFaucetPrivateKey();
        userPublicKey = getUserPublicKey(1);

        deploy = CasperTransferHelper.buildTransferDeploy(
                faucetPrivateKey,
                userPublicKey,
                new BigInteger("2500000000"),
                "casper-net-1",
                Math.abs(new Random().nextLong()),
                BigInteger.valueOf(100000000L),
                1L,
                Ttl.builder().ttl("30m").build(),
                new Date(),
                new ArrayList<>());

        speculativeDeployData = speculauaCasperService.speculativeExec(deploy);
    }

    @Then("a valid speculative_exec_result will be returned")
    public void aValidSpeculative_exec_resultWillBeReturned() {
        assertThat(speculativeDeployData, is(notNullValue()));
        assertThat(speculativeDeployData.getExecutionResult(), is(instanceOf(Success.class)));
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
        //     getTransform();
        final Optional<Entry> transform = getTransform(key);

        assertThat(transform.isPresent(), is(true));
        this.transform = transform.get();
    }

    @And("the speculative_exec execution_result contains a valid transfer transform")
    public void theSpeculative_execExecution_resultContainsAValidTransferTransform() throws IOException {
        assertThat(this.transform.getTransform(), is(instanceOf(WriteTransfer.class)));

        final WriteTransfer writeTransfer = (WriteTransfer) this.transform.getTransform();

        assertThat(writeTransfer.getTransfer().getDeployHash(), is(deploy.getHash().toString()));
        assertThat(writeTransfer.getTransfer().getAmount(), is(new BigInteger("2500000000")));
        assertThat(writeTransfer.getTransfer().getTo(), is(userPublicKey.generateAccountHash(true)));
        assertThat(writeTransfer.getTransfer().getFrom(), is(PublicKey.fromAbstractPublicKey(faucetPrivateKey.derivePublicKey()).generateAccountHash(true)));

    }

    @And("the speculative_exec execution_result contains a valid deploy transform")
    public void theSpeculative_execExecution_resultContainsAValidDeployTransform() throws IOException {
        final String key = "deploy-" + deploy.getHash().toString();
        final Optional<Entry> transform = getTransform(key);
        assertThat(transform.isPresent(), is(true));

        assertThat(transform.get().getKey(), is(key));

        final WriteDeployInfo writeDeployInfo = (WriteDeployInfo) transform.get().getTransform();
        assertThat(writeDeployInfo.getDeployInfo().getGas(), is(BigInteger.valueOf(100000000L)));
        assertThat(writeDeployInfo.getDeployInfo().getHash(), is(deploy.getHash().toString()));

        assertThat(writeDeployInfo.getDeployInfo().getFrom(), is(PublicKey.fromAbstractPublicKey(faucetPrivateKey.derivePublicKey()).generateAccountHash(true)));

        final AccountData stateAccountInfo = casperService.getStateAccountInfo(
                PublicKey.fromAbstractPublicKey(faucetPrivateKey.derivePublicKey()).getAlgoTaggedHex(),
                HashBlockIdentifier.builder().hash(speculativeDeployData.getBlockHash()).build()
        );

        assertThat(writeDeployInfo.getDeployInfo().getSource().getJsonURef(), is(stateAccountInfo.getAccount().getMainPurse()));
        assertThat(writeDeployInfo.getDeployInfo().getTransfers(), hasSize(1));
        assertThat(writeDeployInfo.getDeployInfo().getTransfers().get(0), containsString("transfer-"));
        assertThat(writeDeployInfo.getDeployInfo().getTransfers().get(0).length(), is(73));
    }

    @And("the speculative_exec execution_result contains a valid balance transform")
    public void theSpeculative_execExecution_resultContainsAValidBalanceTransform() {

        final AccountData stateAccountInfo = casperService.getStateAccountInfo(
                PublicKey.fromAbstractPublicKey(faucetPrivateKey.derivePublicKey()).getAlgoTaggedHex(),
                HashBlockIdentifier.builder().hash(speculativeDeployData.getBlockHash()).build()
        );

        final String mainPurse = stateAccountInfo.getAccount().getMainPurse();
        final List<Entry> transforms = getBalanceTransforms(mainPurse.split("-")[1]);
        assertThat(transforms.size(), is(greaterThan(4)));

        Entry entry = transforms.get(0);
        Transform transform = entry.getTransform();
        assertThat(transform, is(instanceOf(WriteContract.class)));
        assertThat(((WriteContract) transform).name(), is("IDENTITY"));

        entry = transforms.get(transforms.size() - 1);
        transform = entry.getTransform();
        assertThat(transform, is(instanceOf(WriteCLValue.class)));
        assertThat(((WriteCLValue) transform).getClvalue().getClType().getTypeName(), is("U512"));
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

    private List<Entry> getBalanceTransforms(final String mainPurse) {
        return speculativeDeployData.getExecutionResult()
                .getEffect()
                .getTransforms()
                .stream()
                .filter(transform -> transform.getKey().equals("balance-" + mainPurse))
                .collect(Collectors.toList());
    }

    private Optional<Entry> getTransform(final String key) {
        return speculativeDeployData.getExecutionResult().getEffect().getTransforms().stream().filter(transform -> transform.getKey().equals(key)).findFirst();
    }
}
