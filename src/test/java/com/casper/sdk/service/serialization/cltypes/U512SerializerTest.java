package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit test for the U512 byte serializer
 */
class U512SerializerTest {

    private final U512Serializer serializer = new U512Serializer();

    @Test
    void serializeU512() {

        byte[] actual = serializer.serialize(new BigInteger("10000000000"));
        byte[] expected = ByteUtils.decodeHex("0500e40b5402");
        assertThat(actual, is(expected));

        actual = serializer.serialize(new BigInteger("1000000000"));
        expected = ByteUtils.decodeHex("0400ca9a3b");
        assertThat(actual, is(expected));
    }
}