package com.casper.sdk.service.metrics;

import com.casper.sdk.Properties;
import com.casper.sdk.service.http.rpc.HttpMethods;
import com.casper.sdk.service.json.JsonConversionService;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MetricsServiceTest {

    private final static String url = "http://localhost";

    private static MockWebServer mockBackEnd;
    private final MetricsService metricsService = new MetricsService(new HttpMethods(new JsonConversionService()));

    @BeforeAll
    static void init() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @Test
    public void testStateRootOk() {

        Properties.properties.put("node-url", url);
        Properties.properties.put("node-port", String.valueOf(mockBackEnd.getPort()));

        mockBackEnd.setDispatcher(
                new MethodDispatcher());

        final String nodeMetrics = metricsService.getMetrics();

        assertNotNull(nodeMetrics);

    }

    private static final class MethodDispatcher extends Dispatcher {

        private final ClassLoader classLoader = getClass().getClassLoader();
        private String responseBodyFile;

        public MethodDispatcher() {}

        @NotNull
        @Override
        public MockResponse dispatch(RecordedRequest request) {

            if (request.getRequestUrl().toString().contains("metrics")) {
                responseBodyFile = "method-json/node-metrics.txt";
            }

            if ( responseBodyFile != null ) {
                return new MockResponse().setResponseCode(200)
                        .addHeader("Content-Type", "application/text")
                        .setBody(loadResponse(responseBodyFile));
            } else {
                return new MockResponse().setResponseCode(404);
            }
        }

        private String loadResponse(final String fileName) {
            try {
                return String.join("", Files.readAllLines(Paths.get(classLoader.getResource(fileName).toURI())));
            } catch (final IOException | NullPointerException | URISyntaxException e) {
                throw new IllegalStateException("Unable to load mock response from file", e);
            }
        }
    }
}