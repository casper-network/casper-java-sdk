package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.AssetUtils;
import com.casper.sdk.e2e.utils.CLValueFactory;
import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.e2e.utils.DeployUtils;
import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.casper.sdk.model.clvalue.CLValueAny;
import com.casper.sdk.model.clvalue.CLValueMap;
import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.bouncycastle.util.encoders.Hex;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
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
    private CLValueMap clValueMap;

    @Given("an Any value contains a {string} value of {string}")
    public void iHaveASimpleAnyValueContainingAStringValue(final String type, final String strValue) throws Throwable {
        clValueAny = new CLValueAny(Hex.decode(strValue));
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

    //@And("the type of the any is {string} with a value of {string}")
    public void theTypeOfTheAnyIsWithAValueOf(String typeName, String strValue) throws Exception {
        assertThat(clValueAny.getClType().getTypeName(), is("Any"));
        assertThat(clValueAny.getValue(), is(notNullValue()));
        // TODO this should really be an inner AbstractCLValue not bytes
        assertThat(clValueAny.getValue(), is(Hex.decode(factory.createValue(CLTypeData.getTypeByName(typeName), strValue).getBytes())));
    }

    @Given("that the map of public keys to any types is read from resource {string}")
    public void thatTheMapOfPublicKeysToAnyTypesIsReadFromResource(final String jsonResource) throws IOException {
        final URL jsonUrl = AssetUtils.getStandardTestResourceURL("/json/" + jsonResource);
        clValueMap = new ObjectMapper().readValue(jsonUrl, CLValueMap.class);
    }

    @Then("the loaded CLMap will contain {int} elements")
    public void theLoadedCLMapWillContainElements(int count) {
        assertThat(clValueMap.getValue().size(), is(count));
    }

    @And("the nested map key type will be {string}")
    public void theMapKeyTypeWillBe(final String type) {
        final CLValueMap innerMap = getInnerMap();
        assertThat(innerMap.getValue().keySet().iterator().next().getClType().getTypeName(), is(type));
    }

    @And("the nested map value type will be {string}")
    public void theMapValueTypeWillBe(final String type) {
        final CLValueMap innerMap = getInnerMap();
        assertThat(innerMap.getValue().values().iterator().next().getClType().getTypeName(), is(type));
    }

    @And("the maps bytes will be {string}")
    public void theMapsBytesWillBe(final String hexBytes) {
        assertThat(clValueMap.getBytes(), is(hexBytes));
    }

    @And("the nested map keys value will be {string}")
    public void theKeysValueWillBe(final String keyHexBytes) throws Exception {
        assertThat(clValueMap.getValue().keySet().iterator().next(), is(new CLValuePublicKey(PublicKey.fromTaggedHexString(keyHexBytes))));
    }

    @And("the nested map any values bytes length will by {int}")
    public void theAnyValuesBytesLengthWillBy(int len) {
        final CLValueAny innerValue = (CLValueAny) getInnerMap().getValue().values().iterator().next();
        assertThat(innerValue.getValue().length, is(len));
    }

    @And("the nested map any values bytes will by {string}")
    public void theAnyValuesBytesWillBy(final String hexBytes) {
        final CLValueAny innerValue = (CLValueAny) getInnerMap().getValue().values().iterator().next();
        assertThat(innerValue.getBytes(), is(hexBytes));
    }

    private CLValueMap getInnerMap() {
        return (CLValueMap) clValueMap.getValue().values().iterator().next();
    }
}
