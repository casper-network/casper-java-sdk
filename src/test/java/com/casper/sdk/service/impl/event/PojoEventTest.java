package com.casper.sdk.service.impl.event;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.event.DataType;
import com.casper.sdk.model.event.deployexpired.DeployExpired;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author ian@meywood.com
 */
class PojoEventTest {

    @Test
    void pojoEvent() {

        final String source = "http://localhost:9999";
        final String version = "1.0.0";
        final DeployExpired data = new DeployExpired(new Digest("bb878bcf8827649f070c487800a95c35be3eb2e83b5447921675040cea38af1c"));

        final PojoEvent<DeployExpired> rawEvent = new PojoEvent<>(source, 2L, data, version);

        assertThat(rawEvent.getSource(), is(source));
        assertThat(rawEvent.getId().isPresent(), is(true));
        assertThat(rawEvent.getId().get(), is(2L));
        assertThat(rawEvent.getData(), is(data));
        assertThat(rawEvent.getDataType(), is(DataType.DEPLOY_EXPIRED));
        assertThat(rawEvent.getVersion(), is(version));

    }
}
