package com.casper.sdk.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.casper.sdk.types.StoredContractNames.PAYMENT;
import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class StoredVersionedContractByNameTest {

    private StoredVersionedContractByName payment;

    @BeforeEach
    void setUp() {
        payment = new StoredVersionedContractByName(
                "payment",
                123456L, "entry-point",
                CLValue.fromString("05005550b405"),
                Arrays.asList(
                        new DeployNamedArg("foo", new CLValue("bar".getBytes(StandardCharsets.UTF_8), new CLTypeInfo(CLType.STRING), "bar")),
                        new DeployNamedArg("amount", new CLValue("05005550b405", new CLTypeInfo(CLType.U64), "24500000000"))
                )
        );
    }

    @Test
    void  storedContractByNameVersionedContactVersion() {
        assertThat(payment.getVersion().isPresent(), is(true));
        assertThat(payment.getVersion().get(), is(123456L));
    }

    @Test
    void storedContractByNameVersionedGetModuleBytes() {
        assertThat(payment.getModuleBytes(), is(new byte[]{5, 0, 85, 80, -76, 5}));
    }

    @Test
    void storedContractByNameVersionedGetArgs() {
        assertThat(payment.getName(), is(PAYMENT));
        assertThat(payment.getEntryPoint(), is("entry-point"));
        assertThat(payment.getTag(), is(4));
        assertThat(payment.getArgs(), hasSize(2));
        assertThat(payment.getArgs().get(0).getName(), is("foo"));
        assertThat(payment.getArgs().get(0).getValue().getCLTypeInfo().getType(), is(CLType.STRING));
        assertThat(payment.getArgs().get(1).getName(), is("amount"));
    }
}