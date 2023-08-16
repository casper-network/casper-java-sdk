package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.*;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.helper.CasperDeployHelper;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeOption;
import com.casper.sdk.model.clvalue.cltype.CLTypePublicKey;
import com.casper.sdk.model.clvalue.cltype.CLTypeU512;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.casper.sdk.model.deploy.executabledeploy.Transfer;
import com.casper.sdk.model.deploy.executionresult.Success;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.service.CasperService;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import com.syntifi.crypto.key.Ed25519PublicKey;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

/**
 * Step definitions for the CLValues feature.
 *
 * @author ian@meywood.com
 */
public class CLValuesDefinitions {

    private final Logger logger = LoggerFactory.getLogger(CLValuesDefinitions.class);

    private final ContextMap contextMap = ContextMap.getInstance();

    private final CLValueFactory cLValueFactory = new CLValueFactory();

    private final CasperService casperService = CasperClientProvider.getInstance().getCasperService();

    @Given("that a CL value of type {string} has a value of {string}")
    public void thatACLValueOfTypeHasAValueOf(final String typeName, final String strValue) throws Exception {

        logger.info("Given that a CL value of type {} has a value of {}", typeName, strValue);

        final AbstractCLValue<?, ?> value = cLValueFactory.createValue(CLTypeData.getTypeByName(typeName), strValue);

        addValueToContext(value);
    }

    @Then("it's bytes will be {string}")
    public void itSBytesWillBe(final String hexBytes) {

        final AbstractCLValue<?, ?> clValue = contextMap.get("clValue");
        assertThat(clValue.getBytes(), is(hexBytes));
    }

    @When("the values are added as arguments to a deploy")
    public void theValuesAreAddedAsArgumentsToADeploy() throws Exception {

        final Ed25519PrivateKey senderKey = new Ed25519PrivateKey();
        final Ed25519PublicKey receiverKey = new Ed25519PublicKey();

        senderKey.readPrivateKey(AssetUtils.getUserKeyAsset(1, 1, StepConstants.SECRET_KEY_PEM).getFile());
        receiverKey.readPublicKey(AssetUtils.getUserKeyAsset(1, 2, StepConstants.PUBLIC_KEY_PEM).getFile());

        final List<NamedArg<?>> transferArgs = new LinkedList<>();
        final NamedArg<CLTypeU512> amountNamedArg = new NamedArg<>("amount", new CLValueU512(new BigInteger("2500000000")));
        transferArgs.add(amountNamedArg);
        final NamedArg<CLTypePublicKey> publicKeyNamedArg = new NamedArg<>("target", new CLValuePublicKey(PublicKey.fromAbstractPublicKey(receiverKey)));
        transferArgs.add(publicKeyNamedArg);

        final CLValueOption idArg = new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(200))));
        final NamedArg<CLTypeOption> idNamedArg = new NamedArg<>("id", idArg);
        transferArgs.add(idNamedArg);
        final Transfer session = Transfer.builder().args(transferArgs).build();
        final ModuleBytes payment = CasperDeployHelper.getPaymentModuleBytes(new BigInteger("100000000"));

        final List<NamedArg<?>> clValues = contextMap.get("clValues");
        transferArgs.addAll(clValues);

        final Ttl ttl = Ttl.builder().ttl("30m").build();

        final Deploy deploy = CasperDeployHelper.buildDeploy(
                senderKey,
                "casper-net-1",
                session,
                payment,
                1L,
                ttl,
                new Date(),
                new ArrayList<>()
        );

        this.contextMap.put(StepConstants.PUT_DEPLOY, deploy);
    }

    @Then("the deploy body hash is {string}")
    public void theDeployBodyHashIs(final String bodyHash)  {

        final Deploy deploy = this.contextMap.get(StepConstants.PUT_DEPLOY);
        final Digest digest = deploy.getHeader().getBodyHash();
        assertThat(digest.toString(), is(bodyHash));
    }

    @When("the deploy is put on chain")
    public void theDeployIsPutOnChain() {

        final Deploy deploy = contextMap.get(StepConstants.PUT_DEPLOY);

        final DeployResult deployResult = casperService.putDeploy(deploy);
        assertThat(deployResult.getDeployHash(), is(notNullValue()));
        contextMap.put(StepConstants.DEPLOY_RESULT, deployResult);
    }

    @And("the deploy has successfully executed")
    public void theDeployHasSuccessfullyExecuted() {

        final DeployResult deployResult = this.contextMap.get(StepConstants.DEPLOY_RESULT);
        final DeployData deployData = DeployUtils.waitForDeploy(deployResult.getDeployHash(), 300, this.casperService);
        assertThat(deployData.getExecutionResults().get(0).getResult(), is(instanceOf(Success.class)));
    }

    @When("the deploy is obtained from the node")
    public void theDeployIsObtainedFromTheNode() {

        final DeployResult deployResult = this.contextMap.get(StepConstants.DEPLOY_RESULT);
        final DeployData deploy = this.casperService.getDeploy(deployResult.getDeployHash());
        assertThat(deploy, is(notNullValue()));
        this.contextMap.put(StepConstants.GET_DEPLOY, deploy);
    }

    @Then("the deploys NamedArgument {string} has a value of {string} and bytes of {string}")
    public void theDeploysNamedArgumentHasAValueOfAndBytesOf(final String name,
                                                             final String strValue,
                                                             final String hexBytes) {

        final DeployData deploy = this.contextMap.get(StepConstants.GET_DEPLOY);
        final Optional<NamedArg<?>> optionalNamedArg = deploy.getDeploy().getSession().getArgs().stream().filter(namedArg -> name.equals(namedArg.getType())).findFirst();
        assertThat(optionalNamedArg.isPresent(), is(true));

        final Object value = CLTypeUtils.convertToCLTypeValue(name, strValue);

        final NamedArg<?> namedArg = optionalNamedArg.get();
        assertThat(namedArg.getClValue().getValue(), is(value));
        assertThat(namedArg.getClValue().getBytes(), is(hexBytes));
        assertThat(namedArg.getClValue().getClType().getTypeName(), is(name));
        assertThat(namedArg.getClValue().getBytes(), is(hexBytes));
    }

    @Given("that the CL complex value of type {string} with an internal types of {string} values of {string}")
    public void thatTheCLComplexValueOfTypeWithAnInternalTypesOfValuesOf(final String type, final String innerTypes, final String innerValues) throws Exception {

        final List<CLTypeData> types = getInnerClTypeData(innerTypes);
        final List<String> values = Arrays.asList(innerValues.split(","));
        final AbstractCLValue<?, ?> complexValue = this.cLValueFactory.createComplexValue(CLTypeData.getTypeByName(type), types, values);
        this.addValueToContext(complexValue);
    }


    @And("the deploys NamedArgument Complex value {string} has internal types of {string} and values of {string} and bytes of {string}")
    public void theDeploysNamedArgumentComplexValueHasInternalValuesOfAndBytesOf(final String name,
                                                                                 final String types,
                                                                                 final String values,
                                                                                 final String bytes) throws Exception {

        final DeployData deploy = this.contextMap.get(StepConstants.GET_DEPLOY);
        final Optional<NamedArg<?>> optionalNamedArg = deploy.getDeploy().getSession().getArgs().stream().filter(namedArg -> name.equals(namedArg.getType())).findFirst();
        assertThat(optionalNamedArg.isPresent(), is(true));

        final NamedArg<?> namedArg = optionalNamedArg.get();

        switch (CLTypeData.getTypeByName(namedArg.getType())) {
            case LIST:
                assertList((CLValueList) namedArg.getClValue(), types, values);
                break;

            case MAP:
                assertMap((CLValueMap) namedArg.getClValue(), types, values);
                break;

            case OPTION:
                assertOption((CLValueOption) namedArg.getClValue(), types, values);
                break;

            case TUPLE1:
                assertTupleOne((CLValueTuple1) namedArg.getClValue(), types, values);
                break;

            case TUPLE2:
                assertTupleTwo((CLValueTuple2) namedArg.getClValue(), types, values);
                break;

            case TUPLE3:
                assertTupleThree((CLValueTuple3) namedArg.getClValue(), types, values);
                break;
            default:
                throw new IllegalArgumentException("Not a supported  complex type " + namedArg.getType());
        }

        assertThat(namedArg.getClValue().getBytes(), is(bytes));
    }

    private void assertList(final CLValueList clValue, final String types, final String values) throws Exception {

        final CLValueList complexValue = (CLValueList) this.cLValueFactory.createComplexValue(CLTypeData.LIST, getInnerClTypeData(types), Arrays.asList(values.split(",")));

        final List<? extends AbstractCLValue<?, ?>> value = clValue.getValue();
        assertThat(value, hasSize(5));

        final Iterator<? extends AbstractCLValue<?, ?>> iterator = complexValue.getValue().iterator();

        for (AbstractCLValue<?, ?> abstractCLValue : value) {
            assertClValues(abstractCLValue, iterator.next());
        }
    }

    private void assertMap(final CLValueMap clValue, final String types, final String values) throws Exception {

        final CLValueMap complexValue = (CLValueMap) this.cLValueFactory.createComplexValue(CLTypeData.MAP, getInnerClTypeData(types), Arrays.asList(values.split(",")));

        final Set<? extends Map.Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>>> thatEntries = complexValue.getValue().entrySet();
        final Iterator<? extends Map.Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>>> iterEntries = clValue.getValue().entrySet().iterator();

        for (Map.Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> entry : thatEntries) {

            final Map.Entry<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> next = iterEntries.next();

            assertClValues(entry.getKey(), next.getKey());
            assertClValues(entry.getValue(), next.getValue());
        }
    }

    private void assertTupleThree(final CLValueTuple3 clValue, final String types, final String values) throws Exception {

        final CLValueTuple3 complexValue = (CLValueTuple3) this.cLValueFactory.createComplexValue(CLTypeData.TUPLE3, getInnerClTypeData(types), Arrays.asList(values.split(",")));

        assertClValues(clValue.getValue().getValue0(), complexValue.getValue().getValue0());
        assertClValues(clValue.getValue().getValue1(), complexValue.getValue().getValue1());
        assertClValues(clValue.getValue().getValue2(), complexValue.getValue().getValue2());
    }

    private void assertTupleTwo(CLValueTuple2 clValue, String types, String values) throws Exception {

        final CLValueTuple2 complexValue = (CLValueTuple2) this.cLValueFactory.createComplexValue(CLTypeData.TUPLE2, getInnerClTypeData(types), Arrays.asList(values.split(",")));

        assertClValues(clValue.getValue().getValue0(), complexValue.getValue().getValue0());
        assertClValues(clValue.getValue().getValue1(), complexValue.getValue().getValue1());
    }

    private void assertTupleOne(final CLValueTuple1 clValue, final String types, final String values) throws Exception {

        final AbstractCLValue<?, ?> innerValue = this.cLValueFactory.createValue(CLTypeData.getTypeByName(types), values);
        assertClValues(clValue.getValue().getValue0(), innerValue);
    }

    private void assertOption(final CLValueOption clValue, final String types, final String values) throws Exception {

        final AbstractCLValue<?, ?> innerValue = this.cLValueFactory.createValue(CLTypeData.getTypeByName(types), values);
        assertThat(clValue.getValue().isPresent(), is(true));
        assertClValues(clValue.getValue().get(), innerValue);
    }

    private void assertClValues(final AbstractCLValue<?, ?> actual, final AbstractCLValue<?, ?> expected) {

        assertThat(actual.getValue(), is(expected.getValue()));
        assertThat(actual.getClType(), is(expected.getClType()));
    }


    private void addValueToContext(final AbstractCLValue<?, ?> value) {

        contextMap.put("clValue", value);

        List<NamedArg<?>> clValues = contextMap.get("clValues");
        if (clValues == null) {
            clValues = new LinkedList<>();
            contextMap.put("clValues", clValues);
        }

        clValues.add(new NamedArg<>(value.getClType().getTypeName(), value));
    }

    private List<CLTypeData> getInnerClTypeData(final String innerTypes) {
        return Arrays.stream(innerTypes.split(","))
                .map(strType -> {
                    try {
                        return CLTypeData.getTypeByName(strType.trim());
                    } catch (NoSuchTypeException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }
}
