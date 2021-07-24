package com.casper.sdk.service.serialization.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for number utils
 */
class TtlUtilsTest {

    @Test
    void getTtlLong() {
        assertThat(TtlUtils.getTtlLong("1s"), is(1000L));
        assertThat(TtlUtils.getTtlLong("1m"), is(60000L));
        assertThat(TtlUtils.getTtlLong("1h"), is(60 * 60000L));
        assertThat(TtlUtils.getTtlLong("1d"), is(24* 60 * 60000L));
    }

    /**
     * Tests a duration in ms can be converted to a string duration.
     */
    @Test
    void toTtlStr() {
        assertThat(TtlUtils.toTtlStr(10), is("0.01s"));
        assertThat(TtlUtils.toTtlStr(1000), is("1s"));
        assertThat(TtlUtils.toTtlStr(1000 * 60), is("1m"));
        assertThat(TtlUtils.toTtlStr(1000 * 60 * 60), is("1h"));
    }
}
