package com.casper.sdk.service.serialization.cltypes;

import org.junit.jupiter.api.Test;

import static com.casper.sdk.service.serialization.util.ByteUtils.decodeHex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class U32SerializerTest {

    private final U32Serializer serializer = new U32Serializer();

    @Test
    public void testU32() {
        assertThat(serializer.serialize("1024"), is(decodeHex("00040000")));
        assertThat(serializer.serialize(2048), is(decodeHex("00080000")));
        assertThat(serializer.serialize(2049), is(decodeHex("01080000")));

        assertThat(serializer.serialize(0xf0e0_d0c0), is(decodeHex( "c0d0e0f0")));
        assertThat(serializer.serialize(100000), is(new byte[]{(byte) 160, (byte) 134, 1, 0}));
    }
}