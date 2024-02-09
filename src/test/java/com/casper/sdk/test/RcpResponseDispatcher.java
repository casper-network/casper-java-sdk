package com.casper.sdk.test;


import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import org.hamcrest.CustomMatcher;
import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ian@meywood.com
 */
public class RcpResponseDispatcher extends Dispatcher {

    private static class MethodMatcher extends CustomMatcher<String> {
        private final String method;

        MethodMatcher(final String method) {
            super("MethodMatcher");
            this.method = method;
        }

        @Override
        public boolean matches(final Object item) {
            return this.method.equals(((DocumentContext) item).read("$.method", String.class));
        }
    }

    private static class JsonBodyMatcher extends CustomMatcher<String> {

        private final String jsonPath;
        private final String expeted;

        JsonBodyMatcher(final String jsonPath, final String expected) {
            super("JsonBodyMatcher");
            this.jsonPath = jsonPath;
            this.expeted = expected;
        }

        @Override
        public boolean matches(final Object item) {
            final String actual = ((DocumentContext) item).read(this.jsonPath, String.class);
            return this.expeted.equals(actual);
        }
    }

    private final List<Matcher<?>> matchers = new ArrayList<>();
    private URL resource;

    @NotNull
    @Override
    public MockResponse dispatch(final RecordedRequest recordedRequest) {

        final String body = recordedRequest.getBody().readString(StandardCharsets.UTF_8);
        final DocumentContext ctx = JsonPath.parse(body);

        for (final Matcher<?> matcher : matchers) {
            if (!matcher.matches(ctx)) {
                return new MockResponse().setResponseCode(404).setBody(matcher.toString());
            }
        }

        //noinspection ConstantConditions
        try (final InputStream in = resource.openStream();
             final Buffer buffer = new Buffer().readFrom(in)) {

            return new MockResponse().setResponseCode(200)
                    .addHeader("Content-Type", "application/json")
                    .setBody(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public RcpResponseDispatcher withMethod(final String method) {
        return addMatcher(new MethodMatcher(method));
    }

    public RcpResponseDispatcher withBody(final String jsonPath, final String expected) {
        return addMatcher(new JsonBodyMatcher(jsonPath, expected));
    }

    public RcpResponseDispatcher addMatcher(final Matcher<?> matcher) {
        this.matchers.add(matcher);
        return this;
    }

    public RcpResponseDispatcher thenDispatch(URL resource) {
        this.resource = resource;
        return this;
    }

    public RcpResponseDispatcher clear() {
        this.matchers.clear();
        this.resource = null;
        return this;
    }
}
