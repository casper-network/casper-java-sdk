package com.casper.sdk.model.deploy;

import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

/**
 * Unit tests for the {@link SeigniorageAllocation} classes
 *
 * @author ian@meywood.com
 */
class SeigniorageAllocationTest {

    @Test
    void delegatorKindSeigniorageAllocation() throws Exception {

        final String json = "{\n" +
                "  \"DelegatorKind\": {\n" +
                "    \"delegator_kind\": {\n" +
                "      \"PublicKey\": \"01af79f28bde4522b27edb8dc9df146c9d3a65f944bbdcf153ea107b291bae232d\"\n" +
                "    },\n" +
                "    \"validator_public_key\": \"016c07fa407f46f7381bb424fbe18c320e8184a7d3e4360acb38d2d58172a9028a\",\n" +
                "    \"amount\": \"6435000000000001\"\n" +
                "  }\n" +
                "}";

        final SeigniorageAllocation seigniorageAllocation = new ObjectMapper().readValue(json, SeigniorageAllocation.class);
        assertThat(seigniorageAllocation, is(instanceOf(DelegatorKindAllocation.class)));
        assertThat(seigniorageAllocation.getAmount(), is(new BigInteger("6435000000000001")));

        assertThat(
                ((DelegatorKindAllocation) seigniorageAllocation).getValidatorPublicKey(),
                is(PublicKey.fromTaggedHexString("016c07fa407f46f7381bb424fbe18c320e8184a7d3e4360acb38d2d58172a9028a"))
        );

        assertThat(
                ((DelegatorKindAllocation) seigniorageAllocation).getDelegatorKind(),
                is(instanceOf(DelegatorKindPublicKey.class))
        );

        assertThat(
                ((DelegatorKindPublicKey) ((DelegatorKindAllocation) seigniorageAllocation).getDelegatorKind()).getPublicKey(),
                is(PublicKey.fromTaggedHexString("01af79f28bde4522b27edb8dc9df146c9d3a65f944bbdcf153ea107b291bae232d"))
        );
    }

    @Test
    void validatorSeigniorageAllocation() throws Exception {
        final String json = " {\n" +
                "  \"Validator\": {\n" +
                "    \"validator_public_key\": \"01f0aaadbf1ef00a83e161eaccadaf4e499d9730a09e8979cb9e8c2d3fc4d8b6f7\",\n" +
                "    \"amount\": \"208000000000000\"\n" +
                "  }\n" +
                "}\n";

        final SeigniorageAllocation seigniorageAllocation = new ObjectMapper().readValue(json, SeigniorageAllocation.class);
        assertThat(seigniorageAllocation, is(instanceOf(Validator.class)));
        assertThat(seigniorageAllocation.getAmount(), is(new BigInteger("208000000000000")));
        assertThat(
                ((Validator) seigniorageAllocation).getValidatorPublicKey(),
                is(PublicKey.fromTaggedHexString("01f0aaadbf1ef00a83e161eaccadaf4e499d9730a09e8979cb9e8c2d3fc4d8b6f7"))
        );
    }


    @Test
    void delegatorSeigniorageAllocation() throws Exception {

        final String json = "{\n" +
                "  \"Delegator\": {\n" +
                "    \"delegator_public_key\": \"01af79f28bde4522b27edb8dc9df146c9d3a65f944bbdcf153ea107b291bae232d\",\n" +
                "    \"validator_public_key\": \"016c07fa407f46f7381bb424fbe18c320e8184a7d3e4360acb38d2d58172a9028a\",\n" +
                "    \"amount\": \"6435000000000001\"\n" +
                "  }\n" +
                "}";

        final SeigniorageAllocation seigniorageAllocation = new ObjectMapper().readValue(json, SeigniorageAllocation.class);
        assertThat(seigniorageAllocation, is(instanceOf(Delegator.class)));
        assertThat(seigniorageAllocation.getAmount(), is(new BigInteger("6435000000000001")));

        assertThat(
                ((Delegator) seigniorageAllocation).getValidatorPublicKey(),
                is(PublicKey.fromTaggedHexString("016c07fa407f46f7381bb424fbe18c320e8184a7d3e4360acb38d2d58172a9028a"))
        );

        assertThat(
                ((Delegator) seigniorageAllocation).getDelegatorPublicKey(),
                is(PublicKey.fromTaggedHexString("01af79f28bde4522b27edb8dc9df146c9d3a65f944bbdcf153ea107b291bae232d"))
        );
    }

}
