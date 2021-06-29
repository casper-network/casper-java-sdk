package com.casper.sdk.service.serialization.cltypes;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.fail;

class U8SerializerTest {

    private final U8Serializer u8Serializer = new U8Serializer();

    @Test
    void serializeU8() {

        assertThat(u8Serializer.serialize(0x08), is(new byte[]{8}));
        assertThat(u8Serializer.serialize(0xff), is(new byte[]{(byte) 255}));
        assertThat(u8Serializer.serialize(0x0a), is(new byte[]{(byte) 10}));
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