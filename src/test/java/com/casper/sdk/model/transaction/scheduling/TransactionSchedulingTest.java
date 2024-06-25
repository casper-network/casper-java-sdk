package com.casper.sdk.model.transaction.scheduling;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * @author ian@meywood.com
 */
class TransactionSchedulingTest {

    @Test
    void standardScheduling() throws Exception {
        final String json = "\"Standard\"";

        final TransactionScheduling transactionScheduling = new ObjectMapper().readValue(json, TransactionScheduling.class);
        assertThat(transactionScheduling, is(instanceOf(Standard.class)));

        final String written = new ObjectMapper().writeValueAsString(transactionScheduling);
        JSONAssert.assertEquals(json, written, true);
    }

    @Test
    void futureTimestampScheduling() throws Exception {
        final String json = "{\"FutureTimestamp\":\"2020-08-07T01:20:22.207Z\"}";

        final TransactionScheduling transactionScheduling = new ObjectMapper().readValue(json, TransactionScheduling.class);
        assertThat(transactionScheduling, is(instanceOf(FutureTimestamp.class)));
        FutureTimestamp futureTimestamp = (FutureTimestamp) transactionScheduling;
        assertThat(futureTimestamp.getFutureTimestamp(), is("2020-08-07T01:20:22.207Z"));

        final String written = new ObjectMapper().writeValueAsString(transactionScheduling);
        JSONAssert.assertEquals(json, written, true);
    }

    @Test
    void futureEraScheduling() throws Exception {
        final String json = "{\"FutureEra\":\"1234567890\"}";
        final TransactionScheduling transactionScheduling = new ObjectMapper().readValue(json, TransactionScheduling.class);
        assertThat(transactionScheduling, is(instanceOf(FutureEra.class)));
        FutureEra futureEra = (FutureEra) transactionScheduling;
        assertThat(futureEra.getEraId(), is(new BigInteger("1234567890")));

        final String written = new ObjectMapper().writeValueAsString(transactionScheduling);
        JSONAssert.assertEquals(json, written, true);
    }
}
