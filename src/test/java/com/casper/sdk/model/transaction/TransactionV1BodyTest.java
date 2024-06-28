package com.casper.sdk.model.transaction;

import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.entrypoint.WithdrawBidEntryPoint;
import com.casper.sdk.model.transaction.scheduling.FutureEra;
import com.casper.sdk.model.transaction.target.Native;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.SerializerBuffer;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author ian@meywood.com
 */
class TransactionV1BodyTest {

    @Test
    void transactionV1BodyToBytes() throws Exception {

        byte[] expectedBytes = {0x2, 0x0, 0x0, 0x0, 0xa, 0x0, 0x0, 0x0, 0x70, 0x75, 0x62, 0x6c, 0x69, 0x63, 0x5f, 0x6b,
                0x65, 0x79, 0x21, 0x0, 0x0, 0x0, 0x1, 0x16, 0x32, (byte) 0x88, 0x1a, 0xe, (byte) 0xe0, 0xa, (byte) 0xcf,
                0x28, 0x28, 0xc, (byte) 0xb2, 0x6, 0x39, 0x3e, (byte) 0x96, 0x3e, (byte) 0xae, 0x23, 0x43, (byte) 0xf9,
                0x46, (byte) 0x9a, (byte) 0xf7, (byte) 0x8a, (byte) 0xb1, 0x6f, 0x52, (byte) 0xfb, (byte) 0xae, 0x9,
                (byte) 0x87, 0x16, 0x6, 0x0, 0x0, 0x0, 0x61, 0x6d, 0x6f, 0x75, 0x6e, 0x74, 0x9, 0x0, 0x0, 0x0, 0x8,
                (byte) 0x97, (byte) 0xd1, (byte) 0xcf, 0x18, 0x75, (byte) 0xe1, (byte) 0xa2, 0x39, 0x8, 0x0, 0x3, 0x1,
                0x1, (byte) 0xc0, (byte) 0x86, 0x1, 0x0, 0x0, 0x0, 0x0, 0x0
        };

        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("public_key", new CLValuePublicKey(PublicKey.fromTaggedHexString("011632881a0ee00acf28280cb206393e963eae2343f9469af78ab16f52fbae0987"))),
                new NamedArg<>("amount", new CLValueU512(new BigInteger(Hex.decode("0897d1cf1875e1a239"))))
        );

        final TransactionV1Body transactionV1Body = TransactionV1Body.builder()
                .args(args)
                .target(new Native())
                .entryPoint(new WithdrawBidEntryPoint())
                .transactionCategory(TransactionCategory.STANDARD)
                .scheduling(new FutureEra(new BigInteger("100032")))
                .build();

        final SerializerBuffer buf = new SerializerBuffer();
        transactionV1Body.serialize(buf, Target.BYTE);
        final byte[] actual = buf.toByteArray();

        String hexActual = Hex.encode(actual);
        String hexExpected = Hex.encode(expectedBytes);assertThat(hexActual, is(hexExpected));

    }
}

