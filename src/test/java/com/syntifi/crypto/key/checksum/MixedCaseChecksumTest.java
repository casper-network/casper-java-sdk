package com.syntifi.crypto.key.checksum;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MixedCaseChecksumTest {

    @Test
    void eip55ShouldMatchLowerCaseAddress() {
        assertEquals("fB6916095ca1df60bB79Ce92cE3Ea74c37c5d359",
                MixedCaseChecksum.checksumEncodeEIP55("fb6916095ca1df60bb79ce92ce3ea74c37c5d359"));
    }

    @Test
    void eip55ShouldMatchUpperCaseAddress() {
        assertEquals("fB6916095ca1df60bB79Ce92cE3Ea74c37c5d359",
                MixedCaseChecksum.checksumEncodeEIP55("FB6916095CA1DF60BB79CE92CE3EA74C37C5D359"));
    }

    @Test
    void cep57houldMatchEmptyAddress() {
        assertEquals("",
                MixedCaseChecksum.checksumEncodeEIP55(""));
    }
    @Test
    void cep57houldMatchLowerCaseAddress() {
        assertEquals("010573D52dFA032716fDC2AE5396987f280304106fC11f6Ac6Ccf287B81dc09ED7",
                MixedCaseChecksum.checksumEncodeCEP57("010573d52dfa032716fdc2ae5396987f280304106fc11f6ac6ccf287b81dc09ed7"));
    }

    @Test
    void cep57houldMatchUpperCaseAddress() {
        assertEquals("51DA5aE5C39880Bfe4f94B0898332d1BD37e647F72f79Cf23475df1Bb1f85bEA",
                MixedCaseChecksum.checksumEncodeCEP57("51da5ae5c39880bfe4f94b0898332d1bd37e647f72f79cf23475df1bb1f85bea"));
    }
}
