package com.casper.sdk.service.http.rpc;

import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.RecordedRequest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class DummyMethodDispatcher extends Dispatcher {

    private final ClassLoader classLoader = getClass().getClassLoader();
    private String responseBodyFile;

    public DummyMethodDispatcher() {
    }

    @Override
    public MockResponse dispatch(final RecordedRequest request) {

        final String body = request.getBody().toString();

        if (body.contains("chain_get_state_root")) {
            responseBodyFile = "method-json/chain_get_state_root_hash-ok.json";
        } else if (body.contains("state_get_item")) {
            responseBodyFile = "method-json/state_get_item.json";
        } else if (body.contains("state_get_balance")) {
            responseBodyFile = "method-json/state_get_balance.json";
        } else if (body.contains("state_get_auction_info")) {
            responseBodyFile = "method-json/state_get_auction_info.json";
        } else if (body.contains("info_get_peers")) {
            responseBodyFile = "method-json/info_get_peers.json";
        } else if (body.contains("info_get_status")) {
            responseBodyFile = "method-json/info_get_status.json";
        } else if (body.contains("account_put_deploy")) {
            responseBodyFile = "method-json/account_put_deploy.json";
        } else if (body.contains("info_get_deploy")) {
            responseBodyFile = "method-json/info_get_deploy.json";
        } else if (body.contains("chain_get_block_transfers")) {
            responseBodyFile = "method-json/chain_get_block_transfers.json";
        } else if (body.contains("chain_get_block")) {
            responseBodyFile = "method-json/chain_get_block.json";
        } else if (body.contains("rpc.discover")) {
            responseBodyFile = "method-json/rpc_discover.json";
        } else if (body.contains("chain_get_era_info_by_switch_b")) {
            responseBodyFile = "method-json/chain_get_era_info_by_switch_block.json";
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
