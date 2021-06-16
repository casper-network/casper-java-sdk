package com.casper.sdk.domain;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.time.Instant;

import static com.casper.sdk.service.serialization.util.ByteUtils.concat;
import static com.casper.sdk.service.serialization.util.ByteUtils.decodeHex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests for the {@link DeployUtil} class
 */
class DeployUtilTest {

    public static final String DEPLOY_JSON_PATH = "/com/casper/sdk/domain/deploy-util-test.json";
    public static final String DEPLOY_TS_JSON_PATH = "/com/casper/sdk/domain/deploy-from-ts-client.json";

    /**
     * Unit tests the makeTransfer method of the DeployUtil.
     */
    @Test
    void makeTransfer() {

        final byte[] expectedIdBytes = decodeHex("01e703000000000000");
        final byte[] expectedTargetBytes = decodeHex("0101010101010101010101010101010101010101010101010101010101010101");
        final byte[] expectedAmountBytes = decodeHex("05005550b405");


        final Transfer transfer = DeployUtil.makeTransfer(new BigInteger("24500000000"),
                new PublicKey("0101010101010101010101010101010101010101010101010101010101010101"),
                new BigInteger("999"));

        assertThat(transfer, is(notNullValue()));
        assertThat(transfer.getTag(), is(5));

        final DeployNamedArg amount = transfer.getNamedArg("amount");
        assertThat(amount.getValue().getCLType(), is(CLType.U512));
        assertThat(amount.getValue().getBytes(), is(expectedAmountBytes));
        assertThat(amount.getValue().getParsed(), is("24500000000"));

        final DeployNamedArg id = transfer.getNamedArg("id");
        assertThat(id.getValue().getCLType(), is(CLType.OPTION));
        assertThat(id.getValue().getBytes(), is(expectedIdBytes));
        assertThat(id.getValue().getParsed(), is("999"));

        final DeployNamedArg target = transfer.getNamedArg("target");
        assertThat(target.getValue().getCLType(), is(CLType.BYTE_ARRAY));
        assertThat(((CLByteArrayInfo) target.getValue().getCLTypeInfo()).getSize(), is(32));
        assertThat(target.getValue().getBytes(), is(expectedTargetBytes));
        assertThat(target.getValue().getParsed(), is("0101010101010101010101010101010101010101010101010101010101010101"));
    }

    @Test
    void standardPayment() {

        final ModuleBytes payment = DeployUtil.standardPayment(1000000L);
        assertThat(payment.getTag(), is(0));

        final CLValue amount = payment.getNamedArg("amount").getValue();
        assertThat(amount.getCLType(), is(CLType.U512));
        assertThat(amount.getParsed(), is(1000000L));
        assertThat(amount.getBytes(), is(decodeHex("0340420F")));
    }

    @Test
    void standardPaymentToBytes() {

        byte[] expectedBytes = decodeHex("00000000000100000006000000616d6f756e74040000000340420f08");
        final ModuleBytes payment = DeployUtil.standardPayment(1000000L);

        assertThat(DeployUtil.toBytes(payment), is(expectedBytes));
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
    void testSerializeBody() throws IOException {

        final InputStream in = getClass().getResource(DEPLOY_TS_JSON_PATH).openStream();
        final Deploy deploy = DeployUtil.fromJson(in);

        // Expected payment bytes
        final byte[] paymentBytes = new byte[]{
                0,
                0,
                0,
                0,
                0,
                1,
                0,
                0,
                0,
                6,
                0,
                0,
                0,
                97,
                109,
                111,
                117,
                110,
                116,
                6,
                0,
                0,
                0,
                5,
                0,
                (byte) 228,
                11,
                84,
                2,
                8
        };

        final byte[] session = new byte[]{
                5, 3, 0, 0, 0, 6, 0, 0, 0, 97, 109, 111, 117, 110, 116, 6, 0, 0, 0, 5, 0, 116, 59, (byte) 164, 11, 8, 6,
                0, 0, 0, 116, 97, 114, 103, 101, 116, 32, 0, 0, 0, 21, 65, 86, 107, (byte) 218, (byte) 211, (byte) 163,
                (byte) 207, (byte) 169, (byte) 235, 76, (byte) 186, 61, (byte) 207, 51, (byte) 238, 101, (byte) 131,
                (byte) 224, 115, 58, (byte) 228, (byte) 178, (byte) 204, (byte) 223, (byte) 233, (byte) 44, (byte) 209,
                (byte) 189, (byte) 146, (byte) 238, 22, 15, 32, 0, 0, 0, 2, 0, 0, 0, 105, 100, 9, 0, 0, 0, 1, (byte) 160,
                (byte) 134, 1, 0, 0, 0, 0, 0, 13, 5
        };

        final byte[] bytes = DeployUtil.serializeBody(deploy.getPayment(), deploy.getSession());

        final byte[] expected = concat(
                paymentBytes,
                session
        );

        assertThat(bytes, is(expected));
    }


    @Test
    void testDeployBodyHash() throws IOException {


        final InputStream in = getClass().getResource(DEPLOY_JSON_PATH).openStream();
        final Deploy deploy = DeployUtil.fromJson(in);
        final Digest expected = deploy.getHeader().getBodyHash();

        final Digest bodyHash = DeployUtil.makeBodyHash(deploy.getPayment(), deploy.getSession());


        assertThat(bodyHash.getHash(), is(expected.getHash()));
    }

    @Test
    void testDeployToBytes() throws IOException {

        final InputStream in = getClass().getResource(DEPLOY_JSON_PATH).openStream();
        final Deploy deploy = DeployUtil.fromJson(in);

        final String strExpected = "017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537a087c0377901000040771b00000000000200000000000000f2e0782bba4a0a9663cafc7d707fd4a74421bc5bfef4e368b7e8f38dfab87db8020000000f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f1010101010101010101010101010101010101010101010101010101010101010070000006d61696e6e6574d7a68bbe656a883d04bba9f26aa340dbe3f8ec99b2adb63b628f2bc92043199800000000000100000006000000616d6f756e74050000000400ca9a3b08050400000006000000616d6f756e740600000005005550b40508060000007461726765742000000001010101010101010101010101010101010101010101010101010101010101010f200000000200000069640900000001e7030000000000000d050f0000006164646974696f6e616c5f696e666f140000001000000074686973206973207472616e736665720a01000000017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce55370195a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06";
        byte[] expected = decodeHex(strExpected);

        final byte[] actual = DeployUtil.toBytes(deploy);

        // TODO complete byte serialization
        //  assertThat(actual, is(expected));
    }
}