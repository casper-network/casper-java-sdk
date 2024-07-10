package com.casper.sdk.model.entity;

import com.casper.sdk.model.AbstractJsonTests;
import com.casper.sdk.model.contract.EntryPointV1;
import com.casper.sdk.model.contract.EntryPointV2;
import com.casper.sdk.model.transaction.execution.Effect;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class StateGetEntityTest extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(StateGetEntityTest.class);

    @Test
    void validateGetStateEntityAccount() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-account-result.json"));

        JsonEntity addressableEntity = OBJECT_MAPPER.readValue(inputJson, JsonEntity.class);

        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));

        assertThat(((AddressableEntity)addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(Account.class)));

    }

    @Test
    void validateGetStateEntitySmartContract() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-smartcontract-result.json"));

        JsonEntity addressableEntity = OBJECT_MAPPER.readValue(inputJson, JsonEntity.class);

        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));

        assertThat(((AddressableEntity)addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(SmartContract.class)));

    }
    @Test
    void validateGetStateEntitySystemEntryPointV1() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-system-entry-point-v1-result.json"));

        JsonEntity addressableEntity = OBJECT_MAPPER.readValue(inputJson, JsonEntity.class);

        assertThat(addressableEntity.getEntity(), is(instanceOf(AddressableEntity.class)));

        assertThat(((AddressableEntity)addressableEntity.getEntity()).getEntity().getEntityAddressKind(), is(instanceOf(System.class)));

        assertThat(((AddressableEntity)addressableEntity.getEntity()).getEntryPoints().size(), is(greaterThan(0)));

        AddressableEntity entity = (AddressableEntity)addressableEntity.getEntity();
        assertThat(entity.getEntryPoints().get(0), is(instanceOf(EntryPointV1.class)));

    }
    @Test
    void validateGetStateEntitySystemEntryPointV2() throws IOException {

        final String inputJson = getPrettyJson(loadJsonFromFile("entity/getstateentity-system-entry-point-v2-result.json"));

        JsonEntity addressableEntity = OBJECT_MAPPER.readValue(inputJson, JsonEntity.class);

        assertInstanceOf(AddressableEntity.class, addressableEntity.getEntity());

        assertThat(((AddressableEntity)addressableEntity.getEntity()).getEntryPoints().size(), is(greaterThan(0)));

        AddressableEntity entity = (AddressableEntity)addressableEntity.getEntity();
        assertThat(entity.getEntryPoints().get(0), is(instanceOf(EntryPointV2.class)));

    }

}
