package com.casper.sdk.domain;

import com.casper.sdk.service.HashService;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.NamedParameterSpec;
import java.time.Instant;
import java.util.Set;

import static com.casper.sdk.domain.DeployUtil.makeBodyHash;
import static com.casper.sdk.domain.DeployUtil.toTtlStr;
import static com.casper.sdk.service.serialization.util.ByteUtils.decodeHex;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests for the {@link DeployUtil} class
 */
class DeployUtilTest {

    public static final String DEPLOY_JSON_PATH = "/com/casper/sdk/domain/deploy-util-test.json";

    /**
     * Unit tests the makeTransfer method of the DeployUtil.
     */
    @Test
    void makeTransfer() {

        final byte[] keyBytes = {(byte) 175, (byte) 145, (byte) 149, (byte) 42, (byte) 56, (byte) 207, (byte) 204, (byte) 174, (byte) 155, (byte) 150, (byte) 79, (byte) 31, (byte) 56, (byte) 51, (byte) 120, (byte) 77, (byte) 247, (byte) 64, (byte) 199, (byte) 146, (byte) 86, (byte) 5, (byte) 90, (byte) 221, (byte) 38, (byte) 230, (byte) 182, (byte) 242, (byte) 177, (byte) 201, (byte) 241, (byte) 32};

        final byte[] expectedIdBytes = decodeHex("012200000000000000");
        // The expected target bytes after being hashed
        final byte[] expectedTargetBytes = decodeHex("e6454d6bc07d32a178298286e589029b083da8cd718ab3d8dbdab1cfd018fb79");
        final byte[] expectedAmountBytes = decodeHex("010A");

        final Transfer transfer = DeployUtil.newTransfer(10, new PublicKey(keyBytes, KeyAlgorithm.ED25519), 34);

        assertThat(transfer, is(notNullValue()));
        assertThat(transfer.getTag(), is(5));

        final DeployNamedArg amount = transfer.getNamedArg("amount");
        assertThat(amount.getValue().getCLType(), is(CLType.U512));
        assertThat(amount.getValue().getBytes(), is(expectedAmountBytes));
        assertThat(amount.getValue().getParsed(), is("10"));

        final DeployNamedArg id = transfer.getNamedArg("id");
        assertThat(id.getValue().getCLType(), is(CLType.OPTION));
        assertThat(id.getValue().getBytes(), is(expectedIdBytes));
        assertThat(id.getValue().getParsed(), is("34"));

        final DeployNamedArg target = transfer.getNamedArg("target");
        assertThat(target.getValue().getCLType(), is(CLType.BYTE_ARRAY));
        assertThat(((CLByteArrayInfo) target.getValue().getCLTypeInfo()).getSize(), is(32));
        assertThat(target.getValue().getBytes(), is(expectedTargetBytes));
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

                DeployUtil.newTransfer(new BigInteger("24500000000"),
                        new PublicKey("010101010101010101010101010101010101010101010101010101010101010101", KeyAlgorithm.ED25519),
                        new BigInteger("999")),

                DeployUtil.standardPayment(new BigInteger("1000000000"))
        );

        assertThat(deploy, is(notNullValue()));
    }

    @Test
    void serializeBody() {

        final byte[] recipientPublicKey = {(byte) 108, (byte) 114, (byte) 213, (byte) 223, (byte) 102, (byte) 130,
                (byte) 189, (byte) 133, (byte) 175, (byte) 102, (byte) 195, (byte) 84, (byte) 171, (byte) 94, (byte) 88,
                (byte) 117, (byte) 242, (byte) 142, (byte) 146, (byte) 54, (byte) 0, (byte) 20, (byte) 129, (byte) 210,
                (byte) 128, (byte) 211, (byte) 127, (byte) 27, (byte) 63, (byte) 229, (byte) 50, (byte) 192};

        final byte[] expectedBody = {(byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0,
                (byte) 0, (byte) 6, (byte) 0, (byte) 0, (byte) 0, (byte) 97, (byte) 109, (byte) 111, (byte) 117,
                (byte) 110, (byte) 116, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 6, (byte) 0, (byte) 160,
                (byte) 114, (byte) 78, (byte) 24, (byte) 9, (byte) 8, (byte) 5, (byte) 3, (byte) 0, (byte) 0, (byte) 0,
                (byte) 6, (byte) 0, (byte) 0, (byte) 0, (byte) 97, (byte) 109, (byte) 111, (byte) 117, (byte) 110,
                (byte) 116, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 10, (byte) 8, (byte) 6, (byte) 0,
                (byte) 0, (byte) 0, (byte) 116, (byte) 97, (byte) 114, (byte) 103, (byte) 101, (byte) 116, (byte) 32,
                (byte) 0, (byte) 0, (byte) 0, (byte) 71, (byte) 53, (byte) 53, (byte) 86, (byte) 95, (byte) 247,
                (byte) 180, (byte) 253, (byte) 215, (byte) 170, (byte) 63, (byte) 122, (byte) 8, (byte) 60, (byte) 127,
                (byte) 157, (byte) 5, (byte) 184, (byte) 47, (byte) 203, (byte) 165, (byte) 124, (byte) 34, (byte) 51,
                (byte) 159, (byte) 191, (byte) 80, (byte) 227, (byte) 197, (byte) 71, (byte) 77, (byte) 203, (byte) 15,
                (byte) 32, (byte) 0, (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 105, (byte) 100,
                (byte) 9, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 34, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 13, (byte) 5
        };

        final ModuleBytes payment = DeployUtil.standardPayment(10000000000000L);
        final Transfer transfer = DeployUtil.newTransfer(10, new PublicKey(recipientPublicKey, KeyAlgorithm.ED25519), 34);

        final byte[] bytes = DeployUtil.serializeBody(payment, transfer);
        assertThat(bytes, is(expectedBody));
    }

    @Test
    void testBlakeBodyHash() {

        final byte[] serialisedBody = new byte[]{
                0, 0, 0, 0, 0, 1, 0, 0, 0, 6, 0, 0, 0, 97, 109, 111, 117, 110, 116, 7, 0, 0, 0, 6, 0, (byte) 160, 114, 78, 24, 9, 8, 5, 3, 0, 0, 0, 6, 0, 0, 0, 97, 109,
                111, 117, 110, 116, 2, 0, 0, 0, 1, 10, 8, 6, 0, 0, 0, 116, 97, 114, 103, 101, 116, 32, 0, 0, 0, 104, 72, 61, (byte) 232, 75, 88, (byte)
                152, (byte) 144, (byte) 245, 116, 27, (byte) 164, (byte) 179, 74, 119, 56, 71, 108, (byte) 218, 70, 56, 91, 58, (byte) 209, 28, (byte) 197, 32,
                76, (byte) 241, 92, (byte) 163, (byte) 143, 15, 32, 0, 0, 0, 2, 0,
                0, 0, 105, 100, 9, 0, 0, 0, 1, 34, 0, 0, 0, 0, 0, 0, 0, 13, 5
        };

        final byte[] expected = new byte[]{
                0, 58, (byte) 203, (byte) 148, 42, 0, (byte) 131, 111, (byte) 223, 18, 26, 72, 59, (byte) 253, (byte) 158,
                (byte) 137, 83, 58, (byte) 211, 62, 63, 47, 87, 49, 6, 74, (byte) 232, (byte) 177, 101, (byte) 177, (byte) 155, (byte) 173
        };

        final byte[] result = HashService.getInstance().getHash((serialisedBody));

        assertThat(result, is(expected));
    }

    @Test
    void serializeApprovals() {

        final Set<DeployApproval> approvals = Set.of(
                new DeployApproval(
                        new PublicKey("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"),
                        new Signature("0195a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06")
                )
        );

        final byte[] expected = {1, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 127, (byte) 116, (byte) 123,
                (byte) 103, (byte) 189, (byte) 63, (byte) 230, (byte) 60, (byte) 42, (byte) 115, (byte) 103,
                (byte) 57, (byte) 223, (byte) 228, (byte) 1, (byte) 86, (byte) 214, (byte) 34, (byte) 52,
                (byte) 115, (byte) 70, (byte) 231, (byte) 15, (byte) 104, (byte) 245, (byte) 28, (byte) 23,
                (byte) 138, (byte) 117, (byte) 206, (byte) 85, (byte) 55, (byte) 1, (byte) 149, (byte) 166,
                (byte) 139, (byte) 26, (byte) 5, (byte) 115, (byte) 27, (byte) 112, (byte) 20, (byte) 229,
                (byte) 128, (byte) 180, (byte) 198, (byte) 122, (byte) 80, (byte) 110, (byte) 3, (byte) 57,
                (byte) 167, (byte) 255, (byte) 254, (byte) 173, (byte) 237, (byte) 159, (byte) 36, (byte) 235,
                (byte) 46, (byte) 127, (byte) 120, (byte) 185, (byte) 107, (byte) 221, (byte) 144, (byte) 11,
                (byte) 155, (byte) 232, (byte) 202, (byte) 51, (byte) 228, (byte) 85, (byte) 42, (byte) 154,
                (byte) 97, (byte) 157, (byte) 196, (byte) 252, (byte) 94, (byte) 78, (byte) 58, (byte) 159,
                (byte) 116, (byte) 164, (byte) 176, (byte) 83, (byte) 124, (byte) 20, (byte) 165, (byte) 168,
                (byte) 0, (byte) 125, (byte) 98, (byte) 165, (byte) 220, (byte) 6};

        assertThat(expected.length, is(102));

        final byte[] bytes = DeployUtil.serializeApprovals(approvals);
        assertThat(bytes, is(expected));
    }

    @Test
    void deployBodyHash() {

        byte[] recipientPublicKey = {
                (byte) 235, (byte) 159, (byte) 193, (byte) 214, (byte) 147, (byte) 156, (byte) 230, (byte) 108, (byte) 231, (byte) 154, (byte) 91, (byte) 254, (byte) 196, (byte) 74, (byte) 129, (byte) 111, (byte) 56, (byte) 4, (byte) 210, (byte) 93, (byte) 83, (byte) 131, (byte) 105, (byte) 100, (byte) 246, (byte) 156, (byte) 136, (byte) 124, (byte) 42, (byte) 221, (byte) 33, (byte) 170,
        };

        // Serialized body
        byte[] expectedBody = {
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 6, (byte) 0, (byte) 0, (byte) 0, (byte) 97, (byte) 109, (byte) 111, (byte) 117, (byte) 110, (byte) 116, (byte) 7, (byte) 0, (byte) 0, (byte) 0, (byte) 6, (byte) 0, (byte) 160, (byte) 114, (byte) 78, (byte) 24, (byte) 9, (byte) 8, (byte) 5, (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 6, (byte) 0, (byte) 0, (byte) 0, (byte) 97, (byte) 109, (byte) 111, (byte) 117, (byte) 110, (byte) 116, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 10, (byte) 8, (byte) 6, (byte) 0, (byte) 0, (byte) 0, (byte) 116, (byte) 97, (byte) 114, (byte) 103, (byte) 101, (byte) 116, (byte) 32, (byte) 0, (byte) 0, (byte) 0, (byte) 99, (byte) 41, (byte) 251, (byte) 253, (byte) 212, (byte) 118, (byte) 159, (byte) 210, (byte) 7, (byte) 128, (byte) 63, (byte) 248, (byte) 225, (byte) 69, (byte) 147, (byte) 87, (byte) 252, (byte) 128, (byte) 237, (byte) 14, (byte) 65, (byte) 213, (byte) 17, (byte) 201, (byte) 35, (byte) 199, (byte) 13, (byte) 119, (byte) 245, (byte) 231, (byte) 99, (byte) 188, (byte) 15, (byte) 32, (byte) 0, (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 0, (byte) 0, (byte) 105, (byte) 100, (byte) 9, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 34, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 13, (byte) 5,
        };

        byte[] expectedHash = {
                (byte) 135, (byte) 253, (byte) 178, (byte) 236, (byte) 71, (byte) 44, (byte) 78, (byte) 151, (byte) 136, (byte) 23, (byte) 159, (byte) 163, (byte) 28, (byte) 2, (byte) 90, (byte) 219, (byte) 44, (byte) 59, (byte) 218, (byte) 234, (byte) 197, (byte) 121, (byte) 219, (byte) 106, (byte) 118, (byte) 51, (byte) 252, (byte) 67, (byte) 229, (byte) 37, (byte) 160, (byte) 30
        };

        final ModuleBytes payment = DeployUtil.standardPayment(10000000000000L);
        final Transfer transfer = DeployUtil.newTransfer(10, new PublicKey(recipientPublicKey, KeyAlgorithm.ED25519), 34);

        byte[] body = DeployUtil.serializeBody(payment, transfer);

        assertThat(body.length, is(expectedBody.length));
        assertThat(body, is(expectedBody));

        final Digest bodyHash = makeBodyHash(payment, transfer);
        assertThat(bodyHash.getHash(), is(expectedHash));
    }

    @Test
    void deployToBytes() throws IOException {

        final InputStream in = getClass().getResource(DEPLOY_JSON_PATH).openStream();
        final Deploy deploy = DeployUtil.fromJson(in);

        final String strExpected = "017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537a087c0377901000040771b00000000000200000000000000f2e0782bba4a0a9663cafc7d707fd4a74421bc5bfef4e368b7e8f38dfab87db8020000000f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f1010101010101010101010101010101010101010101010101010101010101010070000006d61696e6e6574d7a68bbe656a883d04bba9f26aa340dbe3f8ec99b2adb63b628f2bc92043199800000000000100000006000000616d6f756e74050000000400ca9a3b08050400000006000000616d6f756e740600000005005550b40508060000007461726765742000000001010101010101010101010101010101010101010101010101010101010101010f200000000200000069640900000001e7030000000000000d050f0000006164646974696f6e616c5f696e666f140000001000000074686973206973207472616e736665720a01000000017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce55370195a68b1a05731b7014e580b4c67a506e0339a7fffeaded9f24eb2e7f78b96bdd900b9be8ca33e4552a9a619dc4fc5e4e3a9f74a4b0537c14a5a8007d62a5dc06";
        byte[] expected = decodeHex(strExpected);

        final byte[] actual = DeployUtil.toBytes(deploy);

        assertThat(actual, is(expected));
    }

    @Test
    void signDeploy() throws NoSuchAlgorithmException {

        final byte[] privateKeyBytes = {
                (byte) 239, (byte) 239, (byte) 126, (byte) 80, (byte) 139, (byte) 163, (byte) 76, (byte) 205, (byte) 21,
                (byte) 144, (byte) 177, (byte) 126, (byte) 19, (byte) 67, (byte) 59, (byte) 27, (byte) 139, (byte) 186,
                (byte) 172, (byte) 191, (byte) 195, (byte) 81, (byte) 196, (byte) 111, (byte) 213, (byte) 199,
                (byte) 93, (byte) 37, (byte) 50, (byte) 228, (byte) 225, (byte) 199, (byte) 22, (byte) 117, (byte) 181,
                (byte) 87, (byte) 53, (byte) 175, (byte) 221, (byte) 34, (byte) 18, (byte) 147, (byte) 236, (byte) 64,
                (byte) 57, (byte) 231, (byte) 187, (byte) 253, (byte) 206, (byte) 131, (byte) 79, (byte) 31, (byte) 131,
                (byte) 134, (byte) 220, (byte) 43, (byte) 235, (byte) 197, (byte) 108, (byte) 77, (byte) 35, (byte) 110,
                (byte) 71, (byte) 76
        };


        final byte[] publicKeyBytes = {
                38, (byte) 196, (byte) 123, (byte) 104, (byte) 234, (byte) 59, (byte) 216, (byte) 126, (byte) 248,
                (byte) 48, (byte) 36, (byte) 109, (byte) 206, (byte) 174, (byte) 56, (byte) 122, (byte) 168, (byte) 72,
                (byte) 9, (byte) 205, (byte) 92, (byte) 185, (byte) 153, (byte) 244, (byte) 14, (byte) 34, (byte) 153,
                (byte) 237, (byte) 11, (byte) 178, (byte) 97, (byte) 27
        };


        final Deploy deploy = DeployUtil.makeDeploy(

                new DeployParams(
                        new PublicKey("017f747b67bd3fe63c2a736739dfe40156d622347346e70f68f51c178a75ce5537"),
                        "mainnet",
                        1,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null),

                DeployUtil.newTransfer(new BigInteger("24500000000"),
                        new PublicKey("0101010101010101010101010101010101010101010101010101010101010101", KeyAlgorithm.ED25519),
                        new BigInteger("999")),

                DeployUtil.standardPayment(new BigInteger("1000000000"))
        );

        assertThat(deploy.getApprovals().size(), is(0));

        final KeyPair keyPair = KeyPairGenerator.getInstance(NamedParameterSpec.ED25519.getName()).generateKeyPair();

        final Deploy signedDeploy = DeployUtil.signDeploy(deploy, keyPair);

        assertThat(signedDeploy.getApprovals().size(), is(1));

        final DeployApproval approval = signedDeploy.getApprovals().iterator().next();
        assertThat(approval.getSignature().toAccount(), is(notNullValue()));
        assertThat(approval.getSigner().toAccount(), is(notNullValue()));
    }

    @Test
    void serializedHeader() {

        final byte[] recipientPublicKey = {
                (byte) 38, (byte) 196, (byte) 123, (byte) 104, (byte) 234, (byte) 59, (byte) 216, (byte) 126,
                (byte) 248, (byte) 48, (byte) 36, (byte) 109, (byte) 206, (byte) 174, (byte) 56, (byte) 122, (byte) 168,
                (byte) 72, (byte) 9, (byte) 205, (byte) 92, (byte) 185, (byte) 153, (byte) 244, (byte) 14, (byte) 34,
                (byte) 153, (byte) 237, (byte) 11, (byte) 178, (byte) 97, (byte) 27
        };

        final byte[] senderPublicKey = {
                (byte) 207, (byte) 86, (byte) 252, (byte) 149, (byte) 20, (byte) 26, (byte) 76, (byte) 239, (byte) 118,
                (byte) 242, (byte) 95, (byte) 25, (byte) 119, (byte) 198, (byte) 33, (byte) 97, (byte) 83, (byte) 247,
                (byte) 251, (byte) 46, (byte) 43, (byte) 45, (byte) 237, (byte) 249, (byte) 117, (byte) 149, (byte) 84,
                (byte) 212, (byte) 142, (byte) 223, (byte) 74, (byte) 248
        };

        final byte[] expectedHeaderBytes = {
                (byte) 1, (byte) 207, (byte) 86, (byte) 252, (byte) 149, (byte) 20, (byte) 26, (byte) 76, (byte) 239,
                (byte) 118, (byte) 242, (byte) 95, (byte) 25, (byte) 119, (byte) 198, (byte) 33, (byte) 97, (byte) 83,
                (byte) 247, (byte) 251, (byte) 46, (byte) 43, (byte) 45, (byte) 237, (byte) 249, (byte) 117, (byte) 149,
                (byte) 84, (byte) 212, (byte) 142, (byte) 223, (byte) 74, (byte) 248, (byte) 247, (byte) 57, (byte) 245,
                (byte) 47, (byte) 122, (byte) 1, (byte) 0, (byte) 0, (byte) 64, (byte) 119, (byte) 27, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 125, (byte) 14, (byte) 3, (byte) 25, (byte) 160, (byte) 12, (byte) 27,
                (byte) 147, (byte) 69, (byte) 246, (byte) 217, (byte) 172, (byte) 102, (byte) 79, (byte) 201, (byte) 49,
                (byte) 237, (byte) 234, (byte) 170, (byte) 3, (byte) 213, (byte) 239, (byte) 216, (byte) 13, (byte) 241,
                (byte) 44, (byte) 234, (byte) 129, (byte) 112, (byte) 219, (byte) 177, (byte) 28, (byte) 0, (byte) 0,
                (byte) 0, (byte) 0, (byte) 12, (byte) 0, (byte) 0, (byte) 0, (byte) 116, (byte) 101, (byte) 115,
                (byte) 116, (byte) 45, (byte) 110, (byte) 101, (byte) 116, (byte) 119, (byte) 111, (byte) 114,
                (byte) 107
        };

        final DeployParams deployParams = new DeployParams(
                new PublicKey(senderPublicKey, KeyAlgorithm.ED25519),
                "test-network",
                null,
                1624302238199L,
                null,
                null
        );

        final ModuleBytes payment = DeployUtil.standardPayment(10000000000000L);
        final Transfer session = DeployUtil.newTransfer(10, new PublicKey(recipientPublicKey, KeyAlgorithm.ED25519), 34);
        final Digest bodyHash = makeBodyHash(payment, session);
        final DeployHeader header = new DeployHeader(
                deployParams.getAccountPublicKey(),
                deployParams.getTimestamp(),
                toTtlStr(deployParams.getTtl()),
                deployParams.getGasPrice(),
                bodyHash,
                deployParams.getDependencies(),
                deployParams.getChainName()
        );
        final byte[] serializedHeader = DeployUtil.serializedHeader(header);

        assertThat(serializedHeader, is(expectedHeaderBytes));
    }

    @Test
    void testDeploySize() throws IOException {

        final InputStream in = getClass().getResource(DEPLOY_JSON_PATH).openStream();
        final Deploy deploy = DeployUtil.fromJson(in);
        int sizeInBytes = DeployUtil.deploySizeInBytes(deploy);
        assertThat(sizeInBytes, is(473));
    }
}