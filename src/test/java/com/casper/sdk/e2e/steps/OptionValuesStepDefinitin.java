package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.CLValueFactory;
import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.e2e.utils.DeployUtils;
import com.casper.sdk.model.clvalue.CLValueOption;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.NamedArg;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.casper.sdk.e2e.utils.DeployUtils.buildStandardTransferDeploy;
import static com.casper.sdk.e2e.utils.DeployUtils.getNamedArgValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Step definitions for the Option value feature.
 * @author ian@meywood.com
 */
public class OptionValuesStepDefinitin {

    private CLValueOption clValueOption;
    private final CLValueFactory clValueFactory = new CLValueFactory();
    private DeployResult deployResult;
    private DeployData deployData;

    @Given("^that an Option value has an empty value$")
    public void thatAnOptionValueHasAnEmptyValue() throws ValueSerializationException {
        clValueOption = new CLValueOption(Optional.empty());
    }

    @Then("^the Option value is not present$")
    public void theOptionValueShouldBeInvalid() {
        assertThat(clValueOption.getValue().isPresent(), is(false));
    }

    @And("the Option value's bytes are {string}")
    public void theOptionValueBytesAre(final String hexBytes) {
        assertThat(clValueOption.getBytes(), is(hexBytes));
    }

    @Given("an Option value contains a {string} value of {string}")
    public void thatAnOptionValuesHasAValueOf(final String typeName, final String strValue) throws Exception {
        clValueOption = new CLValueOption(
                Optional.of(clValueFactory.createValue(CLTypeData.getTypeByName(typeName), strValue))
        );
    }

    @Then("the Option value is present")
    public void theOptionValueIsPresent() {
        assertThat(clValueOption.getValue().isPresent(), is(true));
    }

    @Given("that the Option value is deployed in a transfer as a named argument")
    public void thatTheOptionValueIsDeployedInATransferAsANamedArgument() throws Exception {
        final List<NamedArg<?>> transferArgs = new LinkedList<>();
        transferArgs.add(new NamedArg<>("OPTION", clValueOption));

        final Deploy deploy = buildStandardTransferDeploy(transferArgs);

        clValueOption = null;

        deployResult = CasperClientProvider.getInstance().getCasperService().putDeploy(deploy);
    }

    @And("the transfer containing the Option value is successfully executed"
    )
    public void theTransferContainingTheOptionValueIsSuccessfullyExecuted() {
        deployData = DeployUtils.waitForDeploy(
                deployResult.getDeployHash(),
                300,
                CasperClientProvider.getInstance().getCasperService()
        );
    }

    @When("the Option is read from the deploy")
    public void theOptionIsReadFromTheDeploy() {
        clValueOption = (CLValueOption) getNamedArgValue(deployData.getDeploy().getSession().getArgs(), "OPTION");
        assertThat(clValueOption, is(notNullValue()));
    }

    @And("the type of the Option is {string} with a value of {string}")
    public void theTypeOfTheOptionIsWithAValueOf(final String typeName, final String strValue) throws Exception {
        assertThat(clValueOption.getClType().getTypeName(), is("Option"));
        assertThat(clValueOption.getValue(), is(notNullValue()));
        assertThat(clValueOption.getValue().get().getValue(), is(this.clValueFactory.createValue(CLTypeData.getTypeByName(typeName), strValue).getValue()));
    }
}
