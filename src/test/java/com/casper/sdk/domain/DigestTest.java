package com.casper.sdk.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Unit tests for the {@link Digest} domain object
 */
class DigestTest {

    @Test
    void getHash() {
        assertThat(new Digest("1234").getHash(), is("1234"));
    }

    @Test
    void testEquals() {
        assertThat(new Digest("1234"), is(new Digest("1234")));
        assertThat(new Digest("1234"), is(not(new Digest("2345"))));
    }

    @Test
    void testHashCode() {
        assertThat(new Digest("1234").hashCode(), is(new Digest("1234").hashCode()));
        assertThat(new Digest("1234").hashCode(), is(not(new Digest("2345").hashCode())));
    }
}