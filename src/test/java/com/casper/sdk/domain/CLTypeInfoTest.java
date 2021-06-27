package com.casper.sdk.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Unit tests for {@link CLTypeInfo}
 */
class CLTypeInfoTest {

    @Test
    void getType() {
        assertThat(new CLTypeInfo(CLType.U32).getType(), is(CLType.U32));
    }

    @Test
    void testEquals() {
        assertThat(new CLTypeInfo(CLType.U32), is(new CLTypeInfo(CLType.U32)));
        assertThat(new CLTypeInfo(CLType.U32), is(not(new CLTypeInfo(CLType.U64))));
    }

    @Test
    void testHashCode() {
        assertThat(new CLTypeInfo(CLType.U32).hashCode(), is(new CLTypeInfo(CLType.U32).hashCode()));
        assertThat(new CLTypeInfo(CLType.U32).hashCode(), is(not(new CLTypeInfo(CLType.U64).hashCode())));
    }
}
