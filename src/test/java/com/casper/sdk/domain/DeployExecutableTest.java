package com.casper.sdk.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for the {@link DeployExecutable} domain object.
 */
class DeployExecutableTest {

    private DeployExecutable deployExecutable;

    @BeforeEach
    void setUp() {
        deployExecutable = new ModuleBytes(
                Arrays.asList(
                        new DeployNamedArg("foo", new CLValue("bar".getBytes(StandardCharsets.UTF_8), new CLTypeInfo(CLType.STRING), "bar")),
                        new DeployNamedArg("amount", new CLValue("05005550b405", new CLTypeInfo(CLType.U64), "24500000000"))
                )
        );

    }

    @Test
    void getArgs() {
        assertThat(deployExecutable.getArgs(), hasSize(2));
        assertThat(deployExecutable.getArgs().get(0).getName(), is("foo"));
        assertThat(deployExecutable.getArgs().get(0).getValue().getCLTypeInfo().getType(), is(CLType.STRING));
        assertThat(deployExecutable.getArgs().get(1).getName(),is("amount"));
    }

    @Test
    void getNamedArg() {
        assertThat(deployExecutable.getNamedArg("foo").getName(), is("foo"));
    }
}