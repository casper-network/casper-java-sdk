package com.casper.sdk;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.casper.sdk.controller.CasperSdk;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TestChain {

    private final static String url = "http://3.140.179.157";
    private final static String port = "7777";

    @Test
    @Disabled
    public void testAgainstChain() throws Throwable {
        CasperSdk casperSdk = new CasperSdk(url, port);

        String accountBalance = casperSdk.getAccountBalance("019d9bc24944a08204c09d222405127c8ca7dc7dcc267ccc6b392deab8148d111d");
        assertEquals("1000000000000000000000000000000000", accountBalance);
    }


    @Test
    public void testChainSimpleQueries_v_0_2_0() throws Throwable {

        CasperSdk casperSdk = new CasperSdk(url, port);

        JsonNode node = new ObjectMapper().readTree(casperSdk.getAuctionInfo());
        assertNotNull(node.get("api_version").textValue());
        assertNotNull(node.get("auction_state").get("state_root_hash").textValue());

        node = new ObjectMapper().readTree(casperSdk.getNodeStatus());
        assertNotNull(node.get("api_version").textValue());
        assertNotNull(node.get("chainspec_name").textValue());
        assertNotNull(node.get("last_added_block_info").asText());
        assertNotNull(node.get("last_added_block_info").get("era_id").asText());
        assertNotNull(node.get("peers").get(0).get("node_id").textValue());

        node = new ObjectMapper().readTree(casperSdk.getNodePeers());
        assertNotNull(node.get("api_version").textValue());
        assertNotNull(node.get("peers").get(0).get("node_id").textValue());

    }


}
