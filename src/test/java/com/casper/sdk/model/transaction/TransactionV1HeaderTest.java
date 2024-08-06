package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.pricing.FixedPricingMode;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author ian@meywood.com
 */
class TransactionV1HeaderTest {

    @Test
    void transactionV1HeaderByteSerialization() throws NoSuchAlgorithmException, NoSuchTypeException, ValueSerializationException {

        byte[] expected = {
                0x14, 0x0, 0x0, 0x0, (byte) 0xf1, (byte) 0x9b, (byte) 0xb0, (byte) 0xa4, (byte) 0xf1, (byte) 0xb8,
                (byte) 0x97, (byte) 0xb6, (byte) 0xf0, (byte) 0xbb, (byte) 0x86, (byte) 0x8a, (byte) 0xf0, (byte) 0xb1,
                (byte) 0x9f, (byte) 0x88, (byte) 0xf3, (byte) 0x9f, (byte) 0x95, (byte) 0xa2, (byte) 0xd4, (byte) 0xfb,
                (byte) 0x85, (byte) 0xc6, 0x73, 0x1, 0x0, 0x0,
                0x40, 0x77, 0x1b, 0x0, 0x0, 0x0, 0x0, 0x0, // 30m TTL
                0x41, 0x14, 0x17, (byte) 0xa9, 0x74, 0x2a, (byte) 0xee, 0x4f, 0x67, 0x58, (byte) 0xd5, (byte) 0x9c, 0x3c,
                0x6b, 0x59, (byte) 0x88, (byte) 0xcc, (byte) 0xaa, (byte) 0x8a, (byte) 0xde, 0x1a, 0x7a, 0x7e, (byte) 0xa9,
                (byte) 0x8d, (byte) 0x92, 0x62, (byte) 0xac, 0x63, 0x49, 0x22, (byte) 0x9a, 0x1, 0x5, 0x0, 0x2, 0x2,
                (byte) 0xe5, 0x54, (byte) 0xfa, 0x19, (byte) 0x93, (byte) 0xb3, (byte) 0xaa, 0x36, 0x4d, 0x25,
                (byte) 0xae, (byte) 0xfd, 0x47, (byte) 0xc9, 0x50, (byte) 0xec, 0x31, (byte) 0xe9, 0x56, 0x3f,
                (byte) 0xb9, 0x6f, (byte) 0xe2, 0x8, 0x27, (byte) 0x92, (byte) 0xca, 0x48, (byte) 0xed, (byte) 0xc7,
                (byte) 0x93, 0x3b
        };

        final byte[] hash = {
                0x41, 0x14, 0x17, (byte) 0xa9, 0x74, 0x2a, (byte) 0xee, 0x4f, 0x67, 0x58, (byte) 0xd5, (byte) 0x9c, 0x3c,
                0x6b, 0x59, (byte) 0x88, (byte) 0xcc, (byte) 0xaa, (byte) 0x8a, (byte) 0xde, 0x1a, 0x7a, 0x7e, (byte) 0xa9,
                (byte) 0x8d, (byte) 0x92, 0x62, (byte) 0xac, 0x63, 0x49, 0x22, (byte) 0x9a
        };

        final String hexHash = Hex.encode(hash);

        final byte[] initiatorAddr = {
                0x02, 0x2, (byte) 0xe5, 0x54, (byte) 0xfa, 0x19, (byte) 0x93, (byte) 0xb3, (byte) 0xaa, 0x36, 0x4d, 0x25,
                (byte) 0xae, (byte) 0xfd, 0x47, (byte) 0xc9, 0x50, (byte) 0xec, 0x31, (byte) 0xe9, 0x56, 0x3f,
                (byte) 0xb9, 0x6f, (byte) 0xe2, 0x8, 0x27, (byte) 0x92, (byte) 0xca, 0x48, (byte) 0xed, (byte) 0xc7,
                (byte) 0x93, 0x3b
        };

        final TransactionV1Header header = TransactionV1Header.builder()
                .chainName("\uD92F\uDC24\uD9A1\uDDF6\uD8AC\uDD8A\uD885\uDFC8\uDB3D\uDD62")
                .timestamp(new Date(1596763536340L))
                .ttl(Ttl.builder().ttl("30m").build())
                .bodyHash(new Digest(hexHash))
                .pricingMode(new FixedPricingMode(5))
                .initiatorAddr(new InitiatorPublicKey(PublicKey.fromBytes(initiatorAddr)))
                .build();

        final SerializerBuffer serializerBuffer = new SerializerBuffer();
        header.serialize(serializerBuffer, Target.BYTE);

        final byte[] actual = serializerBuffer.toByteArray();
        final String hexActual = Hex.encode(actual);
        final String hexExpected = Hex.encode(expected);

        assertThat(hexActual, is(hexExpected));
    }
}
