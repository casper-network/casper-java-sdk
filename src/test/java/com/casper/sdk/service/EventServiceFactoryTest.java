package com.casper.sdk.service;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests for the EventServiceFactory
 * 
 * @author ian@meywood.com 
 */
class EventServiceFactoryTest {

    @Test
    void createUsingHostString() {
        final EventService eventService = EventServiceFactory.create("http://localhost:18101");
        assertEventServiceProxied(eventService);

    }

    @Test
    void createUsingURI() throws URISyntaxException {
        final EventService eventService = EventServiceFactory.create(new URI("http://localhost:18101"));
        assertEventServiceProxied(eventService);
    }

    private void assertEventServiceProxied(final EventService eventService) {
        assertThat(eventService, is(notNullValue()));
        assertThat(Proxy.isProxyClass(eventService.getClass()), is(true));
    }
}