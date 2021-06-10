package com.casper.sdk.service.serialization.cltypes;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the {@link StringSerializer}
 */
class StringSerializerTest {

    private final StringSerializer serializer = new StringSerializer(new TypesFactory());

    @Test
    void stringSerialize() {

        byte[] expected = new byte[]{
                11,
                0,
                0,
                0,
                116,
                101,
                115,
                116,
                95,
                (byte) 230,
                (byte) 181,
                (byte) 139,
                (byte) 232,
                (byte) 175,
                (byte) 149
        };

        assertThat(serializer.serialize("test_测试"), is(expected));
    }

    @Test
    void emptyStringSerialize() {
        assertThat(serializer.serialize(""), is(new byte []{0,0,0,0}));
    }

    @Test
    void nullStringSerialize() {
        assertThat(serializer.serialize(null), is(new byte []{0,0,0,0}));
    }

}