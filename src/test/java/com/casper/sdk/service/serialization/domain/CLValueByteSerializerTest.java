package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.CLType;
import com.casper.sdk.domain.CLValue;
import com.casper.sdk.service.serialization.util.ByteUtils;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class CLValueByteSerializerTest {


    private final CLValueByteSerializer byteSerializer = new CLValueByteSerializer();

    @Test
    void u64toBytes() {

        final String hex = "0501e703000000000000";
        final byte [] expected = ByteUtils.decodeHex(hex);

        CLValue source = new CLValue(
                ByteUtils.decodeHex("01e703000000000000"),
                CLType.U64,
                "999"
        );

        assertThat(byteSerializer.toBytes(source), is(expected));
    }
}