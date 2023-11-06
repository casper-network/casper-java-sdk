package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.CLValueFactory;
import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.e2e.utils.DeployUtils;
import com.casper.sdk.model.clvalue.CLValueAny;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.NamedArg;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.bouncycastle.util.encoders.Hex;

import java.util.LinkedList;
import java.util.List;

import static com.casper.sdk.e2e.utils.DeployUtils.buildStandardTransferDeploy;
import static com.casper.sdk.e2e.utils.DeployUtils.getNamedArgValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;


/**
 * The Any Value feature step definitions.
 *
 * @author ian@meywood.com
 */
public class AnyValueStepDefinitions {
    private final CLValueFactory factory = new CLValueFactory();
    private CLValueAny clValueAny;
    private DeployResult deployResult;
    private DeployData deployData;

    @Given("an Any value contains a {string} value of {string}")
    public void iHaveASimpleAnyValueContainingAStringValue(final String type, final String strValue) throws Throwable {
        clValueAny = new CLValueAny(Hex.decode(factory.createValue(CLTypeData.getTypeByName(type), strValue).getBytes()));
    }

    @Then("the any value's bytes are {string}")
    public void theAnyValueSBytesAre(String hexBytes) {
        assertThat(clValueAny.getBytes(), is(hexBytes));
    }

    @Given("that the any value is deployed in a transfer as a named argument")
    public void thatTheAnyValueIsDeployedInATransferAsANamedArgument() throws Exception {
        final List<NamedArg<?>> transferArgs = new LinkedList<>();
        transferArgs.add(new NamedArg<>("ANY", clValueAny));

        final Deploy deploy = buildStandardTransferDeploy(transferArgs);

        clValueAny = null;

        deployResult = CasperClientProvider.getInstance().getCasperService().putDeploy(deploy);
    }

    @And("the transfer containing the any value is successfully executed")
    public void theTransferContainingTheAnyValueIsSuccessfullyExecuted() {
        deployData = DeployUtils.waitForDeploy(
                deployResult.getDeployHash(),
                300,
                CasperClientProvider.getInstance().getCasperService()
        );
    }

    @When("the any is read from the deploy")
    public void theAnyIsReadFromTheDeploy() {
        clValueAny = (CLValueAny) getNamedArgValue(deployData.getDeploy().getSession().getArgs(), "ANY");
        assertThat(clValueAny, is(notNullValue()));
    }
}
