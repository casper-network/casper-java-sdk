package com.casper.sdk.types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class StoredVersionedContractByHashTest {

    private StoredVersionedContractByHash payment;

    @BeforeEach
    void setUp() {
        payment = new StoredVersionedContractByHash(
                new ContractHash("c4c411864f7b717c27839e56f6f1ebe5da3f35ec0043f437324325d65a22afa4"),
                123456L, "entry-point",
                Arrays.asList(
                        new DeployNamedArg("foo", new CLValue("bar".getBytes(StandardCharsets.UTF_8), new CLTypeInfo(CLType.STRING), "bar")),
                        new DeployNamedArg("amount", new CLValue("05005550b405", new CLTypeInfo(CLType.U64), "24500000000"))
                )
        );
    }

    @Test
    void storedVersionedContractByHashGetHash() {
        assertThat(payment.getHash(), is(new ContractHash("c4c411864f7b717c27839e56f6f1ebe5da3f35ec0043f437324325d65a22afa4")));
    }

    @Test
    void storedVersionedContractByHashGetVersion() {
        assertThat(payment.getVersion().isPresent(), is(true));
        assertThat(payment.getVersion().get(), is(123456L));
    }


    @Test
    void storedVersionedContractByHashGetArgs() {
        assertThat(payment.getEntryPoint(), is("entry-point"));
        assertThat(payment.getTag(), is(3));
        assertThat(payment.getArgs(), hasSize(2));
        assertThat(payment.getArgs().get(0).getName(), is("foo"));
        assertThat(payment.getArgs().get(0).getValue().getCLTypeInfo().getType(), is(CLType.STRING));
        assertThat(payment.getArgs().get(1).getName(), is("amount"));
    }

}