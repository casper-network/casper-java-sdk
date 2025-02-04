package com.casper.sdk.model.transaction;

import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.clvalue.CLValueU256;
import com.casper.sdk.model.clvalue.CLValueU8;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.entrypoint.CallEntryPoint;
import com.casper.sdk.model.transaction.entrypoint.CustomEntryPoint;
import com.casper.sdk.model.transaction.pricing.FixedPricingMode;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.Stored;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.model.transaction.target.TransactionRuntime;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.json.JSONException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

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


    @Test
    void getTransactionStored() throws Exception {

        // The expected payload bytes from which the hash is calculated
        final String expectedHex = "0600000000000000000001003600000002003e0000000300460000000400560000000500810000008" +
                "c01000002000000000000000000010001000000220000000001a5a5b7328118681638be3e06c8749609280dba4c9daf9aeb3" +
                "d3464b8839b018a4ed301d99301000040771b00000000000c0000006361737065722d6e65742d31040000000000000000000" +
                "1000100000002000900000003000a0000000b000000000010a5d4e8000000010104000000000098000000000600000004000" +
                "0006e616d650f0000000b000000434c49434b5420546573740a0600000073796d626f6c0a00000006000000434c49434b540" +
                "a08000000646563696d616c730100000009030c000000746f74616c5f737570706c7908000000070080c6a47e8d03070b000" +
                "0006576656e74735f6d6f646501000000020310000000656e61626c655f6d696e745f6275726e01000000010301003900000" +
                "0040000000000000000000100010000000200020000000300110000001900000002010100000000000000000001000000000" +
                "40000000102030402000f00000001000000000000000000010000000003000f000000010000000000000000000100000000";

        final String json = IOUtils.toString(getClass().getResource("/transaction-samples/get-transaction-stored-v200.json").openStream(), StandardCharsets.UTF_8);
        final Transaction transaction = new ObjectMapper().readValue(json, Transaction.class);

        assertThat(transaction, is(notNullValue()));
        assertThat(transaction.get(), is(notNullValue(TransactionV1.class)));

        final TransactionV1 transactionV1 = transaction.getVersion1();

        assertThat(transactionV1.getHash(), is(new Digest("173251b375edd42e3ee43a05ff7d1996380c59192a785884a8cfdd574d86aa9a")));
        // assertThat(transactionV1.getPayload().getTransactiot is(TransactionCategory.INSTALL_UPGRADE));
        assertThat(transactionV1.getPayload().getChainName(), is("casper-net-1"));
        assertThat(transactionV1.getPayload().getTimestamp(), is(new DateTime("2024-12-18T10:27:48.633Z").toDate()));
        assertThat(transactionV1.getPayload().getInitiatorAddr().getAddress(), is(PublicKey.fromTaggedHexString("01a5a5b7328118681638be3e06c8749609280dba4c9daf9aeb3d3464b8839b018a")));
        assertThat(transactionV1.getPayload().getFields().getScheduling(), is(instanceOf(Standard.class)));
        assertThat(transactionV1.getPayload().getFields().getArgs().size(), is(2));
        assertThat(((Stored) transactionV1.getPayload().getFields().getTarget()).getRuntime(), is(TransactionRuntime.VM_CASPER_V1));
        assertThat(transactionV1.getPayload().getFields().getEntryPoint().getName(), is("Custom"));
        assertThat(((CustomEntryPoint) transactionV1.getPayload().getFields().getEntryPoint()).getCustom(), is("transfer"));

        // Assert JSON round trip is valid
        final String writtenJson = new ObjectMapper().writeValueAsString(transaction);
        JSONAssert.assertEquals(json, writtenJson, false);

        // Recalculate the hash and check it matches the one provided in the JSON
        transactionV1.calculateHash();
        assertThat(transactionV1.getHash(), is(new Digest("173251b375edd42e3ee43a05ff7d1996380c59192a785884a8cfdd574d86aa9a")));
    }
}
