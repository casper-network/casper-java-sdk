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
                32,  // Missing 32? where does it come from
                0,
                0,
                0,
                2,   // Lenfth of ID ?
                0,
                0,
                0,
                105,  // 'i'
                100,  // 'd'
                9,
                0,
                0,
                0,
                1,
                (byte)  160,
                (byte) 134,
                1,
                0,
                0,
                0,
                0,
                0,
                13,
                5,
        };

        assertThat(expected.length, is(98));

        final Transfer transfer = DeployUtil.makeTransfer(new BigInteger("50000000000"),
                new PublicKey("1541566bdad3a3cfa9eb4cba3dcf33ee6583e0733ae4b2ccdfe92cd1bd92ee16"),
                100000L);

        final byte[] actual = serializer.toBytes(transfer);

        assertThat(actual, is(expected));


        // "1541566bdad3a3cfa9eb4cba3dcf33ee6583e0733ae4b2ccdfe92cd1bd92ee16"

        /* PublicKey.....
        {
            "0": 6,
                "1": 0,
                "2": 0,
                "3": 0,
                "4": 116,
                "5": 97,
                "6": 114,
                "7": 103,
                "8": 101,
                "9": 116,
                "10": 32,
                "11": 0,
                "12": 0,
                "13": 0,
                "14": 21,
                "15": 65,
                "16": 86,
                "17": 107,
                "18": 218,
                "19": 211,
                "20": 163,
                "21": 207,
                "22": 169,
                "23": 235,
                "24": 76,
                "25": 186,
                "26": 61,
                "27": 207,
                "28": 51,
                "29": 238,
                "30": 101,
                "31": 131,
                "32": 224,
                "33": 115,
                "34": 58,
                "35": 228,
                "36": 178,
                "37": 204,
                "38": 223,
                "39": 233,
                "40": 44,
                "41": 209,
                "42": 189,
                "43": 146,
                "44": 238,
                "45": 22,
                "46": 15,
                "47": 32,
                "48": 0,
                "49": 0,
                "50": 0,
        }
        */

/*
[
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
    "15": 6,
    "16": 0,
    "17": 0,
    "18": 0,
    "19": 5,
    "20": 0,
    "21": 116,
    "22": 59,
    "23": 164,
    "24": 11,
    "25": 8,
    "26": 6,
    "27": 0,
    "28": 0,
    "29": 0,
    "30": 116,
    "31": 97,
    "32": 114,
    "33": 103,
    "34": 101,
    "35": 116,
    "36": 32,
    "37": 0,
    "38": 0,
    "39": 0,
    "40": 21,
    "41": 65,
    "42": 86,
    "43": 107,
    "44": 218,
    "45": 211,
    "46": 163,
    "47": 207,
    "48": 169,
    "49": 235,
    "50": 76,
    "51": 186,
    "52": 61,
    "53": 207,
    "54": 51,
    "55": 238,
    "56": 101,
    "57": 131,
    "58": 224,
    "59": 115,
    "60": 58,
    "61": 228,
    "62": 178,
    "63": 204,
    "64": 223,
    "65": 233,
    "66": 44,
    "67": 209,
    "68": 189,
    "69": 146,
    "70": 238,
    "71": 22,
    "72": 15,
    "73": 32,
    "74": 0,
    "75": 0,
    "76": 0,
    "77": 2,
    "78": 0,
    "79": 0,
    "80": 0,
    "81": 105,
    "82": 100,
    "83": 9,
    "84": 0,
    "85": 0,
    "86": 0,
    "87": 1,
    "88": 160,
    "89": 134,
    "90": 1,
    "91": 0,
    "92": 0,
    "93": 0,
    "94": 0,
    "95": 0,
    "96": 13,
    "97": 5,
  },
]
 */

    }


        /*
    }

    @Test
    void transferToBytes() {

        final String idStr = "0935b606da7a977f162e3fd3bc41ca0ebcc8019ec805bd3e7d3dfad6e780b7ce";
        final BigInteger id = new BigInteger(idStr, 16);
        Transfer transfer = DeployUtil.makeTransfer(
                    0x010a,
                new PublicKey("0935b606da7a977f162e3fd3bc41ca0ebcc8019ec805bd3e7d3dfad6e780b7ce"),
                id
        );
    }
/*

    Expected body

[


  {
    "0": 0,
    "1": 0,
    "2": 0,
    "3": 0,
    "4": 0,
    "5": 1,
    "6": 0,
    "7": 0,
    "8": 0,
    "9": 6,
    "10": 0,
    "11": 0,
    "12": 0,
    "13": 97,
    "14": 109,
    "15": 111,
    "16": 117,
    "17": 110,
    "18": 116,
    "19": 7,
    "20": 0,
    "21": 0,
    "22": 0,
    "23": 6,
    "24": 0,
    "25": 160,
    "26": 114,
    "27": 78,
    "28": 24,
    "29": 9,
    "30": 8,
  },
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
    "36": 9,
    "37": 53,
    "38": 182,
    "39": 6,
    "40": 218,
    "41": 122,
    "42": 151,
    "43": 127,
    "44": 22,
    "45": 46,
    "46": 63,
    "47": 211,
    "48": 188,
    "49": 65,
    "50": 202,
    "51": 14,
    "52": 188,
    "53": 200,
    "54": 1,
    "55": 158,
    "56": 200,
    "57": 5,
    "58": 189,
    "59": 62,
    "60": 125,
    "61": 61,
    "62": 250,
    "63": 214,
    "64": 231,
    "65": 128,
    "66": 183,
    "67": 206,
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
  },
]
     */
}