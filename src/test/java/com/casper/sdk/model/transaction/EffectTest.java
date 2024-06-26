package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.model.bid.BidKind;
import com.casper.sdk.model.bid.Bridge;
import com.casper.sdk.model.bid.Delegator;
import com.casper.sdk.model.bid.ValidatorCredit;
import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.execution.Effect;
import com.casper.sdk.model.transaction.kind.IdentityKind;
import com.casper.sdk.model.transaction.kind.PruneKind;
import com.casper.sdk.model.transaction.kind.WriteKind;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Unit tests for {@link Effect} and Kind implementations.
 *
 * @author ian@meywood.com
 */
class EffectTest {

    @Test
    void pruneKindEffect() throws JsonProcessingException {
        final String json = "{\n" +
                "  \"key\": \"balance-hold-01fe139a5aa36aa69c04a6b630c9993bc03d868ffde46d3f60c3fbe6e6e762016f78bec10c90010000\",\n" +
                "  \"kind\": {\n" +
                "    \"Prune\": \"balance-hold-01fe139a5aa36aa69c04a6b630c9993bc03d868ffde46d3f60c3fbe6e6e762016f78bec10c90010000\"\n" +
                "  }\n" +
                "}";

        final Effect pruneEffect = new ObjectMapper().readValue(json, Effect.class);
        assertThat(pruneEffect.getKey(), is("balance-hold-01fe139a5aa36aa69c04a6b630c9993bc03d868ffde46d3f60c3fbe6e6e762016f78bec10c90010000"));
        assertThat(pruneEffect, is(notNullValue()));
        assertThat(pruneEffect.getKind(), is(instanceOf(PruneKind.class)));
        assertThat(((PruneKind) pruneEffect.getKind()).getPrune(), is("balance-hold-01fe139a5aa36aa69c04a6b630c9993bc03d868ffde46d3f60c3fbe6e6e762016f78bec10c90010000"));
    }

    @Test
    void writeKindCLValueEffect() throws JsonProcessingException {
        final String json = "{\n" +
                "  \"key\": \"balance-hold-01fe139a5aa36aa69c04a6b630c9993bc03d868ffde46d3f60c3fbe6e6e762016f78bec10c90010000\",\n" +
                "  \"kind\": {\n" +
                "    \"Write\": {\n" +
                "      \"CLValue\": {\n" +
                "        \"cl_type\": \"U512\",\n" +
                "        \"bytes\": \"021027\",\n" +
                "        \"parsed\": \"10000\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}"; // json

        final Effect writeEffect = new ObjectMapper().readValue(json, Effect.class);
        assertThat(writeEffect, is(notNullValue()));
        assertThat(writeEffect.getKind(), is(instanceOf(WriteKind.class)));

        //noinspection rawtypes
        final AbstractCLValue value = (AbstractCLValue) ((WriteKind) writeEffect.getKind()).getWrite().getValue();
        assertThat(value.getClType().getTypeName(), is("U512"));
        assertThat(value.getValue(), is(new BigInteger("10000")));
        assertThat(value.getBytes(), is("021027"));
        assertThat(value.getParsed(), is("10000"));
    }

    @Test
    void writeKindBidKindCreditEffect() throws JsonProcessingException, NoSuchAlgorithmException {
        final String json = "{\n" +
                "  \"key\": \"bid-addr-043bb47b19ea663fe884d9e474f6420c2fd681d3b60d18ace600067f9b8d8fd0867901000000000000\",\n" +
                "  \"kind\": {\n" +
                "    \"Write\": {\n" +
                "      \"BidKind\": {\n" +
                "        \"Credit\": {\n" +
                "          \"validator_public_key\": \"01284a8cd097808afdea9876ccb91f28702143c04266dea057cc19b8fc382cc258\",\n" +
                "          \"era_id\": 377,\n" +
                "          \"amount\": \"10000\"\n" +
                "        }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        final Effect writeEffect = new ObjectMapper().readValue(json, Effect.class);
        assertThat(writeEffect, is(notNullValue()));
        assertThat(writeEffect.getKind(), is(instanceOf(WriteKind.class)));
        WriteKind<?> kind = writeEffect.getKind();
        assertThat(kind.getWrite().getValue(), is(instanceOf(BidKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(BidKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(ValidatorCredit.class)));
        ValidatorCredit value = (ValidatorCredit) kind.getWrite().getValue();
        assertThat(value.getValidatorPublicKey(), is(PublicKey.fromTaggedHexString("01284a8cd097808afdea9876ccb91f28702143c04266dea057cc19b8fc382cc258")));
        assertThat(value.getEraId(), is(377L));
        assertThat(value.getAmount(), is(new BigInteger("10000")));
    }

    @Test
    void writeKindBidKindBridgeEffect() throws JsonProcessingException, NoSuchAlgorithmException {
        final String json = "{\n" +
                "   \"key\": \"bid-addr-043bb47b19ea663fe884d9e474f6420c2fd681d3b60d18ace600067f9b8d8fd0867901000000000000\",\n" +
                "   \"kind\": {\n" +
                "     \"Write\": {\n" +
                "       \"BidKind\": {\n" +
                "         \"Bridge\": {\n" +
                "           \"old_validator_public_key\": \"01284a8cd097808afdea9876ccb91f28702143c04266dea057cc19b8fc382cc258\",\n" +
                "           \"new_validator_public_key\": \"01026ca707c348ed8012ac6a1f28db031fadd6eb67203501a353b867a08c8b9a80\",\n" +
                "           \"era_id\": 378\n" +
                "         }\n" +
                "       }\n" +
                "     }\n" +
                "   }\n" +
                " }";

        final Effect writeEffect = new ObjectMapper().readValue(json, Effect.class);
        assertThat(writeEffect, is(notNullValue()));
        assertThat(writeEffect.getKind(), is(instanceOf(WriteKind.class)));
        WriteKind<?> kind = writeEffect.getKind();
        assertThat(kind.getWrite().getValue(), is(instanceOf(BidKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(BidKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(Bridge.class)));
        Bridge value = (Bridge) kind.getWrite().getValue();
        assertThat(value.getOldValidatorPublicKey(), is(PublicKey.fromTaggedHexString("01284a8cd097808afdea9876ccb91f28702143c04266dea057cc19b8fc382cc258")));
        assertThat(value.getNewValidatorPublicKey(), is(PublicKey.fromTaggedHexString("01026ca707c348ed8012ac6a1f28db031fadd6eb67203501a353b867a08c8b9a80")));
        assertThat(value.getEraId(), is(378L));
    }

    @Test
    void writeKindBidKindDelegatorEffect() throws IOException, NoSuchAlgorithmException, DynamicInstanceException {

        final String json = "{\n" +
                "  \"key\": \"bid-addr-043bb47b19ea663fe884d9e474f6420c2fd681d3b60d18ace600067f9b8d8fd0867901000000000000\",\n" +
                "  \"kind\": {\n" +
                "    \"Write\": {\n" +
                "      \"BidKind\": {\n" +
                "         \"Delegator\": {\n" +
                "            \"delegator_public_key\": \"01128ddb51119f1df535cf3a763996344ab0cc79038faaee0aaaf098a078031ce6\",\n" +
                "            \"validator_public_key\": \"01026ca707c348ed8012ac6a1f28db031fadd6eb67203501a353b867a08c8b9a80\",\n" +
                "            \"staked_amount\": \"29519451635\",\n" +
                "            \"bonding_purse\": \"uref-39a227259f033ce388e529c0330a6a966d591b567c09859f9390af4787f38d39-007\",\n" +
                "            \"vesting_schedule\" : {\n" +
                "              \"initial_release_timestamp_millis\" : 1624978800000,\n" +
                "              \"locked_amounts\" : [\n" +
                "                \"7451869990600886\",\n" +
                "                \"6878649222093126\",\n" +
                "                \"6305428453585366\",\n" +
                "                \"5732207685077606\",\n" +
                "                \"5158986916569846\",\n" +
                "                \"4585766148062086\",\n" +
                "                \"4012545379554326\",\n" +
                "                \"3439324611046566\",\n" +
                "                \"2292883074031046\",\n" +
                "                \"1719662305523286\",\n" +
                "                \"1146441537015526\",\n" +
                "                \"573220768507766\",\n" +
                "                \"0\"\n" +
                "              ]\n" +
                "            }\n" +
                "         }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        final Effect writeEffect = new ObjectMapper().readValue(json, Effect.class);
        assertThat(writeEffect, is(notNullValue()));
        assertThat(writeEffect.getKind(), is(instanceOf(WriteKind.class)));
        WriteKind<?> kind = writeEffect.getKind();
        assertThat(kind.getWrite().getValue(), is(instanceOf(BidKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(BidKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(Delegator.class)));
        final Delegator value = (Delegator) kind.getWrite().getValue();
        assertThat(value.getDelegatorPublicKey(), is(PublicKey.fromTaggedHexString("01128ddb51119f1df535cf3a763996344ab0cc79038faaee0aaaf098a078031ce6")));
        assertThat(value.getValidatorPublicKey(), is(PublicKey.fromTaggedHexString("01026ca707c348ed8012ac6a1f28db031fadd6eb67203501a353b867a08c8b9a80")));
        assertThat(value.getBondingPurse(), is(URef.fromString("uref-39a227259f033ce388e529c0330a6a966d591b567c09859f9390af4787f38d39-007")));
        assertThat(value.getStakedAmount(), is(new BigInteger("29519451635")));
        assertThat(value.getVestingSchedule().getLockedAmounts(), hasSize(13));
        assertThat(value.getVestingSchedule().getLockedAmounts().get(0), is(new BigInteger("7451869990600886")));
        assertThat(value.getVestingSchedule().getLockedAmounts().get(12), is(new BigInteger("0")));
        assertThat(value.getVestingSchedule().getInitialReleaseTimeStampMillis(), is(new BigInteger("1624978800000")));
    }

    @Test
    void identityKindEffect() throws JsonProcessingException {
        final String json = "{\n" +
                "  \"key\": \"entity-system-86c4525a60cb6532342f5f598666711219f3bdcc6a8936152ec1c670c510c75f\",\n" +
                "  \"kind\": \"Identity\"\n" +
                "}";

        final Effect identityEffect = new ObjectMapper().readValue(json, Effect.class);
        assertThat(identityEffect.getKey(), is("entity-system-86c4525a60cb6532342f5f598666711219f3bdcc6a8936152ec1c670c510c75f"));
        assertThat(identityEffect, is(notNullValue()));
        assertThat(identityEffect.getKind(), is(instanceOf(IdentityKind.class)));
    }
}
