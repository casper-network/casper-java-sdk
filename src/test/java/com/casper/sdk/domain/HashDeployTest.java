package com.casper.sdk.domain;

import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class HashDeployTest {

    /* HASHED SESSION
    {
        "0": 5,
            "1": 3,
            "2": 0,
            "3": 0,
            "4": 0,
            "5": 6,
            "6": 0,
            "7": 0,
            "8": 0,
            "9": 97,
            "10": 109,
            "11": 111,
            "12": 117,
            "13": 110,
            "14": 116,
            "15": 2,
            "16": 0,
            "17": 0,
            "18": 0,
            "19": 1,
            "20": 10,
            "21": 8,
            "22": 6,
            "23": 0,
            "24": 0,
            "25": 0,
            "26": 116,
            "27": 97,
            "28": 114,
            "29": 103,
            "30": 101,
            "31": 116,
            "32": 32,
            "33": 0,
            "34": 0,
            "35": 0,
            "36": 71,
            "37": 53,
            "38": 53,
            "39": 86,
            "40": 95,
            "41": 247,
            "42": 180,
            "43": 253,
            "44": 215,
            "45": 170,
            "46": 63,
            "47": 122,
            "48": 8,
            "49": 60,
            "50": 127,
            "51": 157,
            "52": 5,
            "53": 184,
            "54": 47,
            "55": 203,
            "56": 165,
            "57": 124,
            "58": 34,
            "59": 51,
            "60": 159,
            "61": 191,
            "62": 80,
            "63": 227,
            "64": 197,
            "65": 71,
            "66": 77,
            "67": 203,
            "68": 15,
            "69": 32,
            "70": 0,
            "71": 0,
            "72": 0,
            "73": 2,
            "74": 0,
            "75": 0,
            "76": 0,
            "77": 105,
            "78": 100,
            "79": 9,
            "80": 0,
            "81": 0,
            "82": 0,
            "83": 1,
            "84": 34,
            "85": 0,
            "86": 0,
            "87": 0,
            "88": 0,
            "89": 0,
            "90": 0,
            "91": 0,
            "92": 13,
            "93": 5,
    }

     */


    @Test
    void testBodyHash() {

        
        /*
        {
  ,
}
         */
        final String target = ByteUtils.encodeHexString(new byte[]{
                108, (byte) 114, (byte) 213, (byte) 223, (byte) 102, (byte) 130, (byte) 189, (byte) 133, (byte) 175,
                (byte) 102, (byte) 195, (byte) 84, (byte) 171, (byte) 94, (byte) 88, (byte) 117, (byte) 242,
                (byte) 142, (byte) 146, (byte) 54, (byte) 0, (byte) 20, (byte) 129, (byte) 210, (byte) 128,
                (byte) 211, (byte) 127, (byte) 27, (byte) 63, (byte) 229, (byte) 50, (byte) 192
        });

        final String hashedTarget = "473535565ff7b4fdd7aa3f7a083c7f9d05b82fcba57c22339fbf50e3c5474dcb";




        // ID  byte [] id = {'012200000000000000'}  // ==  34

        final ModuleBytes payment = DeployUtil.standardPayment(new BigInteger("10000000000000"));
        final Transfer transfer = DeployUtil.newTransfer(10, new PublicKey(target, KeyAlgorithm.ED25519), 34);

        assertThat(transfer.getNamedArg("target").getValue().getBytes(), is(ByteUtils.decodeHex(hashedTarget)));

        final byte[] serializedBody = DeployUtil.serializeBody(payment, transfer);

        final byte[] expectedBody = ByteUtils.decodeHex(
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