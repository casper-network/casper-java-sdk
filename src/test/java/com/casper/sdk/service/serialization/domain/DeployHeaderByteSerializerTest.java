package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.DeployHeader;
import com.casper.sdk.domain.Digest;
import com.casper.sdk.domain.PublicKey;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class DeployHeaderByteSerializerTest {

    private final DeployHeaderByteSerializer serializer = new DeployHeaderByteSerializer(
            new ByteSerializerFactory(),
            new TypesFactory()
    );

    /**
     * Unit tests that a deploy header can be converted to bytes
     */
    @Test
    void deployHeaderToBytes() {

        // The expected bytes for deserialization of the header
        byte[] expected = {
                1, // Key Tag

                // Key
                (byte) 129, 69, 104, (byte) 208, (byte) 203, (byte) 178, (byte) 192, (byte) 136, (byte) 241,
                (byte) 233, (byte) 154, (byte) 157, 30, (byte) 149, 94, 102, 124, 27, (byte) 240, 126, 118,
                (byte) 218, (byte) 182, 127, (byte) 173, (byte) 199, 46, 62, (byte) 241, (byte) 236, 73, 122,

                38, (byte) 153, (byte) 191, 21, 122, 1, 0, 0,  // Timestamp

                64, 119, 27, 0, 0, 0, 0, 0,  // TTL

                1, 0, 0, 0, 0, 0, 0, 0,  // Gss Price

                // Body Hash
                (byte) 232, (byte) 197, (byte) 255, 16, (byte) 238, (byte) 193, 110, 77, (byte) 158, 91, (byte) 238, 26, 21, 13,
                (byte) 176, 15, 52, (byte) 162, 126, (byte) 216, 53, 25, (byte) 178, (byte) 206, (byte) 136, (byte) 226,
                5, (byte) 180, 33, 15, 94, (byte) 237,

                0, 0, 0, 0, // Dependencies

                12, 0, 0, 0, 116, 101, 115, 116, 45, 110, 101, 116, 119, 111, 114, 107 // Chain name
        };

        // The key
        byte[] key = {
                (byte) 129, 69, 104, (byte) 208, (byte) 203, (byte) 178, (byte) 192, (byte) 136, (byte) 241, (byte) 233,
                (byte) 154, (byte) 157, 30, (byte) 149, 94, 102, 124, 27, (byte) 240, 126, 118, (byte) 218, (byte) 182,
                127, (byte) 173, (byte) 199, 46, 62, (byte) 241, (byte) 236, 73, 122
        };

        // The body hash
        byte[] bodyHash = {
                (byte) 232, (byte) 197, (byte) 255, 16, (byte) 238, (byte) 193, 110, 77, (byte) 158, 91, (byte) 238, 26,
                21, 13, (byte) 176, 15, 52, (byte) 162, 126, (byte) 216, 53, 25, (byte) 178, (byte) 206, (byte) 136,
                (byte) 226, 5, (byte) 180, 33, 15, 94, (byte) 237
        };


        final DeployHeader deployHeader = new DeployHeader(
                new PublicKey(key),
                1623862516006L,
                1800000,
                1,
                new Digest(bodyHash),
                new ArrayList<>(),
                "test-network"
        );

        byte[] bytes = serializer.toBytes(deployHeader);

        assertThat(bytes, is(expected));

    }
}