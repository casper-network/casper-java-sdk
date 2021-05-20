package com.casper.sdk.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for AbstractCLType
 */
class AbstractCLTypeTest {

    private AbstractCLType clType;

    @BeforeEach
    void setUp() {
        clType = new AbstractCLType(CLType.I32) {
            @Override
            public byte[] getBytes() {
                return "0f0f0f0f".getBytes();
            }
        };
    }

    @Test
    void getCLType() {
        assertThat(clType.getCLType(), is(CLType.I32));
    }

    @Test
    void fromString() {
        final byte[] bytes = {15, 31, 47};
        assertThat(AbstractCLType.fromString("0f1f2f"),is(bytes));
    }

    @Test
    void toHex() {
        final byte[] bytes = {15, 31, 47};
        assertThat(AbstractCLType.toHex(bytes),is("0f1f2f"));
    }

}