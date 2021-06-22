package com.casper.sdk.service;

import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.Test;

import java.security.PrivateKey;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

class SigningServiceTest {

    private final SigningService signingService = new SigningService();


    @Test
    void generateEdDSAKey() {
        final PrivateKey privateKey = signingService.generateEdDSAKey();
        assertThat(privateKey, is(notNullValue()));
        assertThat(privateKey.getAlgorithm(), is("EdDSA"));
        String format = privateKey.getFormat();
        assertThat(privateKey.getEncoded().length, is(32));
    }

    @Test
    void signWithPrivateKey() {

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

        // Copy of a deploy hash
        final byte[] toSign = {
                (byte) 153, (byte) 144, (byte) 19, (byte) 83, (byte) 219, (byte) 161, (byte) 143, (byte) 137, (byte) 59,
                (byte) 67, (byte) 187, (byte) 238, (byte) 65, (byte) 111, (byte) 80, (byte) 243, (byte) 142, (byte) 77,
                (byte) 113, (byte) 46, (byte) 2, (byte) 166, (byte) 121, (byte) 118, (byte) 34, (byte) 205, (byte) 123,
                (byte) 14, (byte) 215, (byte) 85, (byte) 234, (byte) 161
        };

       /* {
            "0": 35,
                "1": 146,
                "2": 167,
                "3": 195,
                "4": 43,
                "5": 242,
                "6": 11,
                "7": 95,
                "8": 130,
                "9": 162,
                "10": 175,
                "11": 238,
                "12": 75,
                "13": 92,
                "14": 12,
                "15": 254,
                "16": 169,
                "17": 235,
                "18": 35,
                "19": 170,
                "20": 152,
                "21": 229,
                "22": 217,
                "23": 229,
                "24": 188,
                "25": 244,
                "26": 254,
                "27": 149,
                "28": 130,
                "29": 248,
                "30": 221,
                "31": 252,
                "32": 36,
                "33": 242,
                "34": 150,
                "35": 248,
                "36": 128,
                "37": 90,
                "38": 136,
                "39": 218,
                "40": 42,
                "41": 125,
                "42": 68,
                "43": 159,
                "44": 2,
                "45": 12,
                "46": 197,
                "47": 21,
                "48": 165,
                "49": 98,
                "50": 121,
                "51": 40,
                "52": 209,
                "53": 160,
                "54": 57,
                "55": 99,
                "56": 204,
                "57": 172,
                "58": 30,
                "59": 28,
                "60": 119,
                "61": 121,
                "62": 209,
                "63": 1,
        }
        */

        final byte[] expectedSigned = ByteUtils.decodeHex(
                "01c5534d6965c6e528b7437ab0c1d6ccc48005ce58e37dba017d15db6e5569311dada09ea2c5b6c162790856dc2c35596318c9d4e8e6b39f33d150d0fdb2ca7201"
        );

        final byte[] signed = signingService.signWithPrivateKey(privateKeyBytes, toSign);

        assertThat(signed, is(expectedSigned));

    }
}