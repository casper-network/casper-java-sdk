package com.casper.sdk.service.serialization.cltypes;


import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static com.casper.sdk.service.serialization.util.ByteUtils.decodeHex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for U64 byte serialization
 */
class U64SerializerTest {

    private final U64Serializer serializer = new U64Serializer();

    @Test
    void serializeU64() {

        assertThat(serializer.serialize(Long.MAX_VALUE), is(decodeHex("ffffffffffffff7f")));

        byte[] expected = new byte[]{57, 20, (byte) 214, (byte) 178, (byte) 212, 118, 11, (byte) 197};
        assertThat(serializer.serialize("14198572906121139257"), is(expected));

        expected = new byte[]{(byte) 216, (byte) 167, (byte) 130, 99, (byte) 132, 107, 121, (byte) 136};
        assertThat(serializer.serialize(new BigInteger("9834009477689550808")), is(expected));

        assertThat(serializer.serialize(0), is(decodeHex("0000000000000000")));
        assertThat(serializer.serialize("0"), is(decodeHex("0000000000000000")));
        assertThat(serializer.serialize("1"), is(decodeHex("0100000000000000")));
        assertThat(serializer.serialize("1234"), is(decodeHex("d204000000000000")));
        assertThat(serializer.serialize(0x01020304), is(decodeHex("0403020100000000")));
        assertThat(serializer.serialize(0x0102030405L), is(decodeHex("0504030201000000")));

        assertThat(serializer.serialize(new BigInteger("18446744073709551615")), is(decodeHex("ffffffffffffffff")));

    }
}