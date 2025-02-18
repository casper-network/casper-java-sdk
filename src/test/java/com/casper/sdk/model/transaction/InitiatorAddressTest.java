package com.casper.sdk.model.transaction;

import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.key.AccountHashKey;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.SerializerBuffer;
import org.junit.jupiter.api.Test;

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
    void initiatorPublicKeyJson() throws Exception {

        byte[] expected = {2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1, 0, 0, 0, 34, 0, 0, 0, 0, 1, (byte) 165, (byte) 165,
                (byte) 183, 50, (byte) 129, 24, 104, 22, 56, (byte) 190, 62, 6, (byte) 200, 116, (byte) 150, 9, 40, 13,
                (byte) 186, 76, (byte) 157, (byte) 175, (byte) 154, (byte) 235, 61, 52, 100, (byte) 184, (byte) 131, (byte) 155, 1, (byte) 138};

        final String json = "{\"PublicKey\":\"01a5a5b7328118681638be3e06c8749609280dba4c9daf9aeb3d3464b8839b018a\"}";
        final InitiatorAddr<?> initiatorAddress = new ObjectMapper().readValue(json, InitiatorAddr.class);
        assertThat(initiatorAddress, is(instanceOf(InitiatorPublicKey.class)));
        assertThat(initiatorAddress.getAddress(), is(PublicKey.fromTaggedHexString("01a5a5b7328118681638be3e06c8749609280dba4c9daf9aeb3d3464b8839b018a")));

        final String written = new ObjectMapper().writeValueAsString(initiatorAddress);
        assertThat(written, is(json));

        assertThat(initiatorAddress.getByteTag(), is((byte) 0));

        final SerializerBuffer ser = new SerializerBuffer();
        initiatorAddress.serialize(ser, Target.BYTE);
        final byte[] actual = ser.toByteArray();
        assertThat(actual, is(expected));
    }

    @Test
    void initiatorAccountHashJson() throws Exception {

        final byte[] expected = Hex.decode("0200000000000000000001000100000021000000010b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd");

        final String json = "{\"AccountHash\":\"account-hash-0b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd\"}";
        final InitiatorAddr<?> initiatorAddress = new ObjectMapper().readValue(json, InitiatorAddr.class);
        assertThat(initiatorAddress, is(instanceOf(InitiatorAccountHash.class)));
        assertThat(initiatorAddress.getAddress(), is(AccountHashKey.create("account-hash-0b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd")));

        final String written = new ObjectMapper().writeValueAsString(initiatorAddress);
        assertThat(written, is(json));

        assertThat(initiatorAddress.getByteTag(), is((byte) 1));

        final SerializerBuffer ser = new SerializerBuffer();
        initiatorAddress.serialize(ser, Target.BYTE);
        final byte[] actual = ser.toByteArray();
        assertThat(actual, is(expected));
    }
}
