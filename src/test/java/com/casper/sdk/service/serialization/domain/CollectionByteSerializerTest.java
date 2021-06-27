package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.CLType;
import com.casper.sdk.domain.CLValue;
import com.casper.sdk.domain.DeployNamedArg;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.casper.sdk.service.serialization.util.ByteUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class CollectionByteSerializerTest {

    /** The factory to delegate conversion of fields to */
    private final ByteSerializerFactory factory = new ByteSerializerFactory();
    private final TypesFactory typesFactory = new TypesFactory();
    @SuppressWarnings("rawtypes")
    private final ByteSerializer<List> serializer = factory.getByteSerializerByType(List.class);

    @Test
    void emptyListToBytes() {
        assertThat(serializer.toBytes(new ArrayList<>()), is(toU32(0)));
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


        final byte[] expected = concat(
                toU32(2), // Number of arguments

                toU32(6), // length of 'amount;
                "amount".getBytes(StandardCharsets.UTF_8),
                toU32(6), // length of value
                ByteUtils.decodeHex("05005550b405"),
                new byte[]{CLType.U512.getClType()},

                toU32(2), // length of 'id;
                "id".getBytes(StandardCharsets.UTF_8),
                toU32(9),
                decodeHex("01e703000000000000"),
                new byte[]{CLType.U64.getClType()}
        );

        byte[] bytes = serializer.toBytes(args);

        assertThat(bytes, is(expected));
    }

    private byte[] toU32(int value) {
        return typesFactory.getInstance(CLType.U32).serialize(value);
    }
}