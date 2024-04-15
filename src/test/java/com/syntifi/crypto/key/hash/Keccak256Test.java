package com.syntifi.crypto.key.hash;

import com.syntifi.crypto.key.encdec.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class Keccak256Test {

    @Test
    void kekkac256ShouldMatch() {
        Assertions.assertEquals("a9059cbb2ab09eb219583f4a59a5d0623ade346d962bcd4e46b11da047c9049b",
                Hex.encode(Keccak256.digest("transfer(address,uint256)".getBytes())));
    }
}
