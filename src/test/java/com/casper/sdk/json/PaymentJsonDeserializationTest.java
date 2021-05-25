package com.casper.sdk.json;

import com.casper.sdk.domain.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Tests a Payment DeployExecutable can be parsed from a JSON string
 */
public class PaymentJsonDeserializationTest {

    private static final String PAYMENT_JSON = /* "payment": */ """
            {
                "ModuleBytes": {
                  "module_bytes": "",
                  "args": [
                    [
                      "amount",
                      {
                        "cl_type": "U512",
                        "bytes": "0400ca9a3b",
                        "parsed": "1000000000"
                      }
                    ]
                  ]
                }
            },""";

    private DeployExecutable deployExecutable;

    @BeforeEach
    void setUp() throws IOException {
        final SimpleModule module = new SimpleModule();
        module.addDeserializer(DeployExecutable.class, new DeployExecutableJsonDeserializer());
        module.addDeserializer(DeployNamedArg.class, new DeployNamedArgJsonDeserializer());
        final ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);

        deployExecutable = mapper.reader().readValue(PAYMENT_JSON, DeployExecutable.class);
    }

    @Test
    void testParseTransferDeployExecutableFromJson() {
        assertThat(deployExecutable, is(instanceOf(Payment.class)));

        final Payment payment = (Payment) deployExecutable;
        assertThat(payment.getModuleBytes(), is(new byte[0]));
        assertThat(payment.getArgs(), hasSize(1));
        assertThat(payment.getArgs().get(0).getName(), is("amount"));
        assertThat(payment.getArgs().get(0).getValue().getCLTypeInfo().getType(), is(CLType.U512));
        assertThat(payment.getArgs().get(0).getValue().getBytes(), is(CLValue.fromString("0400ca9a3b")));
        assertThat(payment.getArgs().get(0).getValue().getParsed(), is("1000000000"));
    }
}
