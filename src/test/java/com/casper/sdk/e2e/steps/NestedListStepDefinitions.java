package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.CLValueFactory;
import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.e2e.utils.DeployUtils;
import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.casper.sdk.model.clvalue.CLValueList;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.NamedArg;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Arrays;
import java.util.Collections;

import static com.casper.sdk.e2e.utils.DeployUtils.getNamedArgValue;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * The step definitions for the nested list feature.
 *
 * @author ian@meywood.com
 */
public class NestedListStepDefinitions {

    private CLValueList clValueList;
    private String deployHash;
    private DeployData deployData;
    private final CLValueFactory clValueFactory = new CLValueFactory();

    @Given("^a list is created with U(\\d+) values of \\((\\d+), (\\d+), (\\d+)\\)$")
    public void aListIsCreatedWithUValuesOf(final int numberLen,
                                            final int val1,
                                            final int val2,
                                            final int val3) throws Exception {
        clValueList = new CLValueList(
                asList(createNumericValue(numberLen, val1), createNumericValue(numberLen, val2), createNumericValue(numberLen, val3))
        );
    }

    @Given("a list is created with I{int} values of \\({int}, {int}, {int})")
    public void aListIsCreatedWithIValuesOf(final int numberLen,
                                            final int val1,
                                            final int val2,
                                            final int val3) throws Exception {
        clValueList = new CLValueList(asList(
                createValue("I" + numberLen, Integer.toString(val1)),
                createValue("I" + numberLen, Integer.toString(val2)),
                createValue("I" + numberLen, Integer.toString(val3))
        ));
    }

    @And("the list's {string} item is a CLValue with I{int} value of {int}")
    public void theListSItemIsACLValueWithIValueOf(final String nth, final int type, final int value) throws Exception {
        final AbstractCLValue<?, ?> clValue = getClValue(nth);
        assertThat(clValue.getClType().getTypeName(), is("I" + type));
        assertThat(clValue.getValue(), is(createValue("I" + type, Integer.toString(value)).getValue()));
    }

    @Then("^the list's bytes are \"([^\"]*)\"$")
    public void theListSBytesAre(String hexBytes) {
        assertThat(clValueList.getBytes(), is(hexBytes));
    }

    @And("^the list's length is (\\d+)$")
    public void theListSLengthIs(int length) {
        assertThat(clValueList.getValue().size(), is(length));
    }

    @And("^the list's \"([^\"]*)\" item is a CLValue with U(\\d+) value of (\\d+)$")
    public void theListSItemIsACLValueWithUValueOf(final String index, final int numberLength, final int value) throws Throwable {
        final AbstractCLValue<?, ?> clValue = getClValue(index);
        assertThat(clValue.getClType().getTypeName(), is("U" + numberLength));
        assertThat(clValue.getValue(), is(createNumericValue(numberLength, value).getValue()));
    }

    @Given("a list is created with {string} values of \\({string}, {string}, {string})")
    public void aListIsCreatedWithValuesOf(final String type, final String val1, final String val2, final String val3) throws Exception {
        clValueList = new CLValueList(
                Arrays.asList(createValue(type, val1), createValue(type, val2), createValue(type, val3))
        );
    }

    @And("the list's {string} item is a CLValue with {string} value of {string}")
    public void theListSItemIsACLValueWithValueOf(final String index, final String type, final String value) throws Throwable {
        final AbstractCLValue<?, ?> clValue = getClValue(index);
        assertThat(clValue.getClType().getTypeName(), is(type));
        assertThat(clValue.getValue(), is(createValue(type, value).getValue()));
    }

    @Given("a nested list is created with U{int} values of \\(\\({int}, {int}, {int}),\\({int}, {int}, {int}))")
    public void aNestedListIsCreatedWithValuesOf(final int type,
                                                 final int val1,
                                                 final int val2,
                                                 final int val3,
                                                 final int val4,
                                                 final int val5,
                                                 final int val6) throws Exception {
        clValueList = new CLValueList(Arrays.asList(
                new CLValueList(
                        Arrays.asList(
                                createNumericValue(type, val1),
                                createNumericValue(type, val2),
                                createNumericValue(type, val3)
                        )
                ),
                new CLValueList(
                        Arrays.asList(
                                createNumericValue(type, val4),
                                createNumericValue(type, val5),
                                createNumericValue(type, val6)
                        )
                )
        ));
    }

    @And("the {string} nested list's {string} item is a CLValue with U{int} value of {int}")
    public void theNestedListSItemIsACLValueWithValueOf(String nth, String nestedNth, int type, int val) throws Exception {
        CLValueList nestedList = (CLValueList) getClValue(nth);
        AbstractCLValue<?, ?> clValue = getClValue(nestedList, nestedNth);
        assertThat(clValue.getClType().getTypeName(), is("U" + type));
        assertThat(clValue.getValue(), is(createNumericValue(type, val).getValue()));
    }

    @Given("that the list is deployed in a transfer")
    public void thatTheListIsDeployedInATransfer() throws Exception {

        final Deploy deploy = DeployUtils.buildStandardTransferDeploy(
                Collections.singletonList(new NamedArg<>("LIST", clValueList))
        );

        deployHash = CasperClientProvider.getInstance().getCasperService().putDeploy(deploy).getDeployHash();
    }

    @And("the transfer containing the list is successfully executed")
    public void theTransferContainingTheListIsSuccessfullyExecuted() {
        deployData = DeployUtils.waitForDeploy(
                deployHash,
                300,
                CasperClientProvider.getInstance().getCasperService()
        );
    }

    @When("the list is read from the deploy")
    public void theListIsReadFromTheDeploy() {
        clValueList = (CLValueList) getNamedArgValue(deployData.getDeploy().getSession().getArgs(), "LIST");
        assertThat(clValueList, is(notNullValue()));
    }


    private AbstractCLValue<?, ?> getClValue(final String index) {
        return getClValue(clValueList, index);
    }

    private AbstractCLValue<?, ?> getClValue(final CLValueList list, final String index) {
        return list.getValue().get(Integer.parseInt(index.substring(0, 1)) - 1);
    }


    private AbstractCLValue<?, ?> createNumericValue(final int numberLen, final int value) throws Exception {
        return clValueFactory.createValue(CLTypeData.getTypeByName("U" + numberLen), Integer.toString(value));
    }

    private AbstractCLValue<?, ?> createValue(final String type, final String strValue) throws Exception {
        return clValueFactory.createValue(CLTypeData.getTypeByName(type), strValue);

    }


}
