package com.casper.sdk;

import org.junit.Test;
import com.casper.sdk.service.serialization.factory.TypesEnum;
import com.casper.sdk.service.serialization.factory.TypesFactory;


public class TestSerialization {

    private final TypesFactory factory = new TypesFactory();

    @Test
    public void testU512(){

        String value = factory.getInstance(TypesEnum.U512.name()).serialize("123456789101112131415");
        assert value.equals("0957ff1ada959f4eb106");

        value = factory.getInstance(TypesEnum.U512.name()).serialize("7");
        assert value.equals("0107");

        value = factory.getInstance(TypesEnum.U512.name()).serialize("1024");
        assert value.equals("020004");

        value = factory.getInstance(TypesEnum.U512.name()).serialize("10111111111111");
        assert value.equals("06c7a12f2d3209");

        value = factory.getInstance(TypesEnum.U512.name()).serialize("24500000000");
        assert value.equals("05005550b405");

    }

    @Test
    public void testU64(){

        String value = factory.getInstance(TypesEnum.U64.name()).serialize("999");
        assert value.equals("e703000000000000");

////        Optional.of(U64)
////                if not present 00
////                else 01 and number


    }

    @Test
    public void testU32(){

        String value = factory.getInstance(TypesEnum.U32.name()).serialize("1024");
        assert value.equals("00040000");

    }




}
