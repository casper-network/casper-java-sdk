package com.casper.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.casper.sdk.service.QueryService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;


public class TestQueryService {

    private static MockWebServer mockBackEnd;
    private final QueryService query = new QueryService();

    @BeforeAll
    static void init() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }

    @Test
    public void testStateRootOk() throws Throwable {

        Properties.properties.put("node-url", "http://localhost");
        Properties.properties.put("node-port", String.valueOf(mockBackEnd.getPort()));

        mockBackEnd.setDispatcher(
                new MethodDispatcher());

        String stateRootHash = query.getStateRootHash();

        assertNotNull(stateRootHash);
        assertEquals("1be88786b127b212336d3c817816e5149334c86db156d34b59a78a0e0108b0db", stateRootHash);

    }


    @Test
    public void testGetAccountInfo() throws Throwable {

        Properties.properties.put("node-url", "http://localhost");
        Properties.properties.put("node-port", String.valueOf(mockBackEnd.getPort()));

        mockBackEnd.setDispatcher(
                new MethodDispatcher());

        String info = query.getAccountInfo("01048c1858b7a6ff56a20d7574fd31025ead4af9cb8a854f919d24f886a4ebb741");
        assertNotNull(info);
        final JsonNode node = new ObjectMapper().readTree(info);
        assertEquals(node.get("result").get("stored_value").get("Account").get("account_hash").textValue(), "account-hash-ef5b5e0720614aeb59b0283513379b61af9e429b94e9904ea64a60ed599173ae");
    }

    @Test
    public void testGetPurse() throws Throwable {

        Properties.properties.put("node-url", "http://localhost");
        Properties.properties.put("node-port", String.valueOf(mockBackEnd.getPort()));

        mockBackEnd.setDispatcher(
                new MethodDispatcher());

        String purse = query.getAccountMainPurseURef("01048c1858b7a6ff56a20d7574fd31025ead4af9cb8a854f919d24f886a4ebb741");
        assertNotNull(purse);
        assertEquals(purse, "uref-ebda3f171068107470bce0d74eb9a302fcb8914471fe8900c66fae258a0f46ef-007");
    }

    @Test
    public void testGetAccountBalance() throws Throwable {

        Properties.properties.put("node-url", "http://localhost");
        Properties.properties.put("node-port", String.valueOf(mockBackEnd.getPort()));

        mockBackEnd.setDispatcher(
                new MethodDispatcher());

        String balance = query.getAccountBalance("01048c1858b7a6ff56a20d7574fd31025ead4af9cb8a854f919d24f886a4ebb741");
        assertNotNull(balance);
        assertEquals(balance, "1000000000000000000000000000000000");

    }

    @Test
    public void testGetAuctionInfo() throws Throwable {

        Properties.properties.put("node-url", "http://localhost");
        Properties.properties.put("node-port", String.valueOf(mockBackEnd.getPort()));

        mockBackEnd.setDispatcher(
                new MethodDispatcher());

        String info = query.getAuctionInfo();
        assertNotNull(info);


    }

    @Test
    public void testGetNodePeers() throws Throwable {

        Properties.properties.put("node-url", "http://localhost");
        Properties.properties.put("node-port", String.valueOf(mockBackEnd.getPort()));

        mockBackEnd.setDispatcher(
                new MethodDispatcher());

        String peers = query.getNodePeers();
        assertNotNull(peers);

    }

    @Test
    public void testGetNodeStatus() throws Throwable {

        Properties.properties.put("node-url", "http://localhost");
        Properties.properties.put("node-port", String.valueOf(mockBackEnd.getPort()));

        mockBackEnd.setDispatcher(
                new MethodDispatcher());

        String status = query.getNodeStatus();
        assertNotNull(status);

    }


    private static final class MethodDispatcher extends Dispatcher{

        private final ClassLoader classLoader = getClass().getClassLoader();
        private String responseBodyFile;
        public MethodDispatcher() {}

        @Override
        public MockResponse dispatch(RecordedRequest request) {

            if (request.getBody().toString().contains("chain_get_state_root")){
                responseBodyFile = "method-json/chain_get_state_root_hash-ok.json";
            } else if  (request.getBody().toString().contains("state_get_item")){
                responseBodyFile = "method-json/state_get_item.json";
            } else if  (request.getBody().toString().contains("state_get_balance")){
                responseBodyFile = "method-json/state_get_balance.json";
            } else if  (request.getBody().toString().contains("state_get_auction_info")){
                responseBodyFile = "method-json/state_get_auction_info.json";
            } else if  (request.getBody().toString().contains("info_get_peers")){
                responseBodyFile = "method-json/info_get_peers.json";
            } else if  (request.getBody().toString().contains("info_get_status")){
                responseBodyFile = "method-json/info_get_status.json";
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


}
