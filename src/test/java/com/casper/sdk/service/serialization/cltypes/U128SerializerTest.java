package com.casper.sdk.service.serialization.cltypes;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class U128SerializerTest {

    private final U128Serializer serializer = new U128Serializer();

    @Test
    void serializeU128() {

        byte[] expected = new byte[]{3, (byte) 160, (byte) 134, 1};
        assertThat(serializer.serialize(100000), is(expected));

        expected = new byte[]{6, 0, 0, (byte) 0xc0, (byte) 0xd0, (byte) 0xe0, (byte) 0xf0};
        assertThat(serializer.serialize(0xf0e0_d0c0_0000L), is(expected));

        expected = new byte[]{6, 0, 0, (byte) 0xc0, (byte)  0xd0, (byte) 0xe0, (byte) 0xf0};
        assertThat(serializer.serialize(0x0000_f0e0_d0c0_0000L), is(expected));

        expected = new byte[]{6, 0, 0, (byte) 0xc0, (byte)  0xd0, (byte) 0xe0, (byte) 0xf0};
        assertThat(serializer.serialize(0x0000_f0e0_d0c0_0000L), is(expected));

    }
}