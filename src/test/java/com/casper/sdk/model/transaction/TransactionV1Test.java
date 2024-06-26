package com.casper.sdk.model.transaction;

import com.casper.sdk.model.clvalue.CLValueOption;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.CLValueURef;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.pricing.FixedPricingMode;
import com.casper.sdk.model.transaction.scheduling.FutureTimestamp;
import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests the {@link TransactionV1} class.
 *
 * @author ian@meywood.com
 */
class TransactionV1Test {

    @Test
    public void transactionV1jsonRoundTrip() throws Exception {

        final String json = IOUtils.toString(getClass().getResource("/transaction-samples/transaction-v1.json").openStream(), StandardCharsets.UTF_8);

        final TransactionV1 transactionV1 = new ObjectMapper().readValue(json, TransactionV1.class);

        assertThat(transactionV1, is(notNullValue()));

        assertThat(transactionV1.getHash(), is(new Digest("b7188ee3a749e6504d11708e23e41d16d98bdb2359d413a3bb84b48b5cc215d4")));

        assertThat(transactionV1.getHeader().getChainName(), is("󶻒󕁊򩢰🾎𝁦򃎍󕑒򄆤"));
        assertThat(transactionV1.getHeader().getTtl(), is(Ttl.builder().ttl("10h 14s 452ms").build()));
        assertThat(transactionV1.getHeader().getBodyHash(), is(new Digest("23d8f4cea16a6372ee7955e3fcf73715a564902bc903652377fbb7753f97552a")));
        assertThat(transactionV1.getHeader().getTimestamp(), is(new DateTime("2020-08-07T01:24:55.700Z").toDate()));
        assertThat(transactionV1.getHeader().getPricingMode(), is(instanceOf(FixedPricingMode.class)));
        assertThat(((FixedPricingMode) transactionV1.getHeader().getPricingMode()).getGasPriceTolerance(), is(5));

        assertThat(transactionV1.getBody().getTransactionCategory(), is(TransactionCategory.STANDARD));
        assertThat(transactionV1.getBody().getEntryPoint(), is("Transfer"));
        assertThat(transactionV1.getBody().getTarget(), is(new Native()));
        assertThat(transactionV1.getBody().getScheduling(), is(instanceOf(FutureTimestamp.class)));
        assertThat(((FutureTimestamp) transactionV1.getBody().getScheduling()).asDate(), is(new DateTime("2020-08-07T01:20:22.207Z").toDate()));

        assertThat(transactionV1.getBody().getArgs(), hasSize(3));

        assertThat(transactionV1.getBody().getArgs().get(0).getType(), is("source"));
        assertThat(transactionV1.getBody().getArgs().get(0).getClValue().getClType().getTypeName(), is("Option"));
        assertThat(transactionV1.getBody().getArgs().get(0).getClValue(), is(instanceOf(CLValueOption.class)));
        assertThat(((Optional) transactionV1.getBody().getArgs().get(0).getClValue().getValue()).get(), is(instanceOf(CLValueURef.class)));
        assertThat(((CLValueURef) ((Optional) transactionV1.getBody().getArgs().get(0).getClValue().getValue()).get()).getValue(), is(URef.fromString("uref-7e52a4cb1d0288a84e19ea6d3d277cd78f3e8c438b36c65045860d56d96eee1b-001")));
        assertThat(transactionV1.getBody().getArgs().get(0).getClValue().getBytes(), is("017e52a4cb1d0288a84e19ea6d3d277cd78f3e8c438b36c65045860d56d96eee1b01"));

        assertThat(transactionV1.getBody().getArgs().get(1).getType(), is("target"));
        assertThat(transactionV1.getBody().getArgs().get(1).getClValue().getClType().getTypeName(), is("URef"));
        assertThat(transactionV1.getBody().getArgs().get(1).getClValue(), is(instanceOf(CLValueURef.class)));
        assertThat(transactionV1.getBody().getArgs().get(1).getClValue().getValue(), is(URef.fromString("uref-8a99d6bcfe1563f0ec85b9ca24386ddbd0975601d3bba64c2110b1a0ac9799d2-005")));
        assertThat(transactionV1.getBody().getArgs().get(1).getClValue().getBytes(), is("8a99d6bcfe1563f0ec85b9ca24386ddbd0975601d3bba64c2110b1a0ac9799d205"));

        assertThat(transactionV1.getBody().getArgs().get(2).getType(), is("amount"));
        assertThat(transactionV1.getBody().getArgs().get(2).getClValue().getClType().getTypeName(), is("U512"));
        assertThat(transactionV1.getBody().getArgs().get(2).getClValue(), is(instanceOf(CLValueU512.class)));
        assertThat(transactionV1.getBody().getArgs().get(2).getClValue().getValue(), is(new BigInteger("11501403317070719246")));
        assertThat(transactionV1.getBody().getArgs().get(2).getClValue().getBytes(), is("080ee97f3c61319d9f"));

        assertThat(transactionV1.getApprovals(), hasSize(1));
        assertThat(transactionV1.getApprovals().get(0).getSignature().getAlgoTaggedHex(), is("0299741626a3360a315ef0dc7b8ce91183928bc3267ac6a87688458948bc95bb92569f777d9335234ee2a9e657f81cdf1ca8c4df41a3b292a81d5b2b9e64277417"));
        assertThat(transactionV1.getApprovals().get(0).getSigner(), is(PublicKey.fromTaggedHexString("02023f63b32437c2f964e1d1951060ad63e7d26c0ab921e359d9900743db940a9298")));

        final String writtenJson = new ObjectMapper().writeValueAsString(transactionV1);
        JSONAssert.assertEquals(json, writtenJson, false);
    }
}
