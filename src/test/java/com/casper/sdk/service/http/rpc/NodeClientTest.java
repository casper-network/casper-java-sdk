package com.casper.sdk.service.http.rpc;

import com.casper.sdk.Properties;
import com.casper.sdk.service.hash.HashService;
import com.casper.sdk.service.json.JsonConversionService;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.types.ByteSerializerFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.service.signing.SigningService;
import com.casper.sdk.types.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class NodeClientTest {

    public static final String DEPLOY_JSON_PATH = "/com/casper/sdk/types/deploy-util-test.json";
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
    private final NodeClient nodeClient = new NodeClient(deployService, hashService, new JsonConversionService());

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
    public void testStateRootOk() {

        final String stateRootHash = nodeClient.getStateRootHash();

        assertNotNull(stateRootHash);
        assertEquals("1be88786b127b212336d3c817816e5149334c86db156d34b59a78a0e0108b0db", stateRootHash);
    }

    @Test
    public void testGetAccountInfo() throws Throwable {

        final String info = nodeClient.getAccountInfo("01048c1858b7a6ff56a20d7574fd31025ead4af9cb8a854f919d24f886a4ebb741");
        assertNotNull(info);
        final JsonNode node = new ObjectMapper().readTree(info);
        assertEquals(node.get("stored_value").get("Account").get("account_hash").textValue(), "account-hash-ef5b5e0720614aeb59b0283513379b61af9e429b94e9904ea64a60ed599173ae");
    }

    @Test
    public void testGetPurse() {

        final URef purse = nodeClient.getAccountMainPurseURef("01048c1858b7a6ff56a20d7574fd31025ead4af9cb8a854f919d24f886a4ebb741");
        assertNotNull(purse);
        assertThat(purse.getAccessRights(), is(AccessRights.READ_ADD_WRITE));
        assertThat(purse.getBytes(), is(ByteUtils.decodeHex("ebda3f171068107470bce0d74eb9a302fcb8914471fe8900c66fae258a0f46ef")));
        //  assertEquals(purse., "uref-ebda3f171068107470bce0d74eb9a302fcb8914471fe8900c66fae258a0f46ef-007");
    }

    @Test
    public void testGetAccountBalance() {

        final BigInteger balance = nodeClient.getAccountBalance("01048c1858b7a6ff56a20d7574fd31025ead4af9cb8a854f919d24f886a4ebb741");
        assertNotNull(balance);
        assertEquals(balance, new BigInteger("1000000000000000000000000000000000"));
    }

    @Test
    public void testGetAuctionInfo() {

        String info = nodeClient.getAuctionInfo();
        assertNotNull(info);
    }

    @Test
    public void testGetNodePeers() {

        String peers = nodeClient.getNodePeers();
        assertNotNull(peers);
    }

    @Test
    public void testGetNodeStatus() {

        String status = nodeClient.getNodeStatus();
        assertNotNull(status);
    }

    @Test
    void testPutDeploy() throws Throwable {

        //noinspection ConstantConditions
        final InputStream in = getClass().getResource(DEPLOY_JSON_PATH).openStream();
        final Deploy deploy = deployService.fromJson(in);
        assertThat(nodeClient.putDeploy(deploy), is("01da3c604f71e0e7df83ff1ab4ef15bb04de64ca02e3d2b78de6950e8b5ee187"));

        //noinspection ConstantConditions
        String json = IOUtils.toString(getClass().getResource(DEPLOY_JSON_PATH).openStream(), StandardCharsets.UTF_8);
        final Deploy deploy2 = deployService.fromJson(json);
        assertThat(nodeClient.putDeploy(deploy2), is("01da3c604f71e0e7df83ff1ab4ef15bb04de64ca02e3d2b78de6950e8b5ee187"));
    }


    @Test
    void testGetDeploy() {

        Digest deployHash = new Digest("3752362444d2b75de05f0a849157d94acaeebdcce155716cd2848773c2a159b0");
        Deploy deploy = nodeClient.getDeploy(deployHash);
        assertThat(deploy, is(notNullValue()));
        assertThat(deploy.getHash(), is(deployHash));

        assertThat(deploy.getHeader().getChainName(), is("casper-net-1"));
        assertThat(deploy.getHeader().getAccount(), is(new CLPublicKey("0168f061b4ec8b728c83815f1120c36d444de28ea4c6d696e3f098319ff4908b26")));
        assertThat(deploy.getHeader().getTimestamp(), is(notNullValue()));
        assertThat(deploy.getHeader().getGasPrice(), is(10));
        assertThat(deploy.getHeader().getTimeStampIso(), is("2021-10-26T19:11:45.203Z"));
        assertThat(deploy.getSession(), is(instanceOf(Transfer.class)));
        assertThat(deploy.getPayment(), is(instanceOf(DeployExecutable.class)));
        assertThat(deploy.getApprovals(), hasSize(2));
    }

    @Test
    void getLatestBlockInfo() {

        String latestBlockInfo = nodeClient.getLatestBlockInfo();
        assertThat(latestBlockInfo, hasJsonPath("$.hash", is("ce4e6b534c69b2b29f834c6ce73a4b119090de84485149cfc8f2b10b6737166e")));
        assertThat(latestBlockInfo, hasJsonPath("$.header.height", is(314)));
        assertThat(latestBlockInfo, hasJsonPath("$.header.era_id", is(28)));
    }
}
