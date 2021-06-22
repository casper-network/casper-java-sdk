package com.casper.sdk.service;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

final class DummyMethodDispatcher extends Dispatcher {

    private final ClassLoader classLoader = getClass().getClassLoader();
    private String responseBodyFile;

    DummyMethodDispatcher() {
    }

    @Override
    public MockResponse dispatch(final RecordedRequest request) {

        if (request.getBody().toString().contains("chain_get_state_root")) {
            responseBodyFile = "method-json/chain_get_state_root_hash-ok.json";
        } else if (request.getBody().toString().contains("state_get_item")) {
            responseBodyFile = "method-json/state_get_item.json";
        } else if (request.getBody().toString().contains("state_get_balance")) {
            responseBodyFile = "method-json/state_get_balance.json";
        } else if (request.getBody().toString().contains("state_get_auction_info")) {
            responseBodyFile = "method-json/state_get_auction_info.json";
        } else if (request.getBody().toString().contains("info_get_peers")) {
            responseBodyFile = "method-json/info_get_peers.json";
        } else if (request.getBody().toString().contains("info_get_status")) {
            responseBodyFile = "method-json/info_get_status.json";
        } else if (request.getBody().toString().contains("account_put_deploy")) {
            responseBodyFile = "method-json/account_put_deploy.json";
        }

        return new MockResponse().setResponseCode(200)
                .addHeader("Content-Type", "application/json")
                .setBody(loadJson(responseBodyFile));
    }

    private String loadJson(final String fileName) {
        try {
            return String.join("", Files.readAllLines(Paths.get(classLoader.getResource(fileName).toURI())));
        } catch (final IOException | NullPointerException | URISyntaxException e) {
            throw new IllegalStateException("Unable to load mock response from JSON", e);
        }
    }

}
