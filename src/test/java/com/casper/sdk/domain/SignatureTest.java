package com.casper.sdk.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class SignatureTest {

    @Test
    void signatureFromString() {

        final String signatureStr = "01fc429049ee6f41fa9f2ee7504bc2eb42dd81ae7a870ef429494e4f6dadd44a35106a4b6dffc389c091234611788f020e5e10e3b9a6072d136423b210fd56b208";

        final byte[] signatureBytes = new byte[]{
                -4, 66, -112, 73, -18, 111, 65, -6, -97, 46, -25, 80, 75, -62, -21, 66, -35, -127, -82, 122, -121, 14,
                -12, 41, 73, 78, 79, 109, -83, -44, 74, 53, 16, 106, 75, 109, -1, -61, -119, -64, -111, 35, 70, 17, 120,
                -113, 2, 14, 94, 16, -29, -71, -90, 7, 45, 19, 100, 35, -78, 16, -3, 86, -78, 8
        };

        final Signature signature = new Signature(signatureStr);
        assertThat(signature.getKeyAlgorithm(), is(KeyAlgorithm.ED25519));
        assertThat(signature.getBytes().length, is(64));
        assertThat(signature.getBytes(), is(signatureBytes));
    }
}