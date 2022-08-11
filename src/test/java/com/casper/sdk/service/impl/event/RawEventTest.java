package com.casper.sdk.service.impl.event;

import com.casper.sdk.model.event.DataType;
import com.casper.sdk.model.event.EventType;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author ian@meywood.com
 */
class RawEventTest {

    @Test
    void rawEvent() {

        final String source = "http://localhost:9999";
        final String data = "data:{\"ApiVersion\":\"1.0.0\"}";

        final RawEvent rawEvent = new RawEvent(EventType.MAIN, source, 1L, data);

        assertThat(rawEvent.getEventType(), is(EventType.MAIN));
        assertThat(rawEvent.getSource(), is(source));
        assertThat(rawEvent.getId().isPresent(), is(true));
        assertThat(rawEvent.getId().get(), is(1L));
        assertThat(rawEvent.getData(), is(data));
        assertThat(rawEvent.getDataType(), is(DataType.API_VERSION));
    }
}