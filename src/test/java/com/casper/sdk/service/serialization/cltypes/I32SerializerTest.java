package com.casper.sdk.service.serialization.cltypes;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class I32SerializerTest {

    private final I32Serializer serializer = new I32Serializer();

    @Test
    void I32serialize() {

        byte[] expected = new byte[]{96, 121, (byte) 254, (byte) 255};
        assertThat(serializer.serialize(-100000), is(expected));

        expected = new byte[]{(byte) 160, (byte) 134, 1, 0};
        assertThat(serializer.serialize(100000), is(expected));

        expected = new byte[]{0, 0, 0, 0};
        assertThat(serializer.serialize(0), is(expected));

        expected = new byte[]{(byte) 255, (byte) 255, (byte) 255, (byte) 255};
        assertThat(serializer.serialize(-1), is(expected));
    }
}