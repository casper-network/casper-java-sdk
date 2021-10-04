package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.SignatureAlgorithm;
import com.casper.sdk.types.CLPublicKey;
import com.casper.sdk.service.serialization.util.ByteUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests the {@link PublicKeyByteSerializer}
 */
class CLPublicKeyByteSerializerTest {

    private final PublicKeyByteSerializer serializer = new PublicKeyByteSerializer();

    @Test
    void publicKeyToBytes() {

        // The key
        final byte[] key = {
                (byte) 161, (byte) 154, (byte) 207, (byte) 105, (byte) 157, (byte) 211, (byte) 135, (byte) 135,
                (byte) 131, (byte) 191, (byte) 194, (byte) 208, (byte) 139, 29, 117, 39, (byte) 180, 73, (byte) 129,
                68, 8, 47, (byte) 215, (byte) 174, (byte) 210, (byte) 233, (byte) 207, 3, (byte) 158, (byte) 209, (byte)
                237, 82
        };

        final byte[] expected = ByteUtils.concat(new byte[]{1}, key);

        final byte[] bytes = serializer.toBytes(new CLPublicKey(key, SignatureAlgorithm.ED25519));
        assertThat(bytes, Is.is(expected));

    }
}