package com.casper.sdk;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.casper.sdk.controller.CasperSdk;

public class TestChain {

//    @Test
    public void testAgainstChain() throws Throwable {
        CasperSdk casperSdk = new CasperSdk("http://localhost", "40101");

        String accountBalance = casperSdk.getAccountBalance("019d9bc24944a08204c09d222405127c8ca7dc7dcc267ccc6b392deab8148d111d");
        assertEquals("1000000000000000000000000000000000", accountBalance);
    }


}
