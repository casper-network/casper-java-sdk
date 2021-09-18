package com.casper.sdk.types;

import com.casper.sdk.service.serialization.util.ByteUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

class StoredContractByHashTest {

    private StoredContractByHash storedContractByHash;

    @BeforeEach
    void setUp() {
        storedContractByHash = new StoredContractByHash(
                new Digest("0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f"),
                "pclphXwfYmCmdITj8hnh",
                Arrays.asList(
                        new DeployNamedArg("foo", new CLValue("bar".getBytes(StandardCharsets.UTF_8), new CLTypeInfo(CLType.STRING), "bar")),
                        new DeployNamedArg("amount", new CLValue("05005550b405", new CLTypeInfo(CLType.U64), "24500000000"))
                ));
    }

    @Test
    void getHash() {
        assertThat(storedContractByHash.getHash().getHash(), is(ByteUtils.decodeHex("0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f0f")));
    }

    @Test
    void getEntryPoint() {
        assertThat(storedContractByHash.getEntryPoint(), is("pclphXwfYmCmdITj8hnh"));
    }

    @Test
    void getTag() {
        assertThat(storedContractByHash.getTag(), is(1));
    }

    @Test
    void getArgs() {
        assertThat(storedContractByHash.getArgs(), is(hasSize(2)));
        assertThat(storedContractByHash.getArgs().get(0).getName(), is("foo"));
    }
}