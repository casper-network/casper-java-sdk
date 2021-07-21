package com.casper.sdk.service.serialization.types;

import com.casper.sdk.types.CLByteArrayInfo;
import com.casper.sdk.types.CLType;
import com.casper.sdk.types.CLValue;
import com.casper.sdk.types.DeployNamedArg;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.casper.sdk.service.serialization.util.ByteUtils.concat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

class DeployNamedArgByteSerializerTest {

    private final ByteSerializerFactory factory = new ByteSerializerFactory();
    private final ByteSerializer<DeployNamedArg> deployNamedArgByteSerializer = factory.getByteSerializerByType(DeployNamedArg.class);
    @SuppressWarnings("rawtypes")
    private final ByteSerializer<List> listByteSerializer = factory.getByteSerializerByType(List.class);
    private final TypesFactory typesFactory = new TypesFactory();

    /**
     * Tests a U512 named arg can be converted to a byte array.
     * <p>
     * Array is made of: U32 LE length of name, CL type byte, and byte value
     */
    @Test
    void namedArgU64ToBytes() {

        final byte[] value = ByteUtils.decodeHex("01e703000000000000");

        final byte[] expected = concat(
                concat(
                        new byte[]{2, 0, 0, 0}, // Length of name as U32 little endian
                        "id".getBytes()),// Name
                concat(
                        toU32(value.length),  // length of value
                        value,                          // byte value
                        new byte[]{5}                   // CLType U64(5)
                )
        );

        final DeployNamedArg id = new DeployNamedArg("id", new CLValue(value, CLType.U64, "999"));
        final byte[] actual = deployNamedArgByteSerializer.toBytes(id);
        assertThat(actual, is(expected));
    }


    /**
     * Tests a U512 named arg can be converted to a byte array.
     * <p>
     * Array is made of: U32 LE length of name, CL type byte, and byte value
     */
    @Test
    void namedArgU512ToBytes() {

        final byte[] value = ByteUtils.decodeHex("05005550b405");

        final byte[] expected = concat(
                concat(
                        new byte[]{6, 0, 0, 0}, // Length of name as U32 little endian
                        "amount".getBytes()),// Name
                concat(
                        toU32(value.length),
                        value, // byte value
                        new byte[]{8}// CLType U512(08)
                )
        );

        final DeployNamedArg amount = new DeployNamedArg("amount", new CLValue(value, CLType.U512, "24500000000"));
        final byte[] actual = deployNamedArgByteSerializer.toBytes(amount);
        assertThat(actual, is(expected));
    }

    /**
     * Tests a BYTE_ARRAY named arg can be converted to a byte array.
     * <p>
     * Array is made of: U32 LE length of name, CL type byte, array length, and byte array value
     */
    @Test
    void namedArgByteArrayToBytes() {
        final byte[] value = ByteUtils.decodeHex("0101010101010101010101010101010101010101010101010101010101010101");

        final byte[] expected = concat(
                concat(
                        toU32(6), // Length of name as U32 little endian
                        "target".getBytes()),// Name
                concat(
                        toU32(value.length), // length of array
                        value, // byte value of array
                        new byte[]{CLType.BYTE_ARRAY.getClType()},
                        toU32(value.length)
                )
        );

        final DeployNamedArg target = new DeployNamedArg("target",
                new CLValue(
                        ByteUtils.decodeHex("0101010101010101010101010101010101010101010101010101010101010101"),
                        new CLByteArrayInfo(32),
                        "0101010101010101010101010101010101010101010101010101010101010101"
                )
        );
        final byte[] actual = deployNamedArgByteSerializer.toBytes(target);
        assertThat(actual, is(expected));
    }

    /**
     * Tests that a list of DeployNamedArg can be converted to a byte array
     */
    @Test
    void namedArgsToBytes() {

        final List<DeployNamedArg> args = List.of(
                new DeployNamedArg("amount",
                        new CLValue(
                                ByteUtils.decodeHex("05005550b405"),
                                CLType.U512,
                                "24500000000"
                        )
                ),
                new DeployNamedArg("target",
                        new CLValue(
                                ByteUtils.decodeHex("0101010101010101010101010101010101010101010101010101010101010101"),
                                new CLByteArrayInfo(32),
                                "0101010101010101010101010101010101010101010101010101010101010101"
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

        final byte[] expected = new byte[]{
                3, 0, 0, 0,
                6, 0, 0, 0,
                97, 109, 111, 117, 110, 116,
                6, 0, 0, 0,
                5, 0, 85, 80, -76, 5, 8,
                6, 0, 0, 0,
                116, 97, 114, 103, 101, 116,
                32, 0, 0, 0,
                1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 15,
                32, 0, 0, 0,
                2, 0, 0, 0,
                105, 100,
                9, 0, 0, 0, 1, -25, 3, 0, 0, 0, 0, 0, 0, 5
        };

        final byte[] bytes = listByteSerializer.toBytes(args);

        assertThat(bytes, is(notNullValue()));

        final byte[] len = new byte[4];
        System.arraycopy(bytes, 0, len, 0, len.length);

        // assert the 1st four bytes are the length in LE U32
        assertThat(len, is(new byte[]{3, 0, 0, 0}));

        assertThat(bytes, is(expected));
    }

    private byte[] toU32(int value) {
        return typesFactory.getInstance(CLType.U32).serialize(value);
    }
}