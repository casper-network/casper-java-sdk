package com.casper.sdk.service.http.rpc;

import com.casper.sdk.Properties;
import com.casper.sdk.domain.Deploy;
import com.casper.sdk.domain.DeployService;
import com.casper.sdk.json.JsonConversionService;
import com.casper.sdk.service.HashService;
import com.casper.sdk.service.SigningService;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.domain.ByteSerializerFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class NodeClientTest {

    public static final String DEPLOY_JSON_PATH = "/com/casper/sdk/domain/deploy-util-test.json";
    private final static String url = "http://localhost";
    private static MockWebServer mockBackEnd;

    private final HashService hashService = new HashService();
    private final DeployService deployService = new DeployService(
            new ByteSerializerFactory(),
            hashService,
            new JsonConversionService(),
            new SigningService(),
            new TypesFactory()
    );
    private final NodeClient query = new NodeClient(deployService, hashService, new JsonConversionService());

    @BeforeEach
    void setUp() throws IOException {

        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        mockBackEnd.setDispatcher(new DummyMethodDispatcher());

        Properties.properties.put("node-url", url);
        Properties.properties.put("node-port", String.valueOf(mockBackEnd.getPort()));
    }

    @AfterEach
    void tearDown() throws IOException {
        mockBackEnd.close();
    }

    @Test
    public void testStateRootOk() throws Throwable {

        final String stateRootHash = query.getStateRootHash();

        assertNotNull(stateRootHash);
        assertEquals("1be88786b127b212336d3c817816e5149334c86db156d34b59a78a0e0108b0db", stateRootHash);
    }

    @Test
    public void testGetAccountInfo() throws Throwable {

        final String info = query.getAccountInfo("01048c1858b7a6ff56a20d7574fd31025ead4af9cb8a854f919d24f886a4ebb741");
        assertNotNull(info);
        final JsonNode node = new ObjectMapper().readTree(info);
        assertEquals(node.get("result").get("stored_value").get("Account").get("account_hash").textValue(), "account-hash-ef5b5e0720614aeb59b0283513379b61af9e429b94e9904ea64a60ed599173ae");
    }

    @Test
    public void testGetPurse() throws Throwable {

        final String purse = query.getAccountMainPurseURef("01048c1858b7a6ff56a20d7574fd31025ead4af9cb8a854f919d24f886a4ebb741");
        assertNotNull(purse);
        assertEquals(purse, "uref-ebda3f171068107470bce0d74eb9a302fcb8914471fe8900c66fae258a0f46ef-007");
    }

    @Test
    public void testGetAccountBalance() throws Throwable {

        String balance = query.getAccountBalance("01048c1858b7a6ff56a20d7574fd31025ead4af9cb8a854f919d24f886a4ebb741");
        assertNotNull(balance);
        assertEquals(balance, "1000000000000000000000000000000000");
    }

    @Test
    public void testGetAuctionInfo() throws Throwable {

        String info = query.getAuctionInfo();
        assertNotNull(info);
    }

    @Test
    public void testGetNodePeers() throws Throwable {

        String peers = query.getNodePeers();
        assertNotNull(peers);
    }

    @Test
    public void testGetNodeStatus() throws Throwable {

        String status = query.getNodeStatus();
        assertNotNull(status);
    }

    @Test
    void testPutDeploy() throws Throwable {

        final InputStream in = getClass().getResource(DEPLOY_JSON_PATH).openStream();
        final Deploy deploy = deployService.fromJson(in);

        assertThat(query.putDeploy(deploy), is("01da3c604f71e0e7df83ff1ab4ef15bb04de64ca02e3d2b78de6950e8b5ee187"));
    }
}
