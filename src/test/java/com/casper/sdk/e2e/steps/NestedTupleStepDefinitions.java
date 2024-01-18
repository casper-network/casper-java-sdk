package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.exception.NotImplementedException;
import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.e2e.utils.DeployUtils;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.NamedArg;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.javatuples.Unit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.casper.sdk.e2e.utils.DeployUtils.buildStandardTransferDeploy;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Step definitions for nested Tuple values.
 *
 * @author ian@meywood.com
 */
public class NestedTupleStepDefinitions {

    public static final String FIRST = "first";
    public static final String SECOND = "second";
    public static final String THIRD = "third";
    private static CLValueTuple2 tuple2Root;
    private static CLValueTuple1 tuple1Root;
    private static CLValueTuple3 tuple3Root;
    private DeployResult deployResult;
    private DeployData deployData;

    @Given("that a nested Tuple1 is defined as \\(\\({long})) using U32 numeric values")
    public void thatANestedTupleIsDefinedAsUsingUNumericValue(final long arg0) throws ValueSerializationException {
        tuple1Root = new CLValueTuple1(Unit.with(new CLValueTuple1(Unit.with(new CLValueU32(arg0)))));
    }

    @Then("the {string} element of the Tuple{int} is {string}")
    public void theNthElementOfTheNestedTupleXIs(final String elementIndex, final int tuple, final String tupleStr) {
        final AbstractCLValue<?, ?> clValue = getTupleValue(tuple, elementIndex.toLowerCase());
        final List<Long> actualTupleValues = getTupleValues(clValue);
        final List<Long> expectedTupleValues = getTupleValues(tupleStr);
        assertThat(actualTupleValues, is(expectedTupleValues));
    }

    @Then("the {string} element of the Tuple{int} is {long}")
    public void theNthElementOfTheTupleXIs(final String elementIndex, final int tuple, final long expected) {
        final AbstractCLValue<?, ?> clValue = getTupleValue(tuple, elementIndex.toLowerCase());
        final long actual = (Long) clValue.getValue();
        assertThat(actual, is(expected));
    }

    @Given("that a nested Tuple2 is defined as \\({long}, \\({long}, \\({long}, {long}))) using U32 numeric values")
    public void thatATupleIsDefinedAs(final long arg0,
                                      final long arg1,
                                      final long arg2,
                                      final long arg3) throws ValueSerializationException {
        tuple2Root = new CLValueTuple2(Pair.with(
                new CLValueU32(arg0),
                new CLValueTuple2(Pair.with(
                        new CLValueU32(arg1),
                        new CLValueTuple2(Pair.with(
                                new CLValueU32(arg2),
                                new CLValueU32(arg3)
                        ))
                ))
        ));
    }

    @And("the Tuple{long} bytes are {string}")
    public void theTuplesBytesAre(final long tuple, final String hexBytes) {
        assertThat(getTuple(tuple).getBytes(), is(hexBytes));
    }

    @Given("that a nested Tuple3 is defined as \\({long}, {long}, \\({long}, {long}, \\({long}, {long}, {long}))) using U32 numeric values")
    public void thatANestedTupleIsDefinedAsUsingUNumericValues(final long arg0,
                                                               final long arg1,
                                                               final long arg2,
                                                               final long arg3,
                                                               final long arg4,
                                                               final long arg5,
                                                               final long arg6) throws ValueSerializationException {
        tuple3Root = new CLValueTuple3(Triplet.with(
                new CLValueU32(arg0),
                new CLValueU32(arg1),
                new CLValueTuple3(Triplet.with(
                        new CLValueU32(arg2),
                        new CLValueU32(arg3),
                        new CLValueTuple3(Triplet.with(
                                new CLValueU32(arg4),
                                new CLValueU32(arg5),
                                new CLValueU32(arg6)
                        ))
                ))
        ));
    }


    @Given("that the nested tuples are deployed in a transfer")
    public void thatTheNestedTuplesAreDeployedInATransfer() throws Exception {

        final List<NamedArg<?>> transferArgs = new LinkedList<>();
        transferArgs.add(new NamedArg<>("TUPLE_1", tuple1Root));
        transferArgs.add(new NamedArg<>("TUPLE_2", tuple2Root));
        transferArgs.add(new NamedArg<>("TUPLE_3", tuple3Root));

        final Deploy deploy = buildStandardTransferDeploy(transferArgs);

        // Clear out tuples as we need to obtain them from the deploy result and need to ensure existing values are not used
        tuple1Root = null;
        tuple2Root = null;
        tuple3Root = null;

        deployResult = CasperClientProvider.getInstance().getCasperService().putDeploy(deploy);
    }

    @And("the transfer is successful")
    public void theTransferIsSuccessful() {
        deployData = DeployUtils.waitForDeploy(
                deployResult.getDeployHash(), 300,
                CasperClientProvider.getInstance().getCasperService()
        );
        tuple1Root = (CLValueTuple1) getNamedArgValue("TUPLE_1", deployData.getDeploy().getSession().getArgs());
        tuple2Root = (CLValueTuple2) getNamedArgValue("TUPLE_2", deployData.getDeploy().getSession().getArgs());
        tuple3Root = (CLValueTuple3) getNamedArgValue("TUPLE_3", deployData.getDeploy().getSession().getArgs());
    }

    @When("the tuples deploy is obtained from the node")
    public void theDeployIsObtainedFromTheNode() {
        assertThat(deployData, is(notNullValue()));
    }

    private AbstractCLValue<?, ?> getNamedArgValue(final String type, final List<NamedArg<?>> namedArgs) {
        return namedArgs.stream()
                .filter(namedArg -> type.equals(namedArg.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Named arg " + type + " not found"))
                .getClValue();
    }

    private AbstractCLValue<?, ?> getTuple(final long tuple) {
        if (tuple == 1) {
            return tuple1Root;
        } else if (tuple == 2) {
            return tuple2Root;
        } else if (tuple == 3) {
            return tuple3Root;
        } else {
            throw new NotImplementedException("Tuple " + tuple + " not implemented");
        }
    }

    private AbstractCLValue<?, ?> getTupleValue(final long tuple, final String valueIndex) {
        if (tuple == 1) {
            if (FIRST.equals(valueIndex)) {
                return tuple1Root.getValue().getValue0();
            }

        } else if (tuple == 2) {
            if (FIRST.equals(valueIndex)) {
                return tuple2Root.getValue().getValue0();
            } else if (SECOND.equals(valueIndex)) {
                return tuple2Root.getValue().getValue1();
            }
        } else if (tuple == 3) {
            if (FIRST.equals(valueIndex)) {
                return tuple3Root.getValue().getValue0();
            } else if (SECOND.equals(valueIndex)) {
                return tuple3Root.getValue().getValue1();
            } else if (THIRD.equals(valueIndex)) {
                return tuple3Root.getValue().getValue2();
            }
        }

        throw new NotImplementedException("Tuple " + tuple + " " + valueIndex + " not implemented");
    }

    private List<Long> getTupleValues(final AbstractCLValue<?, ?> tuple) {
        return getTupleValues(tuple, new ArrayList<>());
    }

    private List<Long> getTupleValues(String str) {
        str = str.replaceAll("\\(", "");
        str = str.replaceAll("\\)", "");

        final List<Long> tupleValues = new ArrayList<>();
        for (String s : str.split(",")) {
            tupleValues.add(Long.valueOf(s.trim()));
        }
        return tupleValues;
    }

    private List<Long> getTupleValues(final AbstractCLValue<?, ?> tuple, final ArrayList<Long> tupleValues) {
        if (tuple instanceof CLValueTuple1) {
            processTupleValue(tupleValues, ((CLValueTuple1) tuple).getValue().getValue0());
        } else if (tuple instanceof CLValueTuple2) {
            processTupleValue(tupleValues, ((CLValueTuple2) tuple).getValue().getValue0());
            processTupleValue(tupleValues, ((CLValueTuple2) tuple).getValue().getValue1());
        } else if (tuple instanceof CLValueTuple3) {
            processTupleValue(tupleValues, ((CLValueTuple3) tuple).getValue().getValue0());
            processTupleValue(tupleValues, ((CLValueTuple3) tuple).getValue().getValue1());
            processTupleValue(tupleValues, ((CLValueTuple3) tuple).getValue().getValue2());
        }
        return tupleValues;
    }

    private void processTupleValue(final ArrayList<Long> tupleValues, final Object value) {
        if (value instanceof CLValueU32) {
            tupleValues.add(((CLValueU32) value).getValue());
        } else {
            getTupleValues((AbstractCLValue<?, ?>) value, tupleValues);
        }
    }
}
