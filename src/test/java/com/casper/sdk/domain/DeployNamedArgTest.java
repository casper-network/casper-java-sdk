package com.casper.sdk.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit test for the {@link DeployNamedArg} domain object.
 */
class DeployNamedArgTest {

    private DeployNamedArg namedArg;

    @BeforeEach
    void setUp() {
        namedArg = new DeployNamedArg("named-arg", new CLValue("0123".getBytes(StandardCharsets.UTF_8), CLType.I32));
    }

    @Test
    void getName() {
        assertThat(namedArg.getName(), is("named-arg"));
    }

    @Test
    void getValue() {
        assertThat(namedArg.getValue().getBytes(), is(new Byte[]{48, 49, 50, 51}));
        assertThat(namedArg.getValue().getCLTypeInfo().getType(), is(CLType.I32));
    }
}