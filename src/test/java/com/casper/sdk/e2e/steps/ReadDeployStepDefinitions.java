package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.AssetUtils;
import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.e2e.utils.ContextMap;
import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.deploy.executabledeploy.Transfer;
import com.casper.sdk.service.CasperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * The step definitions for the read_deploy.feature that tests a transfer deploy can be read from JSON.
 *
 * @author ian@meywood.com
 */
public class ReadDeployStepDefinitions {

    private final ContextMap contextMap = ContextMap.getInstance();
    public final CasperService casperService = CasperClientProvider.getInstance().getCasperService();
    private final Logger logger = LoggerFactory.getLogger(ReadDeployStepDefinitions.class);

    @Given("that the {string} JSON deploy is loaded")
    public void thatTheJSONDeployIsLoaded(final String jsonFilename) throws IOException {

        logger.info("Given that the {} JSON deploy is loaded", jsonFilename);

        final URL jsonUrl = AssetUtils.getStandardTestResourceURL("/json/" + jsonFilename);
        final InputStream jsonIn = Objects.requireNonNull(jsonUrl.openStream());
        assertThat(jsonIn, is(notNullValue()));

        final Deploy transfer = new ObjectMapper().readValue(jsonIn, Deploy.class);
        contextMap.put("transfer", transfer);
    }

    @Then("a valid transfer deploy is created")
    public void aValidTransferDeployIsCreated() {
        logger.info("Then a valid transfer deploy is created");

        final Deploy transfer = getDeploy();
        assertThat(transfer, is(notNullValue()));
    }

    @And("the deploy hash is {string}")
    public void theDeployHashIs(final String hash) {
        assertThat(getDeploy().getHash().toString(), is(hash));
    }

    @And("the account is {string}")
    public void theAccountIs(final String account) {
        assertThat(getDeploy().getHeader().getAccount().getAlgoTaggedHex(), is(account));
    }

    @And("the timestamp is {string}")
    public void theTimestampIs(final String timestamp) {
        assertThat(getDeploy().getHeader().getTimeStamp(), is(new DateTime(timestamp).toDate()));
    }

    @And("the ttl is {int}m")
    public void theTtlIsM(final int ttl) {
        assertThat(getDeploy().getHeader().getTtl().getTtl(), is(Ttl.builder().ttl(ttl + "m").build().getTtl()));
    }

    @And("the gas price is {long}")
    public void theGasPriceIs(final long gasPrice) {
        assertThat(getDeploy().getHeader().getGasPrice(), is(gasPrice));
    }

    @And("the body_hash is {string}")
    public void theBody_hashIs(final String bodyHash) {
        assertThat(getDeploy().getHeader().getBodyHash().toString(), is(bodyHash));
    }

    @And("the chain name is  {string}")
    public void theChainNameIs(final String chainName) {
        assertThat(getDeploy().getHeader().getChainName(), is(chainName));
    }

    private Deploy getDeploy() {
        return contextMap.get("transfer");
    }

    @And("dependency {int} is {string}")
    public void dependencyIs(int index, final String hex) {
        assertThat(getDeploy().getHeader().getDependencies().get(index).toString(), is(hex));
    }

    @And("the payment amount is {long}")
    public void thePaymentAmountIs(long amount) throws ValueSerializationException {
        final NamedArg<?> payment = getNamedArg(getDeploy().getPayment().getArgs(), "amount");
        assertThat(payment.getClValue(), is(new CLValueU512(BigInteger.valueOf(amount))));
    }

    @And("the session is a transfer")
    public void theSessionIsATransfer() {
        assertThat(getDeploy().getSession(), is(instanceOf(Transfer.class)));
    }

    @And("the session {string} is {long}")
    public void theSessionAmountIs(final String parameterName, final long amount) throws ValueSerializationException {
        final NamedArg<?> namedArg = getNamedArg(getDeploy().getSession().getArgs(), parameterName);
        assertThat(namedArg.getClValue(), is(new CLValueU512(BigInteger.valueOf(amount))));
    }

    @And("the deploy has {int} approval")
    public void theDeployHasApproval(final int approvalSize) {
        assertThat(getDeploy().getApprovals(), hasSize(approvalSize));
    }

    @And("the approval signer is {string}")
    public void theApprovalSignerIs(final String signer) {
        assertThat(getDeploy().getApprovals().get(0).getSigner().toString(), is(signer));
    }

    @And("the approval signature is {string}")
    public void theApprovalSignatureIs(final String signature) {
        assertThat(getDeploy().getApprovals().get(0).getSignature().getAlgoTaggedHex(), is(signature));
    }

    @And("the session {string} bytes is {string}")
    public void theSessionAmountBytesIs(final String parameterName, final String bytes) {
        final NamedArg<?> amount = getNamedArg(getDeploy().getSession().getArgs(), parameterName);
        final AbstractCLValue<?, ?> clValue = amount.getClValue();
        // TODO check why this is different
        assertThat(clValue.getBytes() + (clValue.getClType().getTypeName().equals("U64") ? "00" : ""), is(bytes));
    }

    @And("the session {string} parsed is {string}")
    public void theSessionAmountParsedIs(final String parameterName, final String parsed) {
        final NamedArg<?> amount = getNamedArg(getDeploy().getSession().getArgs(), parameterName);
        final AbstractCLValue<?, ?> clValue = amount.getClValue();

        final Object parsedVal = clValue.getParsed();
        if (parsedVal instanceof Integer) {
            assertThat(parsedVal, is(Integer.parseInt(parsed)));
        } else {
            assertThat(parsedVal, is(parsed));
        }
    }

    @And("the session {string} type is {string}")
    public void theSessionTypeIs(final String parameterName, final String typeName) {
        final NamedArg<?> amount = getNamedArg(getDeploy().getSession().getArgs(), parameterName);
        final AbstractCLValue<?, ?> clValue = amount.getClValue();
        assertThat(clValue.getClType().getTypeName(), is(typeName));
    }

    @And("the session {string} is {string}")
    public void theSessionIs(final String parameterName, final String value) {
        final NamedArg<?> amount = getNamedArg(getDeploy().getSession().getArgs(), parameterName);
        final AbstractCLValue<?, ?> clValue = amount.getClValue();
        if (clValue.getValue() instanceof byte[]) {
            assertThat(clValue.getValue(), is(Hex.decode(value)));
        } else {
            assertThat(clValue.getValue().toString(), is(value));
        }
    }

    private NamedArg<?> getNamedArg(final List<NamedArg<?>> args, final String name) {
        Optional<NamedArg<?>> namedArg = args.stream().filter(arg -> name.equals(arg.getType())).findFirst();
        assertThat(namedArg.isPresent(), is(true));
        return namedArg.get();
    }
}
