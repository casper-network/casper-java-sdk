package com.casper.sdk.model.transaction;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    void testInitiatorPublicKey() throws JsonProcessingException, NoSuchAlgorithmException {

        final String json = "{\"PublicKey\":\"010b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd\"}";
        final InitiatorAddr initiatorAddress = new ObjectMapper().readValue(json, InitiatorAddr.class);
        assertThat(initiatorAddress, is(instanceOf(InitiatorPublicKey.class)));
        assertThat(initiatorAddress.getAddress(), is(PublicKey.fromTaggedHexString("010b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd")));

        final String written = new ObjectMapper().writeValueAsString(initiatorAddress);
        assertThat(written, is(json));
    }

    @Test
    void testInitiatorAccountHash() throws JsonProcessingException {

        final String json = "{\"AccountHash\":\"0b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd\"}";
        final InitiatorAddr initiatorAddress = new ObjectMapper().readValue(json, InitiatorAddr.class);
        assertThat(initiatorAddress, is(instanceOf(InitiatorAccountHash.class)));
        assertThat(initiatorAddress.getAddress(), is(new Digest("0b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd")));

        final String written = new ObjectMapper().writeValueAsString(initiatorAddress);
        assertThat(written, is(json));
    }
}
