package com.casper.sdk.domain;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.List;

import static com.casper.sdk.domain.ByteUtils.concat;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests for the {@link DeployUtil} class
 */
class DeployUtilTest {

    /**
     * Unit tests the makeTransfer method of the DeployUtil.
     */
    @Test
    void makeTransfer() throws DecoderException {

        final PublicKey targetKey = new PublicKey("0101010101010101010101010101010101010101010101010101010101010101");
        final byte[] expectedIdBytes = Hex.decodeHex("01e703000000000000".toCharArray());
        final byte[] expectedTargetBytes = Hex.decodeHex("0101010101010101010101010101010101010101010101010101010101010101".toCharArray());
        final byte[] expectedAmountBytes = Hex.decodeHex("05005550b405".toCharArray());

        final Transfer transfer = DeployUtil.makeTransfer(
                new BigInteger("24500000000"),
                targetKey,
                new BigInteger("999")
        );

        assertThat(transfer, is(notNullValue()));

        final DeployNamedArg amount = transfer.getNamedArg("amount");
        assertThat(amount.getValue().getCLType(), is(CLType.U512));
        assertThat(amount.getValue().getBytes(), is(expectedAmountBytes));
        assertThat(amount.getValue().getParsed(), is("24500000000"));

        final DeployNamedArg id = transfer.getNamedArg("id");
        assertThat(id.getValue().getCLType(), is(CLType.U64));
        assertThat(id.getValue().getBytes(), is(expectedIdBytes));
        assertThat(id.getValue().getParsed(), is("999"));

        final DeployNamedArg target = transfer.getNamedArg("target");
        assertThat(target.getValue().getCLType(), is(CLType.BYTE_ARRAY));
        assertThat(((CLByteArrayInfo) target.getValue().getCLTypeInfo()).getSize(), is(32));
        assertThat(target.getValue().getBytes(), is(expectedTargetBytes));
        assertThat(target.getValue().getParsed(), is("0101010101010101010101010101010101010101010101010101010101010101"));
    }


    @Test
    void makeDeploy() {

        final Deploy deploy = DeployUtil.makeDeploy(

                new DeployParams(
                        new PublicKey("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"),
                        "mainnet",
                        1,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null),

                DeployUtil.makeTransfer(new BigInteger("24500000000"),
                        new PublicKey("0101010101010101010101010101010101010101010101010101010101010101"),
                        new BigInteger("999")),

                DeployUtil.standardPayment(new BigInteger("1000000000"))
        );

        assertThat(deploy, is(notNullValue()));

    }

    @Test
    void standardPayment() {
    }

    /**
     * Tests a U512 named arg can be converted to a byte array.
     * <p>
     * Array is made of: U32 LE length of name, CL type byte, and byte value
     */
    @Test
    void namedArgU512ToBytes() throws DecoderException {

        final byte[] value = Hex.decodeHex("05005550b405".toCharArray());

        final byte[] expected = concat(
                concat(
                        new byte[]{6, 0, 0, 0}, // Length of name as U32 little endian
                        "amount".getBytes()),// Name
                concat(
                        new byte[]{8},// CLType U512(08)
                        value // byte value
                )
        );

        final DeployNamedArg amount = new DeployNamedArg("amount", new CLValue(value, CLType.U512, "24500000000"));
        final byte[] actual = DeployUtil.toBytes(amount);
        assertThat(actual, is(expected));
    }

    /**
     * Tests a U512 named arg can be converted to a byte array.
     * <p>
     * Array is made of: U32 LE length of name, CL type byte, and byte value
     */
    @Test
    void namedArgU64ToBytes() throws DecoderException {

        final byte[] value = Hex.decodeHex("01e703000000000000".toCharArray());

        final byte[] expected = concat(
                concat(
                        new byte[]{2, 0, 0, 0}, // Length of name as U32 little endian
                        "id".getBytes()),// Name
                concat(
                        new byte[]{5},// CLType U64(5)
                        value // byte value
                )
        );

        final DeployNamedArg id = new DeployNamedArg("id", new CLValue(value, CLType.U64, "999"));
        final byte[] actual = DeployUtil.toBytes(id);
        assertThat(actual, is(expected));
    }

    /**
     * Tests a BYTE_ARRAY named arg can be converted to a byte array.
     * <p>
     * Array is made of: U32 LE length of name, CL type byte, array length, and byte array value
     */
    @Test
    void namedArgByteArrayToBytes() throws DecoderException {
        final byte[] value = Hex.decodeHex("0101010101010101010101010101010101010101010101010101010101010101".toCharArray());

        final byte[] expected = concat(
                concat(
                        new byte[]{6, 0, 0, 0}, // Length of name as U32 little endian
                        "target".getBytes()),// Name
                concat(
                        new byte[]{15, 32, 0, 0, 0},// CLType BYTE_ARRAY(15) followed by array length as LE U32
                        value // byte value
                )
        );

        final DeployNamedArg target = new DeployNamedArg("target",
                new CLValue(
                        Hex.decodeHex("0101010101010101010101010101010101010101010101010101010101010101".toCharArray()),
                        new CLByteArrayInfo(32),
                        "0101010101010101010101010101010101010101010101010101010101010101"
                )
        );
        final byte[] actual = DeployUtil.toBytes(target);
        assertThat(actual, is(expected));
    }

    /**
     * Tests that a list of DeployNamedArg can be converted to a byte array
     */
    @Test
    void namedArgsToBytes() throws DecoderException {

        final List<DeployNamedArg> args = List.of(
                new DeployNamedArg("amount",
                        new CLValue(
                                Hex.decodeHex("05005550b405".toCharArray()),
                                CLType.U512,
                                "24500000000"
                        )
                ),
                new DeployNamedArg("target",
                        new CLValue(
                                Hex.decodeHex("0101010101010101010101010101010101010101010101010101010101010101".toCharArray()),
                                new CLByteArrayInfo(32),
                                "0101010101010101010101010101010101010101010101010101010101010101"
                        )
                ),
                new DeployNamedArg("id",
                        new CLValue(
                                Hex.decodeHex("01e703000000000000".toCharArray()),
                                CLType.U64,
                                "999"
                        )
                )
        );

        final byte[] expected = new byte[]{
                3, 0, 0, 0,
                6, 0, 0, 0, 97, 109, 111, 117, 110, 116, 8, 5, 0, 85, 80, -76, 5, 6, 0, 0, 0, 116, 97, 114,
                103, 101, 116,
                15, 32, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
                1, 1, 1, 1, 1, 1, 1, 1,
                2, 0, 0, 0, 105, 100, 5, 1, -25, 3, 0, 0, 0, 0, 0, 0
        };

        final byte[] bytes = DeployUtil.toBytes(args);

        assertThat(bytes, is(notNullValue()));

        final byte[] len = new byte[4];
        System.arraycopy(bytes, 0, len, 0, len.length);

        // assert the 1st four bytes are the length in LE U32
        assertThat(len, is(new byte[]{3, 0, 0, 0}));

        assertThat(bytes, is(expected));
    }
}