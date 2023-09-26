package com.casper.sdk.e2e.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Provides like commands to a node to obtain raw JSON.
 *
 * @author ian@meywood.com
 */
public class SimpleRcpClient {

    private final String hostname;
    private final int port;

    public SimpleRcpClient(final String hostname, final int port) {
        this.hostname = hostname;
        this.port = port;
    }

    /**
     * Obtains auction info as a JsonNode by block hash
     *
     * @param hash the block to obtain auction info for
     * @return the Json result
     * @throws Exception if an IO error occurs
     */
    public JsonNode getAuctionInfoByHash(final String hash) throws Exception {
        return rcp("state_get_auction_info", "[{\"Hash\":  \"" + hash + "\"}]");
    }

    /**
     * Obtains the era summary
     * Since 1.5
     * This replaces chain_get_era_info_by_switch_block
     * No need now to wait for era end to query the era info
     *
     * @param hash the block to obtain the era summary from
     * @return the Json result
     * @throws Exception if an IO error occurs
     */
    public JsonNode getEraSummary(final String hash) throws Exception {
        return rcp("chain_get_era_summary", "[{\"Hash\":  \"" + hash + "\"}]");
    }

    /**
     * Obtains the validator changes using the info_get_validator_changes RCP method.
     *
     * @return the info_get_validator_changes_result node
     * @throws Exception on an IO error
     */
    public JsonNode getValidatorChanges() throws Exception {
        return rcp("info_get_validator_changes", "[]");
    }


    /**
     * Obtains the query balance using the query_balance RCP method.
     *
     * @return the info_get_validator_changes_result node
     * @throws Exception on an IO error
     */
    public JsonNode queryBalance(final String purseIdentifierName, final String identifier) throws Exception {
        return rcp("query_balance",  String.format("{\"purse_identifier\":{\"%s\":\"%s\"}}",purseIdentifierName, identifier));
    }

    /**
     * Obtains auction info as a JsonNode by block hash
     *
     * @param stateRootHash the state root hash
     * @param purseUref     the ref to the purse whose balance is to be obtained
     * @return the Json result
     * @throws Exception if an IO error occurs
     */
    public JsonNode getBalance(final String stateRootHash, final String purseUref) throws Exception {
        return rcp("state_get_balance",
                String.format("{\"state_root_hash\":\"%s\",\"purse_uref\":\"%s\"}", stateRootHash, purseUref)
        );
    }

    /**
     * Performs an old school java http rcp request to a node.
     *
     * @param method the rcp method to invoke
     * @param params the params to pass into the method
     * @return the JsonNode of the executed RCP request
     * @throws Exception on an invalid request
     */
    private JsonNode rcp(final String method, final String params) throws Exception {

        final URL url = new URL("http", hostname, port, "/rpc");
        final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        final String payload = "{\"id\":\"" + System.currentTimeMillis() + "\",\"jsonrpc\":\"2.0\",\"method\":\"" + method + "\",\"params\":" + params + "}";

        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Content-Length", Integer.toString(payload.length()));
        connection.setDoOutput(true);

        final OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
        out.write(payload);
        out.flush();
        out.close();

        return new ObjectMapper().readTree(connection.getInputStream());
    }
}
