package com.casper.sdk.test;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockWebServer;

import java.io.IOException;
import java.net.URI;

/**
 * Provides a mock node that can be used for integration testing with having to spin up a real CSPR Node.
 *
 * @author ian@meywood.com
 */
public class MockNode {

    private final MockWebServer mockWebServer = new MockWebServer();

    public void start(final URI url) throws IOException {
        mockWebServer.start(url.getPort());
    }

    public void shutdown() throws IOException {
        mockWebServer.shutdown();
    }

    public void setDispatcher(final Dispatcher dispatcher) {
        mockWebServer.setDispatcher(dispatcher);
    }

    public RcpResponseDispatcher withRcpResponseDispatcher() {
        final RcpResponseDispatcher whenDispatcher = new RcpResponseDispatcher();
        setDispatcher(whenDispatcher);
        return whenDispatcher;
    }
}
