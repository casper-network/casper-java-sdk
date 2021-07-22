package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Tests a Transfer DeployExecutable can be parsed from a JSON string
 */
class TransferJsonDeserializerTest {

    // Transfer is used to indicate the type
    private static final String TRANSFER_JSON = "/com/casper/sdk/service/json/transfer-snippet.json";

    private DeployExecutable deployExecutable;

    @BeforeEach
    void setUp() throws IOException {
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(DeployExecutable.class, new DeployExecutableJsonDeserializer());
        module.addDeserializer(DeployNamedArg.class, new DeployNamedArgJsonDeserializer());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        final InputStream in = getClass().getResourceAsStream(TRANSFER_JSON);
        deployExecutable = mapper.reader().readValue(in, DeployExecutable.class);
    }

    @Test
    void testParseTransferDeployExecutableFromJson() {
        assertThat(deployExecutable, is(instanceOf(Transfer.class)));
    }

    @Test
    void testTransferArgs() {
        List<DeployNamedArg> args = deployExecutable.getArgs();
        assertThat(args.size(), is(4));
    }

    @Test
    void testAmountArg() {
        DeployNamedArg amount = deployExecutable.getArgs().get(0);
        assertThat(amount.getName(), is("amount"));
        assertThat(amount.getValue().getCLTypeInfo().getType(), is(CLType.U512));
        assertThat(amount.getValue().getBytes(), is(CLValue.fromString("05005550b405")));
        assertThat(amount.getValue().getParsed(), is("24500000000"));
    }

    @Test
    void testTargetArg() {
        DeployNamedArg target = deployExecutable.getArgs().get(1);
        assertThat(target.getName(), is("target"));
        assertThat(target.getValue().getCLTypeInfo().getType(), is(CLType.BYTE_ARRAY));
        assertThat(((CLByteArrayInfo) target.getValue().getCLTypeInfo()).getSize(), is(32));
        assertThat(target.getValue().getBytes(), is(CLValue.fromString("0101010101010101010101010101010101010101010101010101010101010101")));
        assertThat(target.getValue().getParsed(), is("0101010101010101010101010101010101010101010101010101010101010101"));
    }

    @Test
    void testIdArg() {
        DeployNamedArg id = deployExecutable.getArgs().get(2);
        assertThat(id.getName(), is("id"));
        assertThat(id.getValue().getCLTypeInfo().getType(), is(CLType.U64));
        assertThat(id.getValue().getBytes(), is(CLValue.fromString("01e703000000000000")));
        assertThat(id.getValue().getParsed(), is(BigInteger.valueOf(999)));
    }

    @Test
    void testAdditionalInfoArg() {
        DeployNamedArg additionalInfo = deployExecutable.getArgs().get(3);
        assertThat(additionalInfo.getName(), is("additional_info"));
        assertThat(additionalInfo.getValue().getCLTypeInfo().getType(), is(CLType.STRING));
        assertThat(additionalInfo.getValue().getBytes(), is(CLValue.fromString("1000000074686973206973207472616e73666572")));
        assertThat(additionalInfo.getValue().getParsed(), is("this is transfer"));
    }
}
