package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Unit tests for {@link InitiatorAddr}.
 *
 * @author ian@meywood.com
 */
class InitiatorAddressTest {

    @Test
    void initiatorPublicKeyJson() throws JsonProcessingException, NoSuchAlgorithmException {

        final String json = "{\"PublicKey\":\"010b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd\"}";
        final InitiatorAddr initiatorAddress = new ObjectMapper().readValue(json, InitiatorAddr.class);
        assertThat(initiatorAddress, is(instanceOf(InitiatorPublicKey.class)));
        assertThat(initiatorAddress.getAddress(), is(PublicKey.fromTaggedHexString("010b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd")));

        final String written = new ObjectMapper().writeValueAsString(initiatorAddress);
        assertThat(written, is(json));

        assertThat(initiatorAddress.getByteTag(), is((byte)0));
    }

    @Test
    void initiatorPublicKeyToBytes() throws NoSuchAlgorithmException, NoSuchTypeException, ValueSerializationException {

        byte[] expected = {
                0x0,
                0x02, 0x2, (byte) 0xe5, 0x54, (byte) 0xfa, 0x19, (byte) 0x93, (byte) 0xb3, (byte) 0xaa, 0x36, 0x4d, 0x25,
                (byte) 0xae, (byte) 0xfd, 0x47, (byte) 0xc9, 0x50, (byte) 0xec, 0x31, (byte) 0xe9, 0x56, 0x3f,
                (byte) 0xb9, 0x6f, (byte) 0xe2, 0x8, 0x27, (byte) 0x92, (byte) 0xca, 0x48, (byte) 0xed, (byte) 0xc7,
                (byte) 0x93, 0x3b
        };

        final byte[] publicKeyBytes = {
                0x02, 0x2, (byte) 0xe5, 0x54, (byte) 0xfa, 0x19, (byte) 0x93, (byte) 0xb3, (byte) 0xaa, 0x36, 0x4d, 0x25,
                (byte) 0xae, (byte) 0xfd, 0x47, (byte) 0xc9, 0x50, (byte) 0xec, 0x31, (byte) 0xe9, 0x56, 0x3f,
                (byte) 0xb9, 0x6f, (byte) 0xe2, 0x8, 0x27, (byte) 0x92, (byte) 0xca, 0x48, (byte) 0xed, (byte) 0xc7,
                (byte) 0x93, 0x3b
        };

        final InitiatorPublicKey initiatorPublicKey = new InitiatorPublicKey(PublicKey.fromBytes(publicKeyBytes));

        final SerializerBuffer ser = new SerializerBuffer();
        initiatorPublicKey.serialize(ser, null);
        byte[] actual = ser.toByteArray();

        assertThat(actual, is(expected));
    }

    @Test
    void initiatorAccountHashJson() throws JsonProcessingException {

        final String json = "{\"AccountHash\":\"0b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd\"}";
        final InitiatorAddr initiatorAddress = new ObjectMapper().readValue(json, InitiatorAddr.class);
        assertThat(initiatorAddress, is(instanceOf(InitiatorAccountHash.class)));
        assertThat(initiatorAddress.getAddress(), is(new Digest("0b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd")));

        final String written = new ObjectMapper().writeValueAsString(initiatorAddress);
        assertThat(written, is(json));

        assertThat(initiatorAddress.getByteTag(), is((byte)1));

    }

    @Test
    void initiatorAccountHashBytes() throws NoSuchTypeException, ValueSerializationException {
        byte[] expected = {
                0x1,
                0x49,  (byte) 0xdf, 0x7b,  (byte)0xa7, 0x5e,  (byte)0xf0, 0xc, 0x0,  (byte)0xf5,  (byte)0xf5,
                (byte) 0x9f, 0x56,  (byte)0xe9, 0x49, 0x5, 0x0,  (byte)0xde,  (byte)0x98, 0x1, 0x53, (byte) 0xf0,
                (byte) 0x92, 0x0, 0x0, 0x5f, 0x3, 0x44, 0x27, 0x7b, 0x3c, 0x0, 0x0
        };

        final byte[] digestBytes = {
                0x49,  (byte) 0xdf, 0x7b,  (byte)0xa7, 0x5e,  (byte)0xf0, 0xc, 0x0,  (byte)0xf5,  (byte)0xf5,
                (byte) 0x9f, 0x56,  (byte)0xe9, 0x49, 0x5, 0x0,  (byte)0xde,  (byte)0x98, 0x1, 0x53, (byte) 0xf0,
                (byte) 0x92, 0x0, 0x0, 0x5f, 0x3, 0x44, 0x27, 0x7b, 0x3c, 0x0, 0x0
        };

        final InitiatorAccountHash initiatorPublicKey = new InitiatorAccountHash(new Digest(Hex.encode(digestBytes)));

        final SerializerBuffer ser = new SerializerBuffer();
        initiatorPublicKey.serialize(ser, null);
        byte[] actual = ser.toByteArray();

        assertThat(actual, is(expected));

    }
}
