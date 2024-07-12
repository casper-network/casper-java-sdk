package com.casper.sdk.model.entity;

import com.casper.sdk.model.AbstractJsonTests;
import com.casper.sdk.model.contract.EntryPointV1;
import com.casper.sdk.model.contract.EntryPointV2;
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

        StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);

        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));

        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(Account.class)));

    }

    @Test
    void validateGetStateEntitySmartContract() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-smartcontract-test.json"));

        StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);

        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));

        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(SmartContract.class)));

    }

    @Test
    void validateGetStateEntitySystemEntryPointV1() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-system-entry-point-v1-test.json"));

        StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);

        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));

        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(System.class)));

        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntryPoints().size(), is(greaterThan(0)));

        AddressableEntity entity = (AddressableEntity) addressableEntity.getEntity();
        assertThat(entity.getEntryPoints().get(0), is(instanceOf(EntryPointV1.class)));

    }

    @Test
    void validateGetStateEntitySystemEntryPointV2() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-system-entry-point-v2-test.json"));

        StateEntityResult addressableEntity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);

        assertInstanceOf(AddressableEntity.class, addressableEntity.getEntity());

        assertThat(((AddressableEntity) addressableEntity.getEntity()).getEntryPoints().size(), is(greaterThan(0)));

        AddressableEntity entity = (AddressableEntity) addressableEntity.getEntity();
        assertThat(entity.getEntryPoints().get(0), is(instanceOf(EntryPointV2.class)));

    }

    @Test
    void validateGetStateEntityLegacyAccount() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-legacy-account-test.json"));

        StateEntityResult entity = OBJECT_MAPPER.readValue(inputJson, StateEntityResult.class);

        assertInstanceOf(com.casper.sdk.model.account.Account.class, entity.getEntity());

    }
}
