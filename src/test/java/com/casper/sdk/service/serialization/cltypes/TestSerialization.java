package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.types.CLType;
import org.junit.jupiter.api.Test;

import static com.casper.sdk.service.serialization.util.ByteUtils.decodeHex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;


public class TestSerialization {

    private final TypesFactory factory = new TypesFactory();

    @Test
    public void testU512() {

        byte [] value = factory.getInstance(CLType.U512).serialize("123456789101112131415");
        assertThat(value, is(decodeHex("0957ff1ada959f4eb106")));

        value = factory.getInstance(CLType.U512).serialize("7");
        assertThat(value, is(decodeHex("0107")));

        value = factory.getInstance(CLType.U512).serialize("1024");
        assertThat(value, is(decodeHex("020004")));

        value = factory.getInstance(CLType.U512).serialize("10111111111111");
        assertThat(value, is(decodeHex("06c7a12f2d3209")));

        value = factory.getInstance(CLType.U512).serialize("24500000000");
        assertThat(value, is(decodeHex("05005550b405")));
    }

    @Test
    public void testU64() {

        byte [] value = factory.getInstance(CLType.U64).serialize("999");
        assertThat(value, is(decodeHex("e703000000000000")));

        value = factory.getInstance(CLType.U64).serialize("0");
        assertThat(value, is(decodeHex("0000000000000000")));

        value = factory.getInstance(CLType.U64).serialize("");
        assertThat(value, is(decodeHex("0000000000000000")));
    }

    @Test
    public void testU32() {

        byte [] value = factory.getInstance(CLType.U32).serialize("1024");
        assertThat(value, is(decodeHex("00040000")));
    }

    @Test
    public void testString() {
        byte [] value = factory.getInstance(CLType.STRING).serialize("Hello, World!");
        assertThat(value, is(decodeHex("0d00000048656c6c6f2c20576f726c6421")));

        value = factory.getInstance(CLType.STRING).serialize("this is transfer");
        assertThat(value, is(decodeHex("1000000074686973206973207472616e73666572")));
    }
}
