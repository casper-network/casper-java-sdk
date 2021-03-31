package com.casper.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.codec.DecoderException;
import org.junit.jupiter.api.Test;

import com.casper.sdk.service.HashService;

import java.security.NoSuchAlgorithmException;

public class TestHashService {

    private static final String ED25519publicKey = "01381b36cd07ad85348607ffe0fa3a2d033ea941d14763358ebeace9c8ad3cb771";
    private static final String ED25519hash = "07b30fdd279f21d29ab1922313b56ad3905e7dd6a654344b8012e0be9fefa51b";

    private static final String SECP256K1publicKey = "0203b2f8c0613d2d866948c46e296f09faed9b029110d424d19d488a0c39a811ebbc";
    private static final String SECP256K1hash = "aebf6cf44f8d7a633b4e2084ce3be3bbe3db2cec62e49afe103dca79f7818d43";

    @Test
    public void testValidBlakeEd25519() throws NoSuchAlgorithmException, DecoderException {
        String hash = HashService.getAccountHash(ED25519publicKey);
        assertEquals(hash, ED25519hash);
    }

    @Test
    public void testValidBlakeSecp256k1() throws NoSuchAlgorithmException, DecoderException {
        String hash = HashService.getAccountHash(SECP256K1publicKey);
        assertEquals(hash, SECP256K1hash);
    }

    @Test
    public void testZeroLength() {
        assertThrows(IllegalArgumentException.class, () ->
                HashService.getAccountHash("")
        );
    }

    @Test
    public void testUnknownPrefix() {
        assertThrows(IllegalArgumentException.class, () ->
                HashService.getAccountHash("03381b36cd07ad85348607ffe0fa3a2d033ea941d14763358ebeace9c8ad3cb771")
        );
    }

    @Test
    public void testWrongLengthED25519() {
        assertThrows(IllegalArgumentException.class, () ->
                HashService.getAccountHash("01381b36cd07ad85348607ffe0fa3a2d033ea941d14763358ebeace9c8ad3cb77")
        );
    }

    @Test
    public void testWrongLengthSECP256K1() {
        assertThrows(IllegalArgumentException.class, () ->
                HashService.getAccountHash("0203b2f8c0613d2d866948c46e296f09faed9b029110d424d19d488a0c39a811ebbc1234")
        );
    }



}