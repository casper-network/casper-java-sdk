package com.syntifi.crypto.key.hash;

import com.syntifi.crypto.key.encdec.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class Blake2bTest {

    @Test
    void test() {
        Assertions.assertEquals("44682ea86b704fb3c65cd16f84a76b621e04bbdb3746280f25cf062220e471b4",
                Hex.encode(Blake2b.digest("أبو يوسف يعقوب بن إسحاق الصبّاح الكندي‎".getBytes(StandardCharsets.UTF_8), 32)));
    }
}
