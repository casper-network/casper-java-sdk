package com.syntifi.crypto.key.encdec;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class Base58Test {
    @Test
    void bs58() {
        String expected = "16UjcYNBG9GTK4uq2f7yYEbuifqCzoLMGS";
        byte[] bytes = new byte[]{
                0, 60, 23, 110, 101, (byte) 155, (byte) 234,
                15, 41, (byte) 163, (byte) 233, (byte) 191, 120, (byte) 128,
                (byte) 193, 18, (byte) 177, (byte) 179, 27, 77, (byte) 200,
                38, 38, (byte) 129, (byte) 135
        };

        assertEquals(expected, Base58.encode(bytes));
        assertArrayEquals(Base58.decode(expected), bytes);
    }
}