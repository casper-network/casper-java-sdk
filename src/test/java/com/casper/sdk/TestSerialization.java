package com.casper.sdk;

import org.junit.Test;
import com.casper.sdk.service.serialization.ArgsService;
import com.casper.sdk.service.serialization.factory.TypesEnum;
import com.casper.sdk.service.serialization.factory.TypesFactory;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;


public class TestSerialization {

    private final TypesFactory factory = new TypesFactory();
    private final ArgsService argsService = new ArgsService();

    @Test
    public void testTransferArgs() throws IOException {
        ClassLoader classLoader = TestSerialization.class.getClassLoader();
        JsonNode testTransfer = new ObjectMapper().readTree(new File(classLoader.getResource("serialization/transfer-args.json").getFile()));

        JsonNode transfer = argsService.buildTransfer("24500000000", "0101010101010101010101010101010101010101010101010101010101010101", 999, "this is transfer");

        System.out.println(transfer.toPrettyString());

        assert testTransfer.get("Transfer").get("args").get(0).get(1).get("cl_type").equals(
                transfer.get("Transfer").get("args").get(0).get(1).get("cl_type")
        );
        assert testTransfer.get("Transfer").get("args").get(0).get(1).get("bytes").equals(
                transfer.get("Transfer").get("args").get(0).get(1).get("bytes")
        );
        assert testTransfer.get("Transfer").get("args").get(0).get(1).get("parsed").equals(
                transfer.get("Transfer").get("args").get(0).get(1).get("parsed")
        );

        assert testTransfer.get("Transfer").get("args").get(1).get(1).get("cl_type").get("ByteArray").equals(
                transfer.get("Transfer").get("args").get(1).get(1).get("cl_type").get("ByteArray")
        );
        assert testTransfer.get("Transfer").get("args").get(1).get(1).get("bytes").equals(
                transfer.get("Transfer").get("args").get(1).get(1).get("bytes")
        );
        assert testTransfer.get("Transfer").get("args").get(1).get(1).get("parsed").equals(
                transfer.get("Transfer").get("args").get(1).get(1).get("parsed")
        );

        assert testTransfer.get("Transfer").get("args").get(2).get(1).get("cl_type").equals(
                transfer.get("Transfer").get("args").get(2).get(1).get("cl_type")
        );
        assert testTransfer.get("Transfer").get("args").get(2).get(1).get("bytes").equals(
                transfer.get("Transfer").get("args").get(2).get(1).get("bytes")
        );
        assert testTransfer.get("Transfer").get("args").get(2).get(1).get("parsed").equals(
                transfer.get("Transfer").get("args").get(2).get(1).get("parsed")
        );

        assert testTransfer.get("Transfer").get("args").get(3).get(1).get("cl_type").equals(
                transfer.get("Transfer").get("args").get(3).get(1).get("cl_type")
        );
        assert testTransfer.get("Transfer").get("args").get(3).get(1).get("bytes").equals(
                transfer.get("Transfer").get("args").get(3).get(1).get("bytes")
        );
        assert testTransfer.get("Transfer").get("args").get(3).get(1).get("parsed").equals(
                transfer.get("Transfer").get("args").get(3).get(1).get("parsed")
        );

    }


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
        assert value.equals("01e703000000000000");

        value = factory.getInstance(TypesEnum.U64.name()).serialize("0");
        assert value.equals("00");

        value = factory.getInstance(TypesEnum.U64.name()).serialize("");
        assert value.equals("00");

    }

    @Test
    public void testU32(){

        String value = factory.getInstance(TypesEnum.U32.name()).serialize("1024");
        assert value.equals("00040000");

    }

    @Test
    public void testString(){
        String value = factory.getInstance(TypesEnum.String.name()).serialize("Hello, World!", factory);
        assert value.equals("0d00000048656c6c6f2c20576f726c6421");

        value = factory.getInstance(TypesEnum.String.name()).serialize("this is transfer", factory);
        assert value.equals("1000000074686973206973207472616e73666572");

    }




}
