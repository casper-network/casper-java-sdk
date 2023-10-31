package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.e2e.utils.DeployUtils;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.NamedArg;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.casper.sdk.e2e.utils.DeployUtils.buildStandardTransferDeploy;
import static com.casper.sdk.e2e.utils.DeployUtils.getNamedArgValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author ian@meywood.com
 */
public class NestedMapStepDefinitions {

    private CLValueMap map;
    private DeployResult deployResult;
    private DeployData deployData;

    @Given("^a map is created \\{\"([^\"]*)\": (\\d+)\\}$")
    public void aMapIsCreated(final String key, final long value) throws Exception {
        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap = new LinkedHashMap<>();
        innerMap.put(new CLValueString(key), new CLValueU32(value));
        map = new CLValueMap(innerMap);
    }

    @Then("the map's bytes are {string}")
    public void theMapSBytesAre(final String hexBytes) {
        assertThat(map.getBytes(), is(hexBytes));
    }

    @Given("a map is created \\{{string}: {int}, {string}: \\{{string}: {int}}}")
    public void aMapIsCreated2(final String key0,
                               final int value0,
                               final String key1,
                               final String key2,
                               final int value2) throws Exception {
        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap = new LinkedHashMap<>();
        innerMap.put(new CLValueString(key0), new CLValueU32((long) value0));
        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap2 = new LinkedHashMap<>();
        innerMap2.put(new CLValueString(key2), new CLValueU32((long) value2));
        innerMap.put(new CLValueString(key1), new CLValueMap(innerMap2));
        map = new CLValueMap(innerMap);
    }

    @Given("a map is created \\{{string}: {int}, {string}: \\{{string}: {int}, {string}: \\{{string}: {int}}}}")
    public void aMapIsCreated(final String key0,
                              final int value0,
                              final String key1,
                              final String key2,
                              final int value2,
                              final String key3,
                              final String key4,
                              final int value4) throws Exception {
        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap = new LinkedHashMap<>();
        innerMap.put(new CLValueString(key0), new CLValueU32((long) value0));
        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap2 = new LinkedHashMap<>();
        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap3 = new LinkedHashMap<>();
        innerMap3.put(new CLValueString(key4), new CLValueU32((long) value4));
        innerMap2.put(new CLValueString(key2), new CLValueU32((long) value2));
        innerMap2.put(new CLValueString(key3), new CLValueMap(innerMap3));
        innerMap.put(new CLValueString(key1), new CLValueMap(innerMap2));
        map = new CLValueMap(innerMap);
    }

    @Given("that the nested map is deployed in a transfer")
    public void thatTheNestedTuplesAreDeployedInATransfer() throws Exception {

        final List<NamedArg<?>> transferArgs = new LinkedList<>();
        transferArgs.add(new NamedArg<>("MAP", map));

        final Deploy deploy = buildStandardTransferDeploy(transferArgs);

        map = null;

        deployResult = CasperClientProvider.getInstance().getCasperService().putDeploy(deploy);
    }

    @And("the transfer containing the nested map is successfully executed")
    public void theTransferIsSuccessful() {
        deployData = DeployUtils.waitForDeploy(
                deployResult.getDeployHash(),
                300,
                CasperClientProvider.getInstance().getCasperService()
        );
        map = (CLValueMap) getNamedArgValue("MAP", deployData.getDeploy().getSession().getArgs());
    }
}
