package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.Deploy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

class DeployByteSerializerTest {

    /** The path to the original JSON the model is loaded from */
    private static final String DEPLOY_TRANSFER_JSON = "/com/casper/sdk/service/json/deploy-transfer.json";

    private final ByteSerializerFactory factory = new ByteSerializerFactory();
    private final ByteSerializer<Deploy> serializer = factory.getByteSerializerByType(Deploy.class);
    private Deploy deploy;

    @BeforeEach
    void setUp() throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        final InputStream in = getClass().getResourceAsStream(DEPLOY_TRANSFER_JSON);
        deploy = mapper.reader().readValue(in, Deploy.class);
    }

    @Test
    void testDeployToBytes() {
        byte[] bytes = serializer.toBytes(deploy);
        assertThat(bytes, is(notNullValue()));
    }
}