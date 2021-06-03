package com.casper.sdk;

import com.casper.sdk.domain.CLType;
import com.casper.sdk.service.serialization.factory.TypesFactory;
import org.junit.jupiter.api.Test;


public class TestSerialization {

    private final TypesFactory factory = new TypesFactory();

    @Test
    public void testU512() {

        String value = factory.getInstance(CLType.U512).serialize("123456789101112131415");
        assert value.equals("0957ff1ada959f4eb106");

        value = factory.getInstance(CLType.U512).serialize("7");
        assert value.equals("0107");

        value = factory.getInstance(CLType.U512).serialize("1024");
        assert value.equals("020004");

        value = factory.getInstance(CLType.U512).serialize("10111111111111");
        assert value.equals("06c7a12f2d3209");

        value = factory.getInstance(CLType.U512).serialize("24500000000");
        assert value.equals("05005550b405");
    }

    @Test
    public void testU64() {

        String value = factory.getInstance(CLType.U64).serialize("999");
        assert value.equals("01e703000000000000");

        value = factory.getInstance(CLType.U64).serialize("0");
        assert value.equals("00");

        value = factory.getInstance(CLType.U64).serialize("");
        assert value.equals("00");
    }

    @Test
    public void testU32() {

        String value = factory.getInstance(CLType.U32).serialize("1024");
        assert value.equals("00040000");
    }

    @Test
    public void testString() {
        String value = factory.getInstance(CLType.STRING).serialize("Hello, World!");
        assert value.equals("0d00000048656c6c6f2c20576f726c6421");

        value = factory.getInstance(CLType.STRING).serialize("this is transfer");
        assert value.equals("1000000074686973206973207472616e73666572");
    }
}
