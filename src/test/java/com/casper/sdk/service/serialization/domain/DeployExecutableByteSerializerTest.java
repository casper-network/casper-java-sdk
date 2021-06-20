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

        final byte[] recipientPublicKey = {
                (byte) 125, (byte) 92, (byte) 32, (byte) 204, (byte) 192, (byte) 151, (byte) 192, (byte) 180,
                (byte) 177, (byte) 137, (byte) 29, (byte) 12, (byte) 249, (byte) 243, (byte) 120, (byte) 157,
                (byte) 169, (byte) 117, (byte) 243, (byte) 50, (byte) 243, (byte) 216, (byte) 211, (byte) 229,
                (byte) 42, (byte) 32, (byte) 235, (byte) 210, (byte) 78, (byte) 4, (byte) 71, (byte) 203
        };

        byte[] expected = {
                (byte) 5, (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 6, (byte) 0, (byte) 0, (byte) 0, (byte) 97,
                (byte) 109, (byte) 111, (byte) 117, (byte) 110, (byte) 116, (byte) 2, (byte) 0, (byte) 0, (byte) 0,
                (byte) 1, (byte) 10, (byte) 8, (byte) 6, (byte) 0, (byte) 0, (byte) 0, (byte) 116, (byte) 97,
                (byte) 114, (byte) 103, (byte) 101, (byte) 116, (byte) 32, (byte) 0, (byte) 0, (byte) 0, (byte) 79,
                (byte) 45, (byte) 171, (byte) 75, (byte) 22, (byte) 4, (byte) 42, (byte) 147, (byte) 125, (byte) 141,
                (byte) 62, (byte) 29, (byte) 219, (byte) 28, (byte) 25, (byte) 6, (byte) 242, (byte) 133, (byte) 95,
                (byte) 152, (byte) 198, (byte) 69, (byte) 91, (byte) 217, (byte) 178, (byte) 254, (byte) 49, (byte) 128,
                (byte) 218, (byte) 84, (byte) 153, (byte) 250, (byte) 15, (byte) 32, (byte) 0, (byte) 0, (byte) 0,
                (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 105, (byte) 100, (byte) 9, (byte) 0, (byte) 0, (byte) 0,
                (byte) 1, (byte) 34, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 13,
                (byte) 5

        };

        final Transfer transfer = DeployUtil.newTransfer(10,
                new PublicKey(recipientPublicKey, KeyAlgorithm.ED25519),
                34);

        final byte[] actual = serializer.toBytes(transfer);

        assertThat(actual, is(expected));
    }
}