package com.casper.sdk.model.deploy;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.AbstractJsonTests;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link Deploy} serialization
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
public class DeploySerializationTest extends AbstractJsonTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeploySerializationTest.class);

    @Test
    void deploySerialization() throws IOException, NoSuchTypeException, ValueSerializationException {
        Digest digest = Digest.builder().digest(
                        "01d9bf2148748a85c89da5aad8ee0b0fc2d105fd39d41a4c796536354f0ae2900ca856a4d37501000080ee36000000000001000000000000004811966d37fe5674a8af4001884ea0d9042d1c06668da0c963769c3a01ebd08f0100000001010101010101010101010101010101010101010101010101010101010101010e0000006361737065722d6578616d706c6501da3c604f71e0e7df83ff1ab4ef15bb04de64ca02e3d2b78de6950e8b5ee187020e0000006361737065722d6578616d706c65130000006578616d706c652d656e7472792d706f696e7401000000080000007175616e7469747904000000e803000001050100000006000000616d6f756e7404000000e8030000010100000001d9bf2148748a85c89da5aad8ee0b0fc2d105fd39d41a4c796536354f0ae2900c012dbf03817a51794a8e19e0724884075e6d1fbec326b766ecfa6658b41f81290da85e23b24e88b1c8d9761185c961daee1adab0649912a6477bcd2e69bd91bd08")
                .build();
        String inputJson = getPrettyJson(loadJsonFromFile("deploy-samples/deploy-serialization.json"));

        LOGGER.debug("Original JSON: {}", inputJson);

        Deploy deploy = OBJECT_MAPPER.readValue(inputJson, Deploy.class);

        assertNotNull(deploy);

        SerializerBuffer ser = new SerializerBuffer();
        deploy.serialize(ser, Target.BYTE);
        byte[] deployBytes = ser.toByteArray();
        String val = Hex.toHexString(deployBytes);
        assertEquals(Hex.toHexString(digest.getDigest()), val);
        assertArrayEquals(digest.getDigest(), deployBytes);
    }

}
