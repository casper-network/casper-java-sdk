package com.casper.sdk.domain;

import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class HashDeployTest {

    /* Sender key
    {
        publicKey: {
            rawPublicKey: {
                "0": 35,
                        "1": 247,
                        "2": 94,
                        "3": 204,
                        "4": 88,
                        "5": 21,
                        "6": 150,
                        "7": 123,
                        "8": 72,
                        "9": 88,
                        "10": 190,
                        "11": 2,
                        "12": 182,
                        "13": 124,
                        "14": 201,
                        "15": 81,
                        "16": 127,
                        "17": 236,
                        "18": 10,
                        "19": 198,
                        "20": 99,
                        "21": 65,
                        "22": 246,
                        "23": 29,
                        "24": 27,
                        "25": 110,
                        "26": 61,
                        "27": 226,
                        "28": 22,
                        "29": 157,
                        "30": 249,
                        "31": 211,
            },
            tag: 1,
        },
        privateKey: {
            "0": 178,
                    "1": 110,
                    "2": 216,
                    "3": 127,
                    "4": 58,
                    "5": 22,
                    "6": 65,
                    "7": 0,
                    "8": 148,
                    "9": 23,
                    "10": 158,
                    "11": 18,
                    "12": 96,
                    "13": 211,
                    "14": 143,
                    "15": 88,
                    "16": 211,
                    "17": 41,
                    "18": 195,
                    "19": 14,
                    "20": 145,
                    "21": 251,
                    "22": 123,
                    "23": 24,
                    "24": 107,
                    "25": 82,
                    "26": 163,
                    "27": 1,
                    "28": 97,
                    "29": 107,
                    "30": 115,
                    "31": 74,
                    "32": 35,
                    "33": 247,
                    "34": 94,
                    "35": 204,
                    "36": 88,
                    "37": 21,
                    "38": 150,
                    "39": 123,
                    "40": 72,
                    "41": 88,
                    "42": 190,
                    "43": 2,
                    "44": 182,
                    "45": 124,
                    "46": 201,
                    "47": 81,
                    "48": 127,
                    "49": 236,
                    "50": 10,
                    "51": 198,
                    "52": 99,
                    "53": 65,
                    "54": 246,
                    "55": 29,
                    "56": 27,
                    "57": 110,
                    "58": 61,
                    "59": 226,
                    "60": 22,
                    "61": 157,
                    "62": 249,
                    "63": 211,
        },
        signatureAlgorithm: "ed25519",
    }

     */



    /*

    // key tag == 1

    // signautre algorith "ed25519"

    private key
    {
  "0": 178,
  "1": 143,
  "2": 96,
  "3": 37,
  "4": 116,
  "5": 47,
  "6": 21,
  "7": 88,
  "8": 150,
  "9": 140,
  "10": 253,
  "11": 215,
  "12": 146,
  "13": 175,
  "14": 110,
  "15": 168,
  "16": 21,
  "17": 30,
  "18": 202,
  "19": 139,
  "20": 55,
  "21": 128,
  "22": 114,
  "23": 167,
  "24": 60,
  "25": 120,
  "26": 250,
  "27": 245,
  "28": 153,
  "29": 242,
  "30": 133,
  "31": 7,
  "32": 111,
  "33": 186,
  "34": 42,
  "35": 77,
  "36": 30,
  "37": 239,
  "38": 110,
  "39": 27,
  "40": 39,
  "41": 165,
  "42": 244,
  "43": 207,
  "44": 150,
  "45": 65,
  "46": 53,
  "47": 244,
  "48": 74,
  "49": 162,
  "50": 90,
  "51": 82,
  "52": 88,
  "53": 94,
  "54": 26,
  "55": 87,
  "56": 93,
  "57": 183,
  "58": 225,
  "59": 254,
  "60": 11,
  "61": 165,
  "62": 109,
  "63": 47,
}

     */


    @Test
    void testBodyHash() {

        byte[] publicKey = {(byte) 111, (byte) 186, (byte) 42, (byte) 77, (byte) 30, (byte) 239, (byte) 110,
                (byte) 27, (byte) 39, (byte) 165, (byte) 244, (byte) 207, (byte) 150, (byte) 65, (byte) 53, (byte) 244,
                (byte) 74, (byte) 162, (byte) 90, (byte) 82, (byte) 88, (byte) 94, (byte) 26, (byte) 87, (byte) 93,
                (byte) 183, (byte) 225, (byte) 254, (byte) 11, (byte) 165, (byte) 109, (byte) 47};


        final ModuleBytes payment = DeployUtil.standardPayment(new BigInteger("10000000000000"));
        final Transfer transfer = DeployUtil.makeTransfer(10, new PublicKey(publicKey), 34);

        final byte [] serializedBody = DeployUtil.serializeBody(payment, transfer);

        final byte [] expectedBody = ByteUtils.decodeHex(
                "000000000001" + // Module byte tag
                        "00000006000000616d6f756e74070000000600a0724e180908" + // Payment
                        "" +
                        "05" + // Transfer tag
                        "03000000" + // Number of args
                        "06000000" + // Length of 'amount'
                        "616d6f756e74" + // 'amount'
                        "02000000" + // Length of bytes for 10 as
                        "010a" + // U512 value of 0x0a
                        "08" + // 'target'
                        "060000007461726765742" + // OK to here
                        "0000000c42852808b6c19481ccc2ee1a3fec2116aa3bc5bb346049c740892f02a45d4590f20000000020000006964090000000122000000000000000d05");


        assertThat(serializedBody, is(expectedBody));

    }
}