package com.casper.sdk.model.entity;

import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.model.AbstractJsonTests;
import com.casper.sdk.model.account.Account;
import com.casper.sdk.model.clvalue.cltype.CLTypeKey;
import com.casper.sdk.model.clvalue.cltype.CLTypeU256;
import com.casper.sdk.model.contract.Contract;
import com.casper.sdk.model.contract.NamedKey;
import com.casper.sdk.model.contract.entrypoint.*;
import com.casper.sdk.model.key.Key;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class StateGetEntityTest extends AbstractJsonTests {
    @Test
    void validateGetStateEntityAccount() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-account-test.json"));

        final StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);
        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));
        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(AccountKind.class)));
    }

    @Test
    void validateGetStateEntitySmartContract() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-smartcontract-test.json"));

        final StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);
        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));
        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(SmartContractKind.class)));
    }

    @Test
    void validateGetStateEntitySystemEntryPointV1() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-system-entry-point-v1-test.json"));

        final StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);
        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));
        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(SystemKind.class)));
        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntryPoints().size(), is(greaterThan(0)));

        final AddressableEntity entity = (AddressableEntity) addressableEntity.getEntity();
        assertThat(entity.getEntryPoints().get(0), is(instanceOf(EntryPointValue.class)));
        assertThat(entity.getEntryPoints().get(0).getV1(), is(instanceOf(EntryPoint.class)));
    }

    @Test
    void validateGetStateEntitySystemEntryPointV2() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-system-entry-point-v2-test.json"));

        final StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);
        assertInstanceOf(AddressableEntity.class, addressableEntity.getEntity());
        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntryPoints().size(), is(greaterThan(0)));

        final AddressableEntity entity = (AddressableEntity) addressableEntity.getEntity();
        assertThat(entity.getEntryPoints().get(0), is(instanceOf(EntryPointValue.class)));
        assertThat(entity.getEntryPoints().get(0).getV2(), is(instanceOf(EntryPointV2.class)));
    }

    @Test
    void validateGetStateEntityLegacyAccount() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-legacy-account-test.json"));

        final StateEntityResult entity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);
        assertInstanceOf(com.casper.sdk.model.account.Account.class, entity.getEntity());
    }

    @Test
    void validateGetStateEntityActualSmartContractCctlReturnedResult() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-smartcontract-actual-from-cctl.json"));

        final StateEntityResult entity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);
        assertInstanceOf(AddressableEntity.class, entity.getEntity());
        assertInstanceOf(SmartContractKind.class, ((AddressableEntity) entity.getEntity()).getEntity().getEntityAddressKind());

        final AddressableEntity contract = (AddressableEntity) entity.getEntity();

        assertThat(contract.getNamedKeys().size(), is(11));
        assertThat(contract.getEntryPoints().size(), is(15));

        assertThat(contract.getNamedKeys().get(0).getName(), is("allowances"));
        assertThat(contract.getNamedKeys().get(0).getKey().toString(), is("uref-5e1239586b122bfe8ec9e3285f375d060ffbf90599fade7e807027fd228275cf-007"));

        assertThat(contract.getEntryPoints().get(14).getV1().getName(), is("balance_of"));
    }

    @Test
    void validateGetStateEntityAccountCondor() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-account-condor-test.json"));

        final StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);
        assertThat(addressableEntity.getEntity(), is(instanceOf(Account.class)));

        final Account account = (Account) addressableEntity.getEntity();
        assertThat(account.getNamedKeys().size(), is(0));
        assertThat(account.getDeployment().getDeployment(), is(1));
        assertThat(account.getHash().toString(), is("account-hash-5a9eb1f7da515d9fa2f0b74e18ec84cccf90f146269d538073416dff432a3c77"));
    }


    @Test
    void validateGetStateEntityContractCondorFromTestnet() throws IOException, NoSuchKeyTagException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-contract-testnet-actual.json"));

        final StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);
        assertThat(addressableEntity.getEntity(), is(instanceOf(ContractEntity.class)));

        ContractEntity contractEntity = (ContractEntity) addressableEntity.getEntity();
        assertThat(contractEntity.getWasm().getWasm().getBytes().length(), is(589812));
        assertThat(contractEntity.getWasm().getMerkleProof().length(), is(617628));

        final Contract contract = contractEntity.getContract();
        assertThat(contract.getPackageHash(), is("contract-package-c659b8ee610dde0e8f3f89b86028ac1c9343811e88c9b0a2f0547cab88df2267"));
        assertThat(contract.getWasmHash(), is("contract-wasm-0e1f45d146a3148fc79f204238965cf6296b1cc1eb97f599d274df1bece896b5"));
        assertThat(contract.getNamedKeys(), hasSize(12));

        final NamedKey namedKey = contract.getNamedKeys().get(11);
        assertThat(namedKey.getName(), is("total_supply"));
        assertThat(namedKey.getKey(), is(Key.create("uref-033feb40c15ad15705f3670e58241841804aa13d7f4fd59ac6c0176a80ef25c6-007")));

        assertThat(contract.getEntryPoint("allowance").isPresent(), is(true));
        final EntryPointV1 allowance = contract.getEntryPoint("allowance").get();
        assertThat(allowance.getEntryPointType(), is(EntryPointType.CALLED));
        assertThat(allowance.getName(), is("allowance"));
        assertThat(allowance.getRet(), is(instanceOf(CLTypeU256.class)));
        assertThat(allowance.getArgs(), hasSize(2));
        assertThat(allowance.getArgs().get(1).getName(), is("spender"));
        assertThat(allowance.getArgs().get(1).getClType(), is(instanceOf(CLTypeKey.class)));
    }
}
