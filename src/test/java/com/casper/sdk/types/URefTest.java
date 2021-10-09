package com.casper.sdk.types;

import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests the URef class
 */
class URefTest {

    @Test
    void testConstructFromString() {

        final URef uRef = new URef("uref-ebda3f171068107470bce0d74eb9a302fcb8914471fe8900c66fae258a0f46ef-007");
        assertThat(uRef.getAccessRights(), is(AccessRights.READ_ADD_WRITE));
        assertThat(uRef.getBytes(), is(ByteUtils.decodeHex("ebda3f171068107470bce0d74eb9a302fcb8914471fe8900c66fae258a0f46ef")));
    }
}