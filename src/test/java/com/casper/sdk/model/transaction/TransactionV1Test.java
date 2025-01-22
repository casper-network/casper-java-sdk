package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.CLValueList;
import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.clvalue.CLValueU256;
import com.casper.sdk.model.clvalue.CLValueU8;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.entrypoint.CallEntryPoint;
import com.casper.sdk.model.transaction.pricing.FixedPricingMode;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.model.transaction.target.TransactionRuntime;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

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
    public void name() throws NoSuchTypeException, ValueSerializationException, ValueDeserializationException {

        final byte[] expected = {0x6, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x0, 0x35, 0x0, 0x0, 0x0, 0x2,
                0x0, 0x3d, 0x0, 0x0, 0x0, 0x3, 0x0, 0x45, 0x0, 0x0, 0x0, 0x4, 0x0, 0x53, 0x0, 0x0, 0x0, 0x5, 0x0,
                0x7e, 0x0, 0x0, 0x0, (byte) 0x87, 0x1, 0x0, 0x0, 0x2, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
                0x1, 0x0, 0x1, 0x0, 0x0, 0x0, 0x21, 0x0, 0x0, 0x0, 0x1, (byte) 0x88, (byte) 0x89, (byte) 0xe2, 0x5d,
                0x78, (byte) 0x95, 0x35, 0x3a, (byte) 0x94, 0x76, 0x2a, (byte) 0xb1, (byte) 0xb1, (byte) 0xa3,
                (byte) 0xc4, (byte) 0xdd, 0x49, (byte) 0xa1, (byte) 0xe0, (byte) 0xc2, (byte) 0xd8, (byte) 0xe5,
                (byte) 0x9d, 0x59, (byte) 0x88, 0x36, (byte) 0xff, (byte) 0xf1, (byte) 0xe5, (byte) 0xf6,
                (byte) 0x89, 0x4c, (byte) 0xe9, (byte) 0x84, (byte) 0x83, (byte) 0x9b, (byte) 0x93, 0x1, 0x0, 0x0,
                (byte) 0xe8, 0x3, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0xa, 0x0, 0x0, 0x0, 0x63, 0x68, 0x61, 0x69, 0x6e,
                0x2d, 0x6e, 0x61, 0x6d, 0x65, 0x4, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x0, 0x1,
                0x0, 0x0, 0x0, 0x2, 0x0, 0x9, 0x0, 0x0, 0x0, 0x3, 0x0, 0xa, 0x0, 0x0, 0x0, 0xb, 0x0, 0x0, 0x0,
                0x0, (byte) 0xff, 0x1e, (byte) 0xd3, (byte) 0xfb, 0x6f, 0x52, (byte) 0x9c, 0x52, 0x1, 0x1, 0x4,
                0x0, 0x0, 0x0, 0x0, 0x0, (byte) 0xb2, 0x0, 0x0, 0x0, 0x2, 0x0, 0x0, 0x0, 0xc, 0x0, 0x0, 0x0,
                (byte) 0xf2, (byte) 0xae, (byte) 0xbc, (byte) 0xb8, (byte) 0xf2, (byte) 0xbe, (byte) 0x94,
                (byte) 0x90, (byte) 0xf2, (byte) 0xa2, (byte) 0x88, (byte) 0xb1, 0x29, 0x0, 0x0, 0x0, 0x25, 0x0,
                0x0, 0x0, (byte) 0xa5, 0x72, 0x20, (byte) 0xd8, 0x77, (byte) 0x99, 0x17, 0x4e, 0x0, (byte) 0xfd,
                (byte) 0xe4, 0x56, (byte) 0xdb, (byte) 0xaf, (byte) 0xca, 0x57, 0x35, (byte) 0xde, (byte) 0xd7,
                0x7d, (byte) 0xc0, 0x1c, (byte) 0x82, (byte) 0xbb, 0x5b, (byte) 0x96, 0x74, (byte) 0xe3,
                (byte) 0xc7, 0x6f, (byte) 0xe2, 0x66, (byte) 0xe3, (byte) 0xa1, (byte) 0xfb, (byte) 0xc1, 0x69,
                0xe, 0x3, 0x2b, 0x0, 0x0, 0x0, (byte) 0xf2, (byte) 0xa7, (byte) 0x8d, (byte) 0x94, (byte) 0xe2,
                (byte) 0x9e, (byte) 0x99, (byte) 0xf2, (byte) 0x99, (byte) 0x8f, (byte) 0xb4, (byte) 0xf1,
                (byte) 0xb8, (byte) 0x9b, (byte) 0x94, (byte) 0xf1, (byte) 0xa9, (byte) 0x93, (byte) 0x8c,
                (byte) 0xf2, (byte) 0xb4, (byte) 0xbd, (byte) 0x95, (byte) 0xf1, (byte) 0x93, (byte) 0x95,
                (byte) 0x8c, (byte) 0xf1, (byte) 0xb1, (byte) 0x92, (byte) 0xb0, (byte) 0xf3, (byte) 0xb4,
                (byte) 0xaf, (byte) 0x9c, (byte) 0xf4, (byte) 0x85, (byte) 0xb5, (byte) 0x8d, (byte) 0xf2,
                (byte) 0x91, (byte) 0x87, (byte) 0x83, 0x3a, 0x0, 0x0, 0x0, 0x36, 0x0, 0x0, 0x0, (byte) 0x84,
                (byte) 0xb5, (byte) 0xae, 0x1f, 0x3a, 0x65, (byte) 0xbe, 0x62, (byte) 0xd7, 0x1, 0x72, (byte) 0xf7,
                0x58, 0x56, 0x75, (byte) 0xbc, 0x7e, (byte) 0xdb, 0x7d, 0x55, (byte) 0xd8, 0x1, 0x64, (byte) 0xb4,
                0x3, 0x49, (byte) 0xcd, (byte) 0xee, (byte) 0xec, (byte) 0xd6, (byte) 0xa9, 0x13, 0x3c, (byte) 0xdf,
                0x44, (byte) 0xf9, (byte) 0xec, 0x49, (byte) 0x87, (byte) 0x95, (byte) 0x91, 0x62, (byte) 0xc7,
                (byte) 0xd8, 0x3a, 0x19, 0x15, (byte) 0x90, (byte) 0x9a, (byte) 0x87, (byte) 0xc8, 0x6d,
                (byte) 0xdf, (byte) 0xb7, 0xe, 0x3, 0x1, 0x0, 0xf, 0x0, 0x0, 0x0, 0x1, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0,
                0x0, 0x0, 0x0, 0x1, 0x0, 0x0, 0x0, 0x0, 0x2, 0x0, 0xf, 0x0, 0x0, 0x0, 0x1, 0x0, 0x0, 0x0, 0x0, 0x0,
                0x0, 0x0, 0x0, 0x0, 0x1, 0x0, 0x0, 0x0, 0xb, 0x3, 0x0, 0x1d, 0x0, 0x0, 0x0, 0x2, 0x0, 0x0, 0x0, 0x0,
                0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x0, 0x1, 0x0, 0x0, 0x0, 0x9, 0x0, 0x0, 0x0, 0x1, (byte) 0xf0, 0x47,
                0x8, 0x0, 0x0, 0x0, 0x0, 0x0
        };


        byte[] clListBytes = {
                0x25, 0x0, 0x0, 0x0, (byte) 0xa5, 0x72, 0x20, (byte) 0xd8, 0x77, (byte) 0x99, 0x17, 0x4e, 0x0,
                (byte) 0xfd, (byte) 0xe4, 0x56, (byte) 0xdb, (byte) 0xaf, (byte) 0xca, 0x57, 0x35, (byte) 0xde,
                (byte) 0xd7, 0x7d, (byte) 0xc0, 0x1c, (byte) 0x82, (byte) 0xbb, 0x5b, (byte) 0x96, 0x74, (byte) 0xe3,
                (byte) 0xc7, 0x6f, (byte) 0xe2, 0x66, (byte) 0xe3, (byte) 0xa1, (byte) 0xfb, (byte) 0xc1, 0x69
        };

        final List<NamedArg<?>> args = new ArrayList<>();
        CLValueList argZero = new CLValueList();
        DeserializerBuffer des = new DeserializerBuffer(clListBytes);
        argZero.deserialize(des, Target.JSON);

        args.add(new NamedArg<>("򮼸򾔐򢈱", argZero));
    }

    @Test
    public void transactionV1jsonRoundTrip() throws Exception {

        final String json = IOUtils.toString(getClass().getResource("/transaction-samples/transaction-v1.json").openStream(), StandardCharsets.UTF_8);
        final Transaction transaction = new ObjectMapper().readValue(json, Transaction.class);

        assertThat(transaction, is(notNullValue()));
        assertThat(transaction.get(), is(notNullValue(TransactionV1.class)));

        final TransactionV1 transactionV1 = transaction.getVersion1();

        assertThat(transactionV1.getHash(), is(new Digest("7ef4be88714ed23ae4d4a3f095612d638255c780a24f1fc4c6f57f7e6251f8bf")));

        assertThat(transactionV1.getPayload().getChainName(), is("casper-net-1"));
        assertThat(transactionV1.getPayload().getTtl(), is(Ttl.builder().ttl("30m").build()));
        assertThat(transactionV1.getPayload().getTimestamp(), is(new DateTime("2024-11-27T17:07:36.905Z").toDate()));
        assertThat(transactionV1.getPayload().getPricingMode(), is(instanceOf(FixedPricingMode.class)));
        assertThat(((FixedPricingMode) transactionV1.getPayload().getPricingMode()).getGasPriceTolerance(), is(1));
        assertThat(((FixedPricingMode) transactionV1.getPayload().getPricingMode()).getAdditionalComputationFactor(), is(0));

        assertThat(transactionV1.getPayload().getFields().getEntryPoint(), is(new CallEntryPoint()));
        assertThat(transactionV1.getPayload().getFields().getScheduling(), is(instanceOf(Standard.class)));
        assertThat(transactionV1.getPayload().getFields().getTarget(), is(instanceOf(Session.class)));
        assertThat(((Session) transactionV1.getPayload().getFields().getTarget()).isInstallUpgrade(), is(true));
        assertThat(((Session) transactionV1.getPayload().getFields().getTarget()).getRuntime(), is(TransactionRuntime.VM_CASPER_V1));
        assertThat(((Session) transactionV1.getPayload().getFields().getTarget()).getModuleBytes().length, is(348211));

        assertThat(transactionV1.getPayload().getFields().getArgs().getArgs(), hasSize(6));

        assertThat(transactionV1.getPayload().getFields().getArgs().get(0).getType(), is("name"));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(0).getClValue().getClType().getTypeName(), is("String"));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(0).getClValue(), is(instanceOf(CLValueString.class)));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(0).getClValue().getValue(), is("Test"));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(0).getClValue().getBytes(), is("0400000054657374"));

        assertThat(transactionV1.getPayload().getFields().getArgs().get(3).getType(), is("total_supply"));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(3).getClValue().getClType().getTypeName(), is("U256"));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(3).getClValue(), is(instanceOf(CLValueU256.class)));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(3).getClValue().getValue(), is(new BigInteger("1000000000000000")));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(3).getClValue().getBytes(), is("070080c6a47e8d03"));

        assertThat(transactionV1.getPayload().getFields().getArgs().get(5).getType(), is("enable_mint_burn"));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(5).getClValue().getClType().getTypeName(), is("U8"));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(5).getClValue(), is(instanceOf(CLValueU8.class)));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(5).getClValue().getValue(), is((byte) 1));
        assertThat(transactionV1.getPayload().getFields().getArgs().get(5).getClValue().getBytes(), is("01"));

        assertThat(transactionV1.getApprovals(), hasSize(1));
        assertThat(transactionV1.getApprovals().get(0).getSignature().getAlgoTaggedHex(), is("01a96e46ae1efbcf43e853c198568e25435ac8510bbe02c3cb22f25ce384db0d3fe9a1ce21ca30cc5abf40f03305db15d10fe5defbba332dd4a0ae7bbb07447c0c"));
        assertThat(transactionV1.getApprovals().get(0).getSigner(), is(PublicKey.fromTaggedHexString("0184f6d260f4ee6869ddb36affe15456de6ae045278fa2f467bb677561ce0dad55")));

        final String writtenJson = new ObjectMapper().writeValueAsString(transaction);
        JSONAssert.assertEquals(json, writtenJson, false);
    }


    @Test
    @Disabled
    void transactionV1StoredContractRoundTrip() throws IOException, JSONException {

        final String json = IOUtils.toString(getClass().getResource("/transaction-samples/transaction-v1-stored-contract.json").openStream(), StandardCharsets.UTF_8);
        final Transaction transaction = new ObjectMapper().readValue(json, Transaction.class);

        assertThat(transaction, is(notNullValue()));
        assertThat(transaction.get(), is(notNullValue(TransactionV1.class)));

        final TransactionV1 transactionV1 = transaction.getVersion1();

        assertThat(transactionV1.getHash(), is(new Digest("2b49844436a02422b60b22dbfcbc5be3d3d86491fb556cc405f48b8e48342457")));
        // assertThat(transactionV1.getPayload().getTransactiot is(TransactionCategory.INSTALL_UPGRADE));
        assertThat(transactionV1.getPayload().getFields().getScheduling(), is(instanceOf(Standard.class)));
        assertThat(transactionV1.getPayload().getFields().getArgs().size(), is(6));
        assertThat(((Session) transactionV1.getPayload().getFields().getTarget()).getRuntime(), is(TransactionRuntime.VM_CASPER_V2));
        assertThat(transactionV1.getPayload().getFields().getEntryPoint().getName(), is("Call"));

        final String writtenJson = new ObjectMapper().writeValueAsString(transaction);
        JSONAssert.assertEquals(json, writtenJson, false);
    }
}
