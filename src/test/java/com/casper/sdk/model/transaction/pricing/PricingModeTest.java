package com.casper.sdk.model.transaction.pricing;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
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
    void classicModeByteSerialization() throws NoSuchTypeException, ValueSerializationException {

        final ClassicPricingMode classic = ClassicPricingMode.builder()
                .paymentAmount(new BigInteger("9054610395016823311"))
                .gasPriceTolerance(1)
                .standardPayment(true)
                .build();

        final byte[] expected = {
                (byte) 0x0, (byte) 0xf, (byte) 0x82, (byte) 0x89, (byte) 0x4e, (byte) 0x2e, (byte) 0x70, (byte) 0xa8,
                (byte) 0x7d, (byte) 0x1, (byte) 0x1
        };

        final SerializerBuffer ser = new SerializerBuffer();
        classic.serialize(ser, Target.BYTE);
        final byte[] actual = ser.toByteArray();
        assertThat(actual, is(expected));
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
    void fixedModeByteSerialization() throws NoSuchTypeException, ValueSerializationException {

        final FixedPricingMode fixed = FixedPricingMode.builder()
                .gasPriceTolerance(153)
                .build();

        final byte[] expected = {
                (byte) 0x1,
                (byte) 0x99
        };

        final SerializerBuffer ser = new SerializerBuffer();
        fixed.serialize(ser, Target.BYTE);
        final byte[] actual = ser.toByteArray();
        assertThat(actual, is(expected));
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

    @Test
    void reservedModeByteSerialization() throws NoSuchTypeException, ValueSerializationException {

        final ReservedPricingMode reserved = ReservedPricingMode.builder()
                .receipt(new Digest("92f6e1d1421342165f14563c3a68661ee84614b340cd8584c39526eb397e6686"))
                .build();

        final byte[] expected = {(byte) 0x2, (byte) 0x92, (byte) 0xf6, (byte) 0xe1, (byte) 0xd1, (byte) 0x42,
                (byte) 0x13, (byte) 0x42, (byte) 0x16, (byte) 0x5f, (byte) 0x14, (byte) 0x56, (byte) 0x3c, (byte) 0x3a,
                (byte) 0x68, (byte) 0x66, (byte) 0x1e, (byte) 0xe8, (byte) 0x46, (byte) 0x14, (byte) 0xb3, (byte) 0x40,
                (byte) 0xcd, (byte) 0x85, (byte) 0x84, (byte) 0xc3, (byte) 0x95, (byte) 0x26, (byte) 0xeb, (byte) 0x39,
                (byte) 0x7e, (byte) 0x66, (byte) 0x86
        };

        final SerializerBuffer ser = new SerializerBuffer();
        reserved.serialize(ser, Target.BYTE);
        final byte[] actual = ser.toByteArray();
        assertThat(actual, is(expected));
    }
}
