package com.casper.sdk.service.json.serialize;

import com.casper.sdk.types.Deploy;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the a {@link Deploy} can be serialized to JSON
 */
public class DeployJsonSerializerTest {

    /** The path to the original JSON the model is loaded from */
    private static final String DEPLOY_TRANSFER_JSON = "/com/casper/sdk/service/json/deploy-transfer.json";

    /** The file that is written to for testing */
    private File jsonFile;

    @BeforeEach
    void setUp() throws IOException {

        final ObjectMapper mapper = new ObjectMapper();
        // Load the model from JSON
        final InputStream in = getClass().getResourceAsStream(DEPLOY_TRANSFER_JSON);
        /* The deploy to serialize back to JSON */
        final Deploy deploy = mapper.reader().readValue(in, Deploy.class);

        // Write it back to JSON
        final File deployFile = File.createTempFile("deploy", ".json");
        final FileOutputStream out = new FileOutputStream(deployFile);
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE);
        mapper.writer(prettyPrinter).writeValue(out, deploy);
        out.close();

        // Load the file we just created and test is valid
        jsonFile = new File(deployFile.getPath());
    }

    @AfterEach
    void tearDown() {
        // Clean up the JSON test file
        if (jsonFile != null && jsonFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            jsonFile.delete();
        }
    }

    @Test
    void serializeDeployHash() {
        assertThat(jsonFile, hasJsonPath("$.hash", is("d7a68bbe656a883d04bba9f26aa340dbe3f8ec99b2adb63b628f2bc920431998")));
    }

    @Test
    void serializeDeployHeader() {
        assertThat(jsonFile, hasJsonPath("$.header.account", is("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537")));
        assertThat(jsonFile, hasJsonPath("$.header.timestamp", is("2021-05-04T14:20:35.104Z")));
        assertThat(jsonFile, hasJsonPath("$.header.ttl", is("30m")));
        assertThat(jsonFile, hasJsonPath("$.header.gas_price", is(2)));
        assertThat(jsonFile, hasJsonPath("$.header.body_hash", is("f2e0782bba4a0a9663cafc7d707fd4a74421bc5bfef4e368b7e8f38dfab87db8")));
        assertThat(jsonFile, hasJsonPath("$.header.chain_name", is("mainnet")));
    }

    @Test
    void serializeDeployPayment() {
        assertThat(jsonFile, hasJsonPath("$.payment.ModuleBytes"));
        assertThat(jsonFile, hasJsonPath("$.payment.ModuleBytes.module_bytes", is("")));
        assertThat(jsonFile, hasJsonPath("$.payment.ModuleBytes.args[*]", hasSize(1)));
        assertThat(jsonFile, hasJsonPath("$.payment.ModuleBytes.args[0][0]", is("amount")));
        assertThat(jsonFile, hasJsonPath("$.payment.ModuleBytes.args[0][1].cl_type", is("U512")));
        assertThat(jsonFile, hasJsonPath("$.payment.ModuleBytes.args[0][1].bytes", is("0400ca9a3b")));
        assertThat(jsonFile, hasJsonPath("$.payment.ModuleBytes.args[0][1].parsed", is("1000000000")));
    }

    @Test
    void serializeDeploySession() {

        assertThat(jsonFile, hasJsonPath("$.session.Transfer"));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[*]", hasSize(4)));

        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[0][0]", is("amount")));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[0][1].cl_type", is("U512")));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[0][1].bytes", is("05005550b405")));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[0][1].parsed", is("24500000000")));

        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[1][0]", is("target")));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[1][1].cl_type"));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[1][1].cl_type.ByteArray", is(32)));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[1][1].bytes", is("0101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[1][1].parsed", is("0101010101010101010101010101010101010101010101010101010101010101")));

        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[2][0]", is("id")));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[2][1].cl_type", is("U64")));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[2][1].bytes", is("01e703000000000000")));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[2][1].parsed", is(999)));

        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[3][0]", is("additional_info")));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[3][1].cl_type", is("String")));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[3][1].bytes", is("1000000074686973206973207472616e73666572")));
        assertThat(jsonFile, hasJsonPath("$.session.Transfer.args[3][1].parsed", is("this is transfer")));
    }

    @Test
    void serializeDeployApprovals() {
        assertThat(jsonFile, hasJsonPath("$.approvals[*]", hasSize(1)));
        assertThat(jsonFile, hasJsonPath("$.approvals[0].signer", is("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537")));
        assertThat(jsonFile, hasJsonPath("$.approvals[0].signature", is("0195a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06")));
    }
}