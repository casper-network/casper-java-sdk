package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.CLType;
import com.casper.sdk.domain.CLValue;
import com.casper.sdk.domain.DeployNamedArg;
import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class CollectionByteSerializerTest {

    /** The factory to delegate conversion of fields to */
    private final ByteSerializerFactory factory = new ByteSerializerFactory();
    @SuppressWarnings("rawtypes")
    private final ByteSerializer<List> serializer = factory.getByteSerializerByType(List.class);

    @Test
    void emptyListToBytes() {
        assertThat(serializer.toBytes(new ArrayList<>()), is(ByteUtils.toU32(0)));
    }

    @Test
    void populatedListToBytes() {

        final List<DeployNamedArg> args = List.of(
                new DeployNamedArg("amount",
                        new CLValue(
                                ByteUtils.decodeHex("05005550b405"),
                                CLType.U512,
                                "24500000000"
                        )
                ),

                new DeployNamedArg("id",
                        new CLValue(
                                ByteUtils.decodeHex("01e703000000000000"),
                                CLType.U64,
                                "999"
                        )
                )
        );

        final String expectedHex = "0200000006000000616d6f756e740805005550b4050200000069640501e703000000000000";
        byte[] bytes = serializer.toBytes(args);

        assertThat(bytes, is(ByteUtils.decodeHex(expectedHex)));
    }
}