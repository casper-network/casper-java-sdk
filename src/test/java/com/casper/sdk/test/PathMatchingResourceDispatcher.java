package com.casper.sdk.test;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import org.hamcrest.Matcher;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

public class PathMatchingResourceDispatcher extends Dispatcher {

    /** The path to the resource to serve for the path that matches the pathMatcher */
    private final String resourcePath;
    /** The matcher for the URL request path */
    private final Matcher<String> pathMatcher;
    private String contentType =  "application/json";

    public PathMatchingResourceDispatcher(final String resourcePath, final Matcher<String> pathMatcher) {
        this.resourcePath = resourcePath;
        this.pathMatcher = pathMatcher;
    }

    @NotNull
    @Override
    public MockResponse dispatch(@NotNull final RecordedRequest recordedRequest) {

        if (pathMatcher.matches(recordedRequest.getPath())) {

            //noinspection ConstantConditions
            try (final InputStream in = PathMatchingResourceDispatcher.class.getResourceAsStream(resourcePath);
                 final Buffer buffer = new Buffer().readFrom(in)) {

                return new MockResponse().setResponseCode(200)
                        .addHeader("Content-Type", contentType)
                        .setBody(buffer);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // return not found if no match
            return new MockResponse().setResponseCode(404);
        }
    }

    public Dispatcher setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }
}
