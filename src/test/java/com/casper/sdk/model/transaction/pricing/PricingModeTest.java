package com.casper.sdk.model.transaction.pricing;

import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for {@link PricingMode}.
 *
 * @author ian@meywood.com
 */
class PricingModeTest {

    @Test
    void classicPricingMode() throws Exception {

        final String json = "{\"Classic\":{\"payment_amount\": \"12345\", \"gas_price_tolerance\": 5, \"standard_payment\": true}}";

        final PricingMode pricingMode = new ObjectMapper().readValue(json, PricingMode.class);
        assertThat(pricingMode, is(instanceOf(ClassicPricingMode.class)));

        final ClassicPricingMode classicPricingMode = (ClassicPricingMode) pricingMode;
        assertThat(classicPricingMode.getPaymentAmount(), is(new BigInteger("12345")));
        assertThat(classicPricingMode.getGasPriceTolerance(), is(5));
        assertThat(classicPricingMode.isStandardPayment(), is(true));

        final String writtenJson = new ObjectMapper().writeValueAsString(pricingMode);
        JSONAssert.assertEquals(json, writtenJson, false);
    }

    @Test
    void fixedPricingMode() throws Exception {
        final String json = "{\"Fixed\":{\"gas_price_tolerance\": 5}}";

        final PricingMode pricingMode = new ObjectMapper().readValue(json, PricingMode.class);
        assertThat(pricingMode, is(instanceOf(FixedPricingMode.class)));

        final FixedPricingMode fixedPricingMode = (FixedPricingMode) pricingMode;
        assertThat(fixedPricingMode.getGasPriceTolerance(), is(5));

        final String writtenJson = new ObjectMapper().writeValueAsString(pricingMode);
        JSONAssert.assertEquals(json, writtenJson, false);
    }

    @Test
    void reservedPricingMode() throws Exception {
        final String json = "{\"Reserved\":{\"receipt\": \"b7188ee3a749e6504d11708e23e41d16d98bdb2359d413a3bb84b48b5cc215d4\"}}";

        final PricingMode pricingMode = new ObjectMapper().readValue(json, PricingMode.class);
        assertThat(pricingMode, is(instanceOf(ReservedPricingMode.class)));

        final ReservedPricingMode fixedPricingMode = (ReservedPricingMode) pricingMode;
        assertThat(fixedPricingMode.getReceipt(), is(new Digest("b7188ee3a749e6504d11708e23e41d16d98bdb2359d413a3bb84b48b5cc215d4")));

        final String writtenJson = new ObjectMapper().writeValueAsString(pricingMode);
        JSONAssert.assertEquals(json, writtenJson, false);
    }
}
