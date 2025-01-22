package com.casper.sdk.model.transaction.pricing;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.junit.jupiter.api.Disabled;
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
    @Disabled
    void paymentLimitedMode() throws Exception {
        byte[] expected = {
                (byte) 4, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 9, (byte) 0,
                (byte) 0, (byte) 0, (byte) 3, (byte) 0, (byte) 10, (byte) 0, (byte) 0, (byte) 0, (byte) 11, (byte) 0,
                (byte) 0, (byte) 0, (byte) 0, (byte) 232, (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 0, (byte) 2, (byte) 1
        };

        final String json = "{\"PaymentLimited\":{\"payment_amount\": 1000, \"gas_price_tolerance\": 2, \"standard_payment\": true}}";

        final PricingMode pricingMode = new ObjectMapper().readValue(json, PricingMode.class);
        assertThat(pricingMode, is(instanceOf(PaymentLimited.class)));

        final PaymentLimited paymentLimited = (PaymentLimited) pricingMode;
        assertThat(paymentLimited.getPaymentAmount(), is(new BigInteger("1000")));
        assertThat(paymentLimited.getGasPriceTolerance(), is(2));
        assertThat(paymentLimited.isStandardPayment(), is(true));

        final String writtenJson = new ObjectMapper().writeValueAsString(pricingMode);
        JSONAssert.assertEquals(json, writtenJson, false);

        assertBytes(expected, pricingMode);
    }

    @Test
    void fixedPricingMode() throws Exception {

        byte[] expected = {
                (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0, (byte) 0,
                (byte) 1, (byte) 0, (byte) 1, (byte) 0, (byte) 0, (byte) 0, (byte) 2, (byte) 0, (byte) 2, (byte) 0,
                (byte) 0, (byte) 0, (byte) 3, (byte) 0, (byte) 0, (byte) 0, (byte) 1, (byte) 1, (byte) 2
        };

        final String json = "{\"Fixed\":{\"gas_price_tolerance\": 1, \"additional_computation_factor\": 2}}";

        final PricingMode pricingMode = new ObjectMapper().readValue(json, PricingMode.class);
        assertThat(pricingMode, is(instanceOf(FixedPricingMode.class)));

        final FixedPricingMode fixedPricingMode = (FixedPricingMode) pricingMode;
        assertThat(fixedPricingMode.getGasPriceTolerance(), is(1));
        assertThat(fixedPricingMode.getAdditionalComputationFactor(), is(2));

        final String writtenJson = new ObjectMapper().writeValueAsString(pricingMode);
        JSONAssert.assertEquals(json, writtenJson, false);

        assertBytes(expected, pricingMode);
    }

    @Test
    @Disabled
    void prepaidPricingMode() throws Exception {

        byte[] expected = Hex.decode(
                "020000000000000000000100010000002100000002b7188ee3a749e6504d11708e23e41d16d98bdb2359d413a3bb84b48b5cc215d4"
        );

        final String json = "{\"Prepaid\":{\"receipt\": \"b7188ee3a749e6504d11708e23e41d16d98bdb2359d413a3bb84b48b5cc215d4\"}}";

        final PricingMode pricingMode = new ObjectMapper().readValue(json, PricingMode.class);
        assertThat(pricingMode, is(instanceOf(PrepaidPricingMode.class)));

        final PrepaidPricingMode fixedPricingMode = (PrepaidPricingMode) pricingMode;
        assertThat(fixedPricingMode.getReceipt(), is(new Digest("b7188ee3a749e6504d11708e23e41d16d98bdb2359d413a3bb84b48b5cc215d4")));

        final String writtenJson = new ObjectMapper().writeValueAsString(pricingMode);
        JSONAssert.assertEquals(json, writtenJson, false);

        assertBytes(expected, pricingMode);
    }

    private void assertBytes(byte[] expected, PricingMode pricingMode) throws NoSuchTypeException, ValueSerializationException {
        final SerializerBuffer ser = new SerializerBuffer();
        pricingMode.serialize(ser, Target.BYTE);
        final byte[] actual = ser.toByteArray();
        assertThat(actual, is(expected));
    }
}
