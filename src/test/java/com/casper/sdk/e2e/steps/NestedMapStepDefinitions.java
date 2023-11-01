package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.e2e.utils.DeployUtils;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.NamedArg;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.casper.sdk.e2e.utils.DeployUtils.buildStandardTransferDeploy;
import static com.casper.sdk.e2e.utils.DeployUtils.getNamedArgValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Step definitions for the nested map feature.
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

    @Then("the map's key type is {string} and the maps value type is {string}")
    public void theMapsKeyTypeIsAndTheMapsValueTypeIs(final String keyType, final String valueType) {
        assertThat(map.getClType().getKeyValueTypes().getKeyType().getTypeName(), is(keyType));
        assertThat(map.getClType().getKeyValueTypes().getValueType().getTypeName(), is(valueType));
    }

    @Then("the map's bytes are {string}")
    public void theMapSBytesAre(final String hexBytes) {
        assertThat(map.getBytes(), is(hexBytes));
    }

    @Given("a nested map is created \\{{string}: \\{{string}: {int}}, {string}: \\{{string}, {int}}}")
    public void aMapIsCreated(String key0, String key1, int value1, String key2, String key3, int value3) throws Exception {

        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap = new LinkedHashMap<>();
        innerMap.put(new CLValueString(key1), new CLValueU32((long) value1));

        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap2 = new LinkedHashMap<>();
        innerMap2.put(new CLValueString(key3), new CLValueU32((long) value3));

        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> rootMap = new LinkedHashMap<>();

        rootMap.put(new CLValueString(key0), new CLValueMap(innerMap));
        rootMap.put(new CLValueString(key2), new CLValueMap(innerMap2));
        map = new CLValueMap(rootMap);
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

        final String json = new ObjectMapper().writeValueAsString(deploy);
        System.out.println(json);

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

    }

    @Given("a nested map is created  \\{{int}: \\{{int}: \\{{int}: {string}}, {int}: \\{{int}: {string}}}, {int}: \\{{int}: \\{{int}: {string}}, {int}: \\{{int}: {string}}}}")
    public void aMapIsCreated(final int key1,
                              final int key11,
                              final int key111,
                              final String value111,
                              final int key12,
                              final int key121,
                              final String value121,
                              final int key2,
                              final int key21,
                              final int key211,
                              final String value211,
                              final int key22,
                              final int key221,
                              final String value221) throws Exception {


        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap111 = new LinkedHashMap<>();
        innerMap111.put(new CLValueU256(BigInteger.valueOf(key111)), new CLValueString(value111));
        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap121 = new LinkedHashMap<>();
        innerMap121.put(new CLValueU32((long) key121), new CLValueString(value121));

        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap11 = new LinkedHashMap<>();
        innerMap11.put(new CLValueU256(BigInteger.valueOf(key11)), new CLValueMap(innerMap111));
        innerMap11.put(new CLValueU256(BigInteger.valueOf(key12)), new CLValueMap(innerMap121));


        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap211 = new LinkedHashMap<>();
        innerMap211.put(new CLValueU256(BigInteger.valueOf(key211)), new CLValueString(value211));
        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap221 = new LinkedHashMap<>();
        innerMap221.put(new CLValueU32((long) key221), new CLValueString(value221));

        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> innerMap21 = new LinkedHashMap<>();
        innerMap21.put(new CLValueU256(BigInteger.valueOf(key21)), new CLValueMap(innerMap211));
        innerMap21.put(new CLValueU256(BigInteger.valueOf(key22)), new CLValueMap(innerMap221));

        final Map<AbstractCLValue<?, ?>, AbstractCLValue<?, ?>> rootMap = new LinkedHashMap<>();
        rootMap.put(new CLValueU256(BigInteger.valueOf(key1)), new CLValueMap(innerMap11));
        rootMap.put(new CLValueU256(BigInteger.valueOf(key2)), new CLValueMap(innerMap21));

        map = new CLValueMap(rootMap);
    }

    @When("the map is read from the deploy")
    public void theMapIsReadFromTheDeploy() {
        map = (CLValueMap) getNamedArgValue("MAP", deployData.getDeploy().getSession().getArgs());
        assertThat(map, is(notNullValue()));
    }

    @SuppressWarnings("unchecked")
    @And("the map's key is {string} and value is {string}")
    public void theMapsKeyIsAndValueIs(final String strKey, final String strValue) throws ValueSerializationException {
        final CLValueString key = new CLValueString(strKey);
        final Map<CLValueString, CLValueU32> innerMap = (Map<CLValueString, CLValueU32>) map.getValue();
        assertThat(innerMap, is(notNullValue()));
        assertThat(innerMap.containsKey(key), is(true));
        assertThat(innerMap.get(key).getValue().toString(), is(strValue));
    }

    @SuppressWarnings("unchecked")
    @And("the 1st nested map's key is {string} and value is {string}")
    public void theStNestedMapSKeyIsAndValueIs(final String strKey, final String strValue) throws ValueSerializationException {
        final CLValueString key = new CLValueString(strKey);
        final Map<CLValueString, CLValueMap> innerMap = (Map<CLValueString, CLValueMap>) map.getValue().values().iterator().next().getValue();
        assertThat(innerMap, is(notNullValue()));
        assertThat(innerMap.containsKey(key), is(true));
        assertThat(innerMap.get(key).getValue().toString(), is(strValue));
    }
}
