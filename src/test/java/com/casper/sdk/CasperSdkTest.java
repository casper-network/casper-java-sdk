package com.casper.sdk;

import com.casper.sdk.service.http.rpc.DummyMethodDispatcher;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.*;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Objects;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CasperSdkTest {

    private static final String DEPLOY_TRANSFER_JSON = "/com/casper/sdk/service/json/deploy-transfer.json";
    private final static String url = "http://localhost";
    private static MockWebServer mockBackEnd;
    private CasperSdk casperSdk;

    @BeforeEach
    void setUp() throws IOException {

        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        mockBackEnd.setDispatcher(new DummyMethodDispatcher());

        casperSdk = new CasperSdk(url, mockBackEnd.getPort());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockBackEnd.close();
    }

    /**
     * Tests the SDK can create a transfer object
     */
    @Test
    void newTransfer() {

        final byte[] recipientPublicKey = {1, (byte) 108, (byte) 114, (byte) 213, (byte) 223, (byte) 102, (byte) 130,
                (byte) 189, (byte) 133, (byte) 175, (byte) 102, (byte) 195, (byte) 84, (byte) 171, (byte) 94, (byte) 88,
                (byte) 117, (byte) 242, (byte) 142, (byte) 146, (byte) 54, (byte) 0, (byte) 20, (byte) 129, (byte) 210,
                (byte) 128, (byte) 211, (byte) 127, (byte) 27, (byte) 63, (byte) 229, (byte) 50, (byte) 192};

        // Convert the bytes to a java.security.PublicKey
        final PublicKey publicKey = casperSdk.createPublicKey(ByteUtils.encodeHexString(recipientPublicKey));

        // The recipientPublicKey hashed to 32 bytes using ED25519
        final String targetBytes = "473535565ff7b4fdd7aa3f7a083c7f9d05b82fcba57c22339fbf50e3c5474dcb";

        final Transfer transfer = casperSdk.newTransfer(
                10,
                publicKey,
                34
        );

        assertThat(transfer.getNamedArg("amount").getValue().getParsed(), is("10"));
        assertThat(transfer.getNamedArg("amount").getValue().getCLType(), is(CLType.U512));
        assertThat(transfer.getNamedArg("id").getValue().getCLType(), is(CLType.OPTION));
        assertThat(transfer.getNamedArg("id").getValue(), is(instanceOf(CLOptionValue.class)));
        assertThat(((CLOptionTypeInfo) transfer.getNamedArg("id").getValue().getCLTypeInfo()).getInnerType().getType(), is(CLType.U64));
        assertThat(transfer.getNamedArg("id").getValue().getBytes(), is(new byte[]{1, 34, 0, 0, 0, 0, 0, 0, 0}));
        assertThat(transfer.getNamedArg("target").getValue().getBytes(), is(ByteUtils.decodeHex(targetBytes)));
        assertThat(transfer.getNamedArg("target").getValue().getCLType(), is(CLType.BYTE_ARRAY));
        assertThat(((CLByteArrayInfo) transfer.getNamedArg("target").getValue().getCLTypeInfo()).getSize(), is(32));
    }

    @Test
    void getRpcSchema() {
        final String rpcSchema = casperSdk.getRpcSchema();
        assertThat(rpcSchema, is(notNullValue()));
        assertThat(rpcSchema, hasJsonPath("$.api_version", is("1.1.0")));
        assertThat(rpcSchema, hasJsonPath("$.name", is("OpenRPC Schema")));
    }

    @Test
    void getBlockTransfers() {
        final String blockTransfers = casperSdk.getBlockTransfers();
        assertThat(blockTransfers, is(notNullValue()));
        assertThat(blockTransfers, hasJsonPath("$.transfers"));
        assertThat(blockTransfers, hasJsonPath("$.transfers[0].deploy_hash", is("5db09c0275c4c1ba54ebcc69784ed767350bf9c2b0be7ab4fec1ca84acd1c47d")));
        assertThat(blockTransfers, hasJsonPath("$.transfers[0].amount", is("7000000000")));
    }

    @Test
    void getEraInfoBySwitchBlock() {
        final String eraInfoBySwitchBlock = casperSdk.getEraInfoBySwitchBlock();
        assertThat(eraInfoBySwitchBlock, is(notNullValue()));
        assertThat(eraInfoBySwitchBlock, hasJsonPath("$.era_summary"));
    }

    @Test
    public void getNodeMetrics() {

        final String nodeMetrics = casperSdk.getNodeMetrics();
        assertNotNull(nodeMetrics);
    }


    @Test
    void deployFromJsonStream() throws IOException {

        // Load the deploy from JSON
        final String originalJson = IOUtils.toString(Objects.requireNonNull(getClass()
                .getResourceAsStream(DEPLOY_TRANSFER_JSON)), StandardCharsets.UTF_8);

        final Deploy deploy = casperSdk.deployFromJson(getClass().getResourceAsStream(DEPLOY_TRANSFER_JSON));

        assertThat(deploy, is(notNullValue()));

        // Convert it back to JSON
        final String json = deleteWhitespace(casperSdk.deployToJson(deploy));

        // Assert JSON written from CL Object matches original JSON ignoring whitespace
        assertThat(json, is(deleteWhitespace(originalJson)));
    }

    @Test
    void deployFromJsonString() throws IOException {

        // Load the deploy from JSON
        final String originalJson = IOUtils.toString(Objects.requireNonNull(getClass()
                .getResourceAsStream(DEPLOY_TRANSFER_JSON)), StandardCharsets.UTF_8);

        final Deploy deploy = casperSdk.deployFromJson(originalJson);

        assertThat(deploy, is(notNullValue()));

        // Convert it back to JSON
        final String json = deleteWhitespace(casperSdk.deployToJson(deploy));

        // Assert JSON written from CL Object matches original JSON ignoring whitespace
        assertThat(json, is(deleteWhitespace(originalJson)));
    }
}
