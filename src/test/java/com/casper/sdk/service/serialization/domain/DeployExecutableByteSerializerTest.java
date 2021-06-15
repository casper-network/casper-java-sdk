package com.casper.sdk.service.serialization.domain;

import com.casper.sdk.domain.*;
import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests the {@link DeployExecutableByteSerializer}.
 */
class DeployExecutableByteSerializerTest {

    private final ByteSerializerFactory byteSerializerFactory = new ByteSerializerFactory();
    private final DeployExecutableByteSerializer serializer = (DeployExecutableByteSerializer) byteSerializerFactory.getByteSerializerByType(DeployExecutable.class);

    /**
     * Tests module bytes can be converted to a casper serialised  byte array
     */
    @Test
    void moduleBytesToBytes() {

        final BigInteger paymentAmount = new BigInteger("1000000000");
        final byte[] amountBytes = ByteUtils.toU512(paymentAmount);
        final DeployNamedArg paymentArg = new DeployNamedArg(
                "amount",
                new CLValue(amountBytes, CLType.U512, paymentAmount)
        );

        final ModuleBytes moduleBytes = new ModuleBytes(new byte[0], List.of(paymentArg));

        final byte[] expected = new byte[]{
                0, // Type 0 module bytes
                0, // Module bytes - length zero
                0,
                0,
                0,
                1, // args (number of == 1)
                0,
                0,
                0,
                6, //  length of 'amount'
                0,
                0,
                0,
                97,
                109,
                111,
                117,
                110,
                116,
                5,   // length of value  '04 00 ca 9a 3b'
                0,
                0,
                0,
                4,   // Value
                0,
                (byte) 202,
                (byte) 154,
                59,
                8   // type of value
        };

        final byte[] actual = serializer.toBytes(moduleBytes);
        assertThat(actual, is(expected));
    }


    @Test
    void sessionToBytes() {

        byte[] expected = {
                5, // Transfer Type 5
                3, // Number of parameters
                0,
                0,
                0,
                6, // 'amount' length
                0,
                0,
                0,
                97,     // a
                109,    // m
                111,    // o
                117,    // u
                110,    // n
                116,    // t
                6,      // length of value
                0,
                0,
                0,
                5,      // value
                0,
                116,
                59,
                (byte) 164,
                11,
                8,
                6,
                0,
                0,
                0,
                116,
                97,
                114,
                103,
                101,
                116,
                32,
                0,
                0,
                0,
                21,
                65,
                86,
                107,
                (byte) 218,
                (byte) 211,
                (byte) 163,
                (byte) 207,
                (byte) 169,
                (byte) 235,
                76,
                (byte) 186,
                61,
                (byte) 207,
                51,
                (byte) 238,
                101,
                (byte) 131,
                (byte) 224,
                115,
                58,
                (byte) 228,
                (byte) 178,
                (byte) 204,
                (byte) 223,
                (byte) 233,
                44,
                (byte) 209,
                (byte) 189,
                (byte) 146,
                (byte) 238,
                22,
                15,
                32,  // Length of byte array
                0,
                0,
                0,
                2,   // Length of ID ?
                0,
                0,
                0,
                105,  // 'i'
                100,  // 'd'
                9,
                0,
                0,
                0,
                1,    // OPTION_SOME
                (byte) 160,
                (byte) 134,
                1,
                0,
                0,
                0,
                0,
                0,
                13,     // Option type
                5       // U64 type
        };

        assertThat(expected.length, is(98));

        final Transfer transfer = DeployUtil.makeTransfer(new BigInteger("50000000000"),
                new PublicKey("1541566bdad3a3cfa9eb4cba3dcf33ee6583e0733ae4b2ccdfe92cd1bd92ee16"),
                100000L);

        final byte[] actual = serializer.toBytes(transfer);

        assertThat(actual, is(expected));
    }
}