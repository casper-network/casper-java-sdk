package com.casper.sdk.service.serialization.cltypes;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class I64SerializerTest {

    private final I64Serializer serializer = new I64Serializer();

    @Test
    void I64serialize() {

        byte[] expected = new byte[]{57, 20, 94, (byte) 139, 1, 121, (byte) 193, 2};
        assertThat(serializer.serialize(198572906121139257L), is(expected));

        expected = new byte[]{40, 88, (byte) 148, (byte) 186, 102, (byte) 193, (byte) 241, (byte) 255};
        assertThat(serializer.serialize("-4009477689550808"), is(expected));
    }

}