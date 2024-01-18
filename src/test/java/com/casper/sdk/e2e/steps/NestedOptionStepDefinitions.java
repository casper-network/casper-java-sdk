package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.e2e.utils.DeployUtils;
import com.casper.sdk.model.clvalue.CLValueList;
import com.casper.sdk.model.clvalue.CLValueOption;
import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.clvalue.CLValueU256;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.NamedArg;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import io.cucumber.java.PendingException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.casper.sdk.e2e.utils.DeployUtils.buildStandardTransferDeploy;
import static com.casper.sdk.e2e.utils.DeployUtils.getNamedArgValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Step definitions for the nested_option.feature
 */
public class NestedOptionStepDefinitions {
    private CLValueOption clOption;
    private DeployResult deployResult;
    private DeployData deployData;

    @Given("^that a nested Option has an inner type of Option with a type of String and a value of \"([^\"]*)\"$")
    public void thatANestedOptionHasAnInnerTypeOfOptionWithATypeOfStringAndAValueOf(final String strVal) throws ValueSerializationException {
        this.clOption = new CLValueOption(Optional.of(new CLValueString(strVal)));
    }

    @Then("^the inner type is Option with a type of String and a value of \"([^\"]*)\"$")
    public void theInnerTypeIsOptionWithATypeOfStringAndAValueOf(final String value) {
        assertThat(clOption.getValue().isPresent(), is(true));
        assertThat(clOption.getValue().get().getValue(), is(value));
    }

    @And("^the bytes are \"([^\"]*)\"$")
    public void theBytesAre(String hexBytes) {
        assertThat(clOption.getBytes(), is(hexBytes));
    }

    @Given("^that the nested Option is deployed in a transfer$")
    public void thatTheNestedOptionIsDeployedInATransfer() throws Exception {
        final List<NamedArg<?>> transferArgs = new LinkedList<>();
        transferArgs.add(new NamedArg<>("nested_option", this.clOption));
        final Deploy deploy = buildStandardTransferDeploy(transferArgs);

        // Clear out
        this.clOption = null;
        deployResult = CasperClientProvider.getInstance().getCasperService().putDeploy(deploy);
    }

    @And("^the transfer containing the nested Option is successfully executed$")
    public void theTransferContainingTheNestedOptionIsSuccessfullyExecuted() {
        deployData = DeployUtils.waitForDeploy(
                deployResult.getDeployHash(), 300,
                CasperClientProvider.getInstance().getCasperService()
        );
    }

    @When("^the nested Option is read from the deploy$")
    public void theOptionIsReadFromTheDeploy() {
        this.clOption = (CLValueOption) getNamedArgValue(deployData.getDeploy().getSession().getArgs(), "nested_option");
    }

    @Given("^that a nested Option has an inner type of List with a type of U256 and a value of \\((\\d+), (\\d+), (\\d+)\\)$")
    public void thatANestedOptionHasAnInnerTypeOfListWithATypeOfUAndAValueOf(final long val1, final long val2, final long val3) throws ValueSerializationException {
        final CLValueList innerValue = new CLValueList(
                Arrays.asList(
                        new CLValueU256(BigInteger.valueOf(val1)),
                        new CLValueU256(BigInteger.valueOf(val2)),
                        new CLValueU256(BigInteger.valueOf(val3)
                        )
                )
        );
        this.clOption = new CLValueOption(Optional.of(innerValue));
    }

    @And("^the nested list's length is (\\d+)$")
    public void theListSLengthIs(final int length) {
        assertThat(clOption.getValue().isPresent(), is(true));
        assertThat(((CLValueList) clOption.getValue().get()).getValue().size(), is(length));
    }

    @And("^the nested list's \"([^\"]*)\" item is a CLValue with U(\\d+) value of (\\d+)$")
    public void theListSItemIsACLValueWithUValueOf(String nth, int numLen, long value) {
        assertThat(clOption.getValue().isPresent(), is(true));
        final CLValueList list = (CLValueList) clOption.getValue().get();
        final CLValueU256 actualVal = (CLValueU256) list.getValue().get(Integer.parseInt(nth.substring(0, 1)) - 1);
        assertThat(actualVal.getValue(), is(BigInteger.valueOf(value)));
    }

    @Given("^that a nested Option has an inner type of Tuple2 with a type of \"([^\"]*)\" values of \\(\"([^\"]*)\", (\\d+)\\)$")
    public void thatANestedOptionHasAnInnerTypeOfTupleWithATypeOfValuesOf(String type, String val1, int val2) {
        // Write code here that turns the phrase above into concrete actions
        //throw new PendingException();
    }

    //  the inner type is Tuple2 with a type of "String,U32" and a value of ("7fc5ae1d-348e-4b01-97cf-a1c2942b28a9", 10000)
    @Then("^the inner type is Tuple2 with a type of \"([^\"]*)\" and a value of \\(\"([^\"]*)\", (\\d+)\\)$")
    public void theInnerTypeIsTupleWithATypeOfAndAValueOf(String types, String val1, int val2) {
        // Write code here that turns the phrase above into concrete actions
        // throw new PendingException();
    }

    @Given("^that a nested Option has an inner type of Map with a type of \"([^\"]*)\" values of \"([^\"]*)\"ONE\"([^\"]*)\"$")
    public void thatANestedOptionHasAnInnerTypeOfMapWithATypeOfValuesOfONE(String arg0, String arg1, String arg2) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the inner type is Map with a type of \"([^\"]*)\" and a value of \"([^\"]*)\"ONE\"([^\"]*)\"$")
    public void theInnerTypeIsMapWithATypeOfAndAValueOfONE(String arg0, String arg1, String arg2) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Given("^that a nested Option has an inner type of Any with a value of \"([^\"]*)\"$")
    public void thatANestedOptionHasAnInnerTypeOfAnyWithAValueOf(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }

    @Then("^the inner type is Any with a value of \"([^\"]*)\"$")
    public void theInnerTypeIsAnyWithAValueOf(String arg0) {
        // Write code here that turns the phrase above into concrete actions
        throw new PendingException();
    }
}
