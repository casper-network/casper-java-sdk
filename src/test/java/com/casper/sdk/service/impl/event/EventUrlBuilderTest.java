package com.casper.sdk.service.impl.event;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the EventUrlBuilder
 *
 * @author ian@meywood.com
 */
class EventUrlBuilderTest {

    private final EventUrlBuilder eventUrlBuilder = new EventUrlBuilder();

    @Test
    void buildMainEventUrl() throws URISyntaxException {

        URI uri = new URI("https://localhost:18101");
        assertThat(eventUrlBuilder.buildUrl(uri, null).toString(), is("https://localhost:18101/events"));
        assertThat(eventUrlBuilder.buildUrl(uri, 1L).toString(), is("https://localhost:18101/events?start_from=1"));
    }
}
