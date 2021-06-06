package com.casper.sdk.service.serialization.cltypes;

import org.junit.jupiter.api.Test;

import static com.casper.sdk.service.serialization.util.ByteUtils.decodeHex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class U32Test {

    private final U32 serializer = new U32();

    @Test
    public void testU32() {
        assertThat(serializer.serialize("1024"), is(decodeHex("00040000")));
        assertThat(serializer.serialize(2048), is(decodeHex("00080000")));
        assertThat(serializer.serialize(2049), is(decodeHex("01080000")));
    }
}