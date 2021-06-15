package com.casper.sdk.domain;

import com.casper.sdk.service.serialization.util.ByteUtils;
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

        final byte[] expectedIdBytes = ByteUtils.decodeHex("01e703000000000000");
        final byte[] expectedTargetBytes = ByteUtils.decodeHex("0101010101010101010101010101010101010101010101010101010101010101");
        final byte[] expectedAmountBytes = ByteUtils.decodeHex("05005550b405");


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

        final StoredContractByName payment = DeployUtil.standardPayment(1000000L);
        assertThat(payment.getTag(), is(2));
        assertThat(payment.getName(), is("payment"));

        final CLValue amount = payment.getNamedArg("amount").getValue();
        assertThat(amount.getCLType(), is(CLType.U512));
        assertThat(amount.getParsed(), is(1000000L));
        assertThat(amount.getBytes(), is(decodeHex("0F4240")));
    }

    @Test
    void standardPaymentToBytes() {

        final String expectedHex = "02070000007061796d656e74000000000100000006000000616d6f756e74080f4240";
        byte[] expectedBytes = ByteUtils.decodeHex(expectedHex);

        final StoredContractByName payment = DeployUtil.standardPayment(1000000L);
        byte[] bytes = DeployUtil.toBytes(payment);

        // TODO calculate what this should really be
        assertThat(bytes, is(expectedBytes));
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
                8,
        };

        final byte[] session = new byte[]{
                5, 3, 0, 0, 0, 6, 0, 0, 0, 97, 109, 111, 117, 110, 116, 6, 0, 0, 0, 5, 0, 116, 59, (byte) 164, 11, 8, 6,
                0, 0, 0, 116, 97, 114, 103, 101, 116, 32, 0, 0, 0, 21, 65, 86, 107, (byte) 218, (byte) 211, (byte) 163,
                (byte) 207, (byte) 169, (byte) 235, 76, (byte) 186, 61, (byte) 207, 51, (byte) 238, 101, (byte) 131,
                (byte) 224, 115, 58, (byte) 228, (byte) 178, (byte) 204, (byte) 223, (byte) 233, (byte) 44, (byte) 209,
                (byte)189, (byte)146, (byte)238, 22, 15, 32, 0, 0, 0, 2, 0, 0, 0, 105, 100, 9, 0, 0, 0, 1, (byte)160,
                (byte)134, 1, 0, 0, 0, 0, 0, 13, 5
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

        // {
        //  "0": 0,
        //  "1": 0,
        //  "2": 0,
        //  "3": 0,
        //  "4": 0,
        //  "5": 1,
        //  "6": 0,
        //  "7": 0,
        //  "8": 0,
        //  "9": 6,
        //  "10": 0,
        //  "11": 0,
        //  "12": 0,
        //  "13": 97,
        //  "14": 109,
        //  "15": 111,
        //  "16": 117,
        //  "17": 110,
        //  "18": 116,
        //  "19": 7,
        //  "20": 0,
        //  "21": 0,
        //  "22": 0,
        //  "23": 6,
        //  "24": 0,
        //  "25": 160,
        //  "26": 114,
        //  "27": 78,
        //  "28": 24,
        //  "29": 9,
        //  "30": 8,
        //  "31": 5,
        //  "32": 3,
        //  "33": 0,
        //  "34": 0,
        //  "35": 0,
        //  "36": 6,
        //  "37": 0,
        //  "38": 0,
        //  "39": 0,
        //  "40": 97,
        //  "41": 109,
        //  "42": 111,
        //  "43": 117,
        //  "44": 110,
        //  "45": 116,
        //  "46": 2,
        //  "47": 0,
        //  "48": 0,
        //  "49": 0,
        //  "50": 1,
        //  "51": 10,
        //  "52": 8,
        //  "53": 6,
        //  "54": 0,
        //  "55": 0,
        //  "56": 0,
        //  "57": 116,
        //  "58": 97,
        //  "59": 114,
        //  "60": 103,
        //  "61": 101,
        //  "62": 116,
        //  "63": 32,
        //  "64": 0,
        //  "65": 0,
        //  "66": 0,
        //  "67": 137,
        //  "68": 218,
        //  "69": 233,
        //  "70": 255,
        //  "71": 191,
        //  "72": 245,
        //  "73": 45,
        //  "74": 197,
        //  "75": 87,
        //  "76": 20,
        //  "77": 40,
        //  "78": 44,
        //  "79": 113,
        //  "80": 99,
        //  "81": 131,
        //  "82": 241,
        //  "83": 54,
        //  "84": 230,
        //  "85": 182,
        //  "86": 115,
        //  "87": 167,
        //  "88": 63,
        //  "89": 68,
        //  "90": 56,
        //  "91": 89,
        //  "92": 233,
        //  "93": 172,
        //  "94": 110,
        //  "95": 223,
        //  "96": 132,
        //  "97": 204,
        //  "98": 18,
        //  "99": 15,
        //  "100": 32,
        //  "101": 0,
        //  "102": 0,
        //  "103": 0,
        //  "104": 2,
        //  "105": 0,
        //  "106": 0,
        //  "107": 0,
        //  "108": 105,
        //  "109": 100,
        //  "110": 9,
        //  "111": 0,
        //  "112": 0,
        //  "113": 0,
        //  "114": 1,
        //  "115": 34,
        //  "116": 0,
        //  "117": 0,
        //  "118": 0,
        //  "119": 0,
        //  "120": 0,
        //  "121": 0,
        //  "122": 0,
        //  "123": 13,
        //  "124": 5,
        //}

        // Body Hash

        // {
        //  "0": 233,
        //  "1": 239,
        //  "2": 144,
        //  "3": 132,
        //  "4": 28,
        //  "5": 27,
        //  "6": 33,
        //  "7": 16,
        //  "8": 52,
        //  "9": 154,
        //  "10": 114,
        //  "11": 92,
        //  "12": 227,
        //  "13": 183,
        //  "14": 177,
        //  "15": 174,
        //  "16": 143,
        //  "17": 115,
        //  "18": 169,
        //  "19": 101,
        //  "20": 231,
        //  "21": 224,
        //  "22": 135,
        //  "23": 123,
        //  "24": 36,
        //  "25": 169,
        //  "26": 227,
        //  "27": 255,
        //  "28": 209,
        //  "29": 56,
        //  "30": 180,
        //  "31": 170,
        //}

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

        assertThat(actual, is(expected));
    }
}