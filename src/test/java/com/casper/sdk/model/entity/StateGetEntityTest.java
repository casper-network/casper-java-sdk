package com.casper.sdk.model.entity;

import com.casper.sdk.model.AbstractJsonTests;
import com.casper.sdk.model.account.Account;
import com.casper.sdk.model.contract.EntryPoint;
import com.casper.sdk.model.contract.EntryPointV2;
import com.casper.sdk.model.contract.EntryPointValue;
import com.casper.sdk.model.entity.kind.EntityAccountKind;
import com.casper.sdk.model.entity.kind.EntitySmartContractKind;
import com.casper.sdk.model.entity.kind.EntitySystemKind;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class StateGetEntityTest extends AbstractJsonTests {
    @Test
    void validateGetStateEntityAccount() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-account-test.json"));

        final StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);


        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));
        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(EntityAccountKind.class)));
    }

    @Test
    void validateGetStateEntitySmartContract() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-smartcontract-test.json"));

        final StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);
        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));
        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(EntitySmartContractKind.class)));
    }

    @Test
    void validateGetStateEntitySystemEntryPointV1() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-system-entry-point-v1-test.json"));

        final StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);
        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));
        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(EntitySystemKind.class)));
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
        assertInstanceOf(EntitySmartContractKind.class, ((AddressableEntity) entity.getEntity()).getEntity().getEntityAddressKind());

        final AddressableEntity contract =  (AddressableEntity) entity.getEntity();

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

}
