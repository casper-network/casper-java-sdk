package com.casper.sdk.service.serialization.cltypes;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.fail;

class U8Test {

    private final U8 u8Serializer = new U8();

    @Test
    void serializeU8() {

        assertThat(u8Serializer.serialize((byte) 8), is(new byte[]{8}));
        assertThat(u8Serializer.serialize(0xff), is(new byte[]{(byte) 255}));
        assertThat(u8Serializer.serialize("ff"), is(new byte[]{(byte) 255}));
    }

    @Test
    void invalidByteValues() {

        try {
            u8Serializer.serialize(256);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Not a valid byte 256"));
        }

        try {
           u8Serializer.serialize("1FF");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Not a valid byte 1FF"));
        }

    }
}