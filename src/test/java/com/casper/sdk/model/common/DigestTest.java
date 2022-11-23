package com.casper.sdk.model.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DigestTest {

    @Test
    void testDigestIsValid(){
        assertTrue(new Digest("99483863a391510b8d3447dd5cfc446b42d65e598672d569abc4cdded85b81e6").isValid());
    }

    @Test
    void testDigestIsInValid(){
        assertFalse(new Digest("9483863a391510b8d3447dd5cfc446b42d65e598672d569abc4cdded85b81e6").isValid());
        assertFalse(new Digest("uref-99483863a391510b8d3447dd5cfc446b42d65e598672d569abc4cdded85b81e6").isValid());
    }
}
