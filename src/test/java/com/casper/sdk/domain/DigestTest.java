package com.casper.sdk.domain;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Unit tests for the {@link Digest} domain object
 */
class DigestTest {

    private static final String HASH_ONE = "d7a68bbe656a883d04bba9f26aa340dbe3f8ec99b2adb63b628f2bc920431998";
    public static final String HASH_TWO = "f2e0782bba4a0a9663cafc7d707fd4a74421bc5bfef4e368b7e8f38dfab87db8";

    @Test
    void getHash() {
        assertThat(new Digest(HASH_ONE).getHash(), is(HASH_ONE));
    }

    @Test
    void testEquals() {
        assertThat(new Digest(HASH_ONE), is(new Digest(HASH_ONE)));
        assertThat(new Digest(HASH_ONE), is(not(new Digest(HASH_TWO))));
    }

    @Test
    void testHashCode() {
        assertThat(new Digest(HASH_ONE).hashCode(), is(new Digest(HASH_ONE).hashCode()));
        assertThat(new Digest(HASH_ONE).hashCode(), is(not(new Digest(HASH_TWO).hashCode())));
    }

    @Test
    void testIllegalLength() {
        try {
            new Digest("1234");
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Hash must be 32 bytes long: 1234"));
        }

    }

    @Test
    void testNullHash() {
        try {
            new Digest(null);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), is("Hash must not be null"));
        }

    }
}