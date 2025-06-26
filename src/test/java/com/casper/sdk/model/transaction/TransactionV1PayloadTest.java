package com.casper.sdk.model.transaction;

import com.casper.sdk.model.AbstractJsonTests;
import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.clvalue.CLValueU8;
import com.casper.sdk.model.clvalue.CLValueURef;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.entrypoint.CallEntryPoint;
import com.casper.sdk.model.transaction.entrypoint.CustomEntryPoint;
import com.casper.sdk.model.transaction.entrypoint.TransactionEntryPoint;
import com.casper.sdk.model.transaction.field.Fields;
import com.casper.sdk.model.transaction.pricing.FixedPricingMode;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.*;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Unit tests for the {@link TransactionV1Payload} class
 *
 * @author ian@meywood.com
 */
class TransactionV1PayloadTest extends AbstractJsonTests {

    @Test
    void payloadWithSessionJsonRoundTrip() throws Exception {
        final String json = IOUtils.toString(
                Objects.requireNonNull(getClass().getResource("/transaction-samples/payload-session.json")).openStream(),
                StandardCharsets.UTF_8
        );

        final TransactionV1Payload fromJson = new ObjectMapper().readValue(json, TransactionV1Payload.class);
        assertThat(fromJson.getInitiatorAddr().getAddress(), is(PublicKey.fromTaggedHexString("0184f6d260f4ee6869ddb36affe15456de6ae045278fa2f467bb677561ce0dad55")));
        assertThat(fromJson.getTimestamp(), is(new DateTime("2024-11-27T17:07:36.905Z").toDate()));
        assertThat(fromJson.getTtl(), is(Ttl.builder().ttl("30m").build()));
        assertThat(fromJson.getChainName(), is("casper-net-1"));
        assertThat(fromJson.getPricingMode(), is(instanceOf(FixedPricingMode.class)));
        assertThat(((FixedPricingMode) fromJson.getPricingMode()).getAdditionalComputationFactor(), is(0));
        assertThat(((FixedPricingMode) fromJson.getPricingMode()).getGasPriceTolerance(), is(1));

        final Fields fields = fromJson.getFields();
        assertThat(fields, is(notNullValue()));

        assertThat(fields.getArgs(), is(notNullValue()));
        assertThat(fields.getArgs().getArgs().get(0).getType(), is("name"));
        assertThat(fields.getArgs().getArgs().get(0).getClValue(), is(new CLValueString("Test")));
        assertThat(fields.getArgs().getArgs().get(1).getType(), is("symbol"));
        assertThat(fields.getArgs().getArgs().get(1).getClValue(), is(new CLValueString("test")));
        assertThat(fields.getArgs().getArgs().get(2).getType(), is("decimals"));
        assertThat(fields.getArgs().get("enable_mint_burn").getClValue(), is(new CLValueU8((byte) 1)));

        assertThat(fields.getEntryPoint(), is(instanceOf(CallEntryPoint.class)));
        assertThat(fields.getScheduling(), is(instanceOf(Standard.class)));
        assertThat(fields.getTarget(), is(instanceOf(Session.class)));

        final Session session = (Session) fields.getTarget();
        assertThat(session.isInstallUpgrade(), is(true));
        assertThat(session.getRuntime(), is(TransactionRuntime.VM_CASPER_V1));
      //  assertThat(session.getTransferredValue(), is(0L));
      //  assertThat(session.getSeed(), is(nullValue()));

        final String writtenJson = getPrettyJson(fromJson);
        JSONAssert.assertEquals(json, writtenJson, false);
    }

    @Test
    void payloadWithNativeJsonRoundTrip() throws Exception {
        final String json = IOUtils.toString(
                Objects.requireNonNull(getClass().getResource("/transaction-samples/payload-native.json")).openStream(),
                StandardCharsets.UTF_8
        );

        final TransactionV1Payload fromJson = new ObjectMapper().readValue(json, TransactionV1Payload.class);
        assertThat(fromJson, is(notNullValue()));

        final Fields fields = fromJson.getFields();
        assertThat(fields, is(notNullValue()));
        assertThat(fields.getEntryPoint(), is(instanceOf(TransactionEntryPoint.class)));
        assertThat(fields.getScheduling(), is(instanceOf(Standard.class)));
        assertThat(fields.getTarget(), is(instanceOf(Native.class)));

        final String writtenJson = getPrettyJson(fromJson);
        JSONAssert.assertEquals(json, writtenJson, false);
    }

    @Test
    void payloadWithStoredJsonRoundTrip() throws Exception {
        final String json = IOUtils.toString(
                Objects.requireNonNull(getClass().getResource("/transaction-samples/payload-stored.json")).openStream(),
                StandardCharsets.UTF_8
        );

        final TransactionV1Payload fromJson = new ObjectMapper().readValue(json, TransactionV1Payload.class);
        assertThat(fromJson, is(notNullValue()));

        final Fields fields = fromJson.getFields();
        assertThat(fields, is(notNullValue()));
        assertThat(fields.getEntryPoint(), is(instanceOf(CustomEntryPoint.class)));
        assertThat(fields.getScheduling(), is(instanceOf(Standard.class)));
        assertThat(fields.getTarget(), is(instanceOf(Stored.class)));
        assertThat(fields.getArgs().get("delegator_purse").getClValue(), is(new CLValueURef(URef.fromString("uref-de0ef381c12dc842e0872453d35aa4b49fa488c0427e68b7dd5ac63e151eae98-007"))));

        Stored stored = (Stored) fields.getTarget();
        assertThat(stored.getId(), is(instanceOf(ByHash.class)));
        assertThat(((ByHash) stored.getId()).getHashAddress(), is(new Digest("d39b360530484c6b3ed32e84a1198b368fe2bae3d5c9e8af6b0ae4d3eba73caa")));
        assertThat(stored.getRuntime(), is(TransactionRuntime.VM_CASPER_V1));
        assertThat(stored.getTransferredValue(), is(0L));

        final String writtenJson = getPrettyJson(fromJson);
        JSONAssert.assertEquals(json, writtenJson, false);
    }
}
