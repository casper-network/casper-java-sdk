package com.casper.sdk.service.serialization.cltypes;

import com.casper.sdk.types.CLType;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static com.casper.sdk.service.serialization.util.ByteUtils.decodeHex;
import static org.hamcrest.MatcherAssert.assertThat;

class ByteArraySerializerTest {

    private final TypesFactory typesFactory = new TypesFactory();
    private final ByteArraySerializer serializer = typesFactory.getInstance(CLType.BYTE_ARRAY);

    @Test
    void serializeByteArray() {
        byte[] actual = serializer.serialize(new byte[]{1, 2, 3});
        assertThat(actual, Is.is(decodeHex("03000000010203")));
    }

    @Test
    void serializeEmptyByteArray() {
        byte[] actual = serializer.serialize(new byte[0]);
        assertThat(actual, Is.is(decodeHex("00000000")));

        actual = serializer.serialize(null);
        assertThat(actual, Is.is(decodeHex("00000000")));

        actual = serializer.serialize("");
        assertThat(actual, Is.is(decodeHex("00000000")));
    }
}