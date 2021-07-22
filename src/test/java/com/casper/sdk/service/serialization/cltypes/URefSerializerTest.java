package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.types.AccessRights;
import com.casper.sdk.types.URef;
import com.casper.sdk.service.serialization.util.ByteUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests the URef serialization
 */
class URefSerializerTest {

    private final URefSerializer uRefSerializer = new URefSerializer();

    @Test
    void serializeURef() {

        final String urefAddr = "2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a";

        final byte[] expected = ByteUtils.decodeHex("022a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a2a07");

        final URef uref = new URef(urefAddr, AccessRights.READ_ADD_WRITE);

        final byte[] serialized = uRefSerializer.serialize(uref);

        assertThat(serialized, Is.is(expected));
    }
}