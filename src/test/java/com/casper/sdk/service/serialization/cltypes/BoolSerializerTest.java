package com.casper.sdk.service.serialization.cltypes;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests the CLType.BOOL serializer
 */
class BoolSerializerTest {

    private final BoolSerializer boolSerializer = new BoolSerializer();

    @Test
    void serializeBool() {

        final byte[] expectedFalseBytes = {0};
        final byte[] expectedTrueBytes = {1};
        
        assertThat(boolSerializer.serialize("false"), is(expectedFalseBytes));
        assertThat(boolSerializer.serialize(false), is(expectedFalseBytes));
        assertThat(boolSerializer.serialize("true"), is(expectedTrueBytes));
        assertThat(boolSerializer.serialize(true), is(expectedTrueBytes));
        // We treat null as false
        try {
            assertThat(boolSerializer.serialize(null), is(expectedFalseBytes));
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            // passed
        }
    }
}