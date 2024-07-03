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
        assertThat(eventUrlBuilder.buildUrl(uri, null).toString(), is("https://localhost:18101/events/main"));
        assertThat(eventUrlBuilder.buildUrl(uri, 1L).toString(), is("https://localhost:18101/events/main?start_from=1"));
    }

    @Test
    void buildDeploysEventUrl() throws URISyntaxException {

        URI uri = new URI("https://localhost:18101");
        assertThat(eventUrlBuilder.buildUrl(uri, null).toString(), is("https://localhost:18101/events/deploys"));
        assertThat(eventUrlBuilder.buildUrl(uri, 99L).toString(), is("https://localhost:18101/events/deploys?start_from=99"));
    }

    @Test
    void buildSigsEventUrl() throws URISyntaxException {

        URI uri = new URI("https://localhost:18101");
        assertThat(eventUrlBuilder.buildUrl(uri, null).toString(), is("https://localhost:18101/events/sigs"));
        assertThat(eventUrlBuilder.buildUrl(uri, 3L).toString(), is("https://localhost:18101/events/sigs?start_from=3"));
    }
}
