package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.model.bid.BidKind;
import com.casper.sdk.model.bid.Bridge;
import com.casper.sdk.model.bid.Delegator;
import com.casper.sdk.model.bid.ValidatorCredit;
import com.casper.sdk.model.clvalue.AbstractCLValue;
import com.casper.sdk.model.contract.entrypoint.EntryPoint;
import com.casper.sdk.model.contract.entrypoint.EntryPointValue;
import com.casper.sdk.model.deploy.transform.Transform;
import com.casper.sdk.model.entity.Entity;
import com.casper.sdk.model.entity.SmartContract;
import com.casper.sdk.model.entity.contract.ByteCode;
import com.casper.sdk.model.entity.contract.NamedKey;
import com.casper.sdk.model.entity.contract.Package;
import com.casper.sdk.model.key.*;
import com.casper.sdk.model.transaction.execution.Effect;
import com.casper.sdk.model.transaction.kind.*;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.encdec.Hex;
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
        assertThat(pruneEffect.getKey(), is(instanceOf(BalanceHoldKey.class)));
        assertThat(pruneEffect.getKey().toString(), is("balance-hold-01fe139a5aa36aa69c04a6b630c9993bc03d868ffde46d3f60c3fbe6e6e762016f78bec10c90010000"));
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
        final WriteKind<?> kind = writeEffect.getKind();
        assertThat(kind.getWrite().getValue(), is(instanceOf(BidKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(BidKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(ValidatorCredit.class)));
        final ValidatorCredit value = (ValidatorCredit) kind.getWrite().getValue();
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
        final WriteKind<?> kind = writeEffect.getKind();
        assertThat(kind.getWrite().getValue(), is(instanceOf(BidKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(BidKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(Bridge.class)));
        final Bridge value = (Bridge) kind.getWrite().getValue();
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
        assertThat(identityEffect.getKey(), is(instanceOf(AddressableEntityKey.class)));
        assertThat(identityEffect.getKey().toString(), is("entity-system-86c4525a60cb6532342f5f598666711219f3bdcc6a8936152ec1c670c510c75f"));
        assertThat(identityEffect, is(notNullValue()));
        assertThat(identityEffect.getKind(), is(instanceOf(IdentityKind.class)));
    }

    @Test
    void identityKindAddUint512() throws JsonProcessingException {

        final String json = "{\n" +
                "  \"key\": \"balance-1c29560834540520e147468c3bf86f09e5bd60cda2cb8380d94ee1c348a4281d\",\n" +
                "  \"kind\": {\n" +
                "  \"AddUInt512\" : 2500000000\n" +
                "}\n" +
                "}";

        final Effect identityEffect = new ObjectMapper().readValue(json, Effect.class);
        assertThat(identityEffect.getKey(), is(instanceOf(Key.class)));
        assertThat(identityEffect.getKey().toString(), is("balance-1c29560834540520e147468c3bf86f09e5bd60cda2cb8380d94ee1c348a4281d"));
        assertThat(identityEffect, is(notNullValue()));
        assertThat(identityEffect.getKind(), is(instanceOf(Transform.class)));
    }

    @Test
    void writeKindByteCode() throws JsonProcessingException {

        final String json = "{\n" +
                "  \"key\": \"byte-code-v1-wasm-3beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a\",\n" +
                "  \"kind\": {\n" +
                "    \"Write\": {\n" +
                "      \"ByteCode\": {\n" +
                "         \"kind\": \"V1CasperWasm\",\n" +
                "         \"bytes\": \"0061736d01000000017f106002\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        final Effect writeEffect = new ObjectMapper().readValue(json, Effect.class);
        assertThat(writeEffect.getKey().toString(), is("byte-code-v1-wasm-3beb396c91ff7ae62d08857cc8a787146cd4f0771b8a21d385b3f4ac6077854a"));

        final WriteKind<?> kind = writeEffect.getKind();
        assertThat(kind.getWrite(), is(instanceOf(ByteCodeKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(ByteCode.class)));

        final ByteCode byteCodeKind = (ByteCode) kind.getWrite().getValue();
        assertThat(byteCodeKind.getBytes(), is("0061736d01000000017f106002"));
        assertThat(byteCodeKind.getKind(), is(ByteCode.ByteCodes.V1CasperWasm));
    }

    @Test
    void writeKindNamedKey() throws JsonProcessingException {

        final String json = "{\n" +
                "  \"key\": \"named-key-entity-account-a897e2e1c25b1149e96b774bdfd758d4a44ec392bd914a19ff780d3905ff45c0-e8265cd5ef8e0275971c6d4ff28263ef787412f034631a11bbc508761ffd7119\",\n" +
                "  \"kind\": {\n" +
                "    \"Write\": {\n" +
                "      \"NamedKey\": {\n" +
                "           \"named_key\":" +
                "              {\n" +
                "                   \"cl_type\": \"Key\", \n" +
                "                   \"bytes\": \"028c5da3dd186f82c27dd4689dca3f838ac17f93ef10a5159fa40a30ca78f9e93207\",\n" +
                "                   \"parsed\": \"uref-8c5da3dd186f82c27dd4689dca3f838ac17f93ef10a5159fa40a30ca78f9e932-007\" \n" +
                "               },\n" +
                "           \"name\":" +
                "              {\n" +
                "                   \"cl_type\": \"String\", \n" +
                "                   \"bytes\": \"2800000063657031385f636f6e74726163745f7061636b6167655f6163636573735f41636d6520546f6b656e\", \n" +
                "                   \"parsed\": \"cep18_contract_package_access_Acme Token\" \n" +
                "               }\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        final Effect writeEffect = new ObjectMapper().readValue(json, Effect.class);

        assertThat(writeEffect.getKey(), is(instanceOf(NamedKeyKey.class)));
        assertThat(writeEffect.getKey().toString(), is("named-key-entity-account-a897e2e1c25b1149e96b774bdfd758d4a44ec392bd914a19ff780d3905ff45c0-e8265cd5ef8e0275971c6d4ff28263ef787412f034631a11bbc508761ffd7119"));

        final WriteKind<?> kind = writeEffect.getKind();
        assertThat(kind.getWrite(), is(instanceOf(NamedKeyKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(NamedKey.class)));

        final NamedKey namedKeyKind = (NamedKey) kind.getWrite().getValue();
        final AbstractCLValue<?, ?> namedKey = namedKeyKind.getNamedKey();
        assertThat(namedKey.getBytes(), is("028c5da3dd186f82c27dd4689dca3f838ac17f93ef10a5159fa40a30ca78f9e93207"));
        assertThat(namedKey.getParsed(), is("uref-8c5da3dd186f82c27dd4689dca3f838ac17f93ef10a5159fa40a30ca78f9e932-007"));
        assertThat(namedKey.getClType().getTypeName(), is("Key"));

        final AbstractCLValue<?, ?> name = namedKeyKind.getName();
        assertThat(name.getBytes(), is("2800000063657031385f636f6e74726163745f7061636b6167655f6163636573735f41636d6520546f6b656e"));
        assertThat(name.getParsed(), is("cep18_contract_package_access_Acme Token"));
        assertThat(name.getClType().getTypeName(), is("String"));
    }

    @Test
    void writeKindEntryPoint() throws JsonProcessingException {

        final String json = "{\n" +
                "  \"key\": \"entry-point-v1-entity-contract-3b6b4d8a3d815372508faa92f3a05dcb50c9c98de05d9a7668cb94b04f1ef9af-768c370eb010604bd19029a409dca8b5fbf9af9bc14a36c2b294a2a7a922161e\",\n" +
                "  \"kind\": {\n" +
                "    \"Write\": {\n" +
                "      \"EntryPoint\": {\n" +
                "        \"V1CasperVm\": {\n" +
                "           \"name\": \"burn\", \n" +
                "           \"args\": [\n" +
                "              {\n" +
                "                   \"name\": \"owner\", \n" +
                "                   \"cl_type\": \"Key\" \n" +
                "               },\n" +
                "               { \n" +
                "                   \"name\": \"amount\", \n" +
                "                   \"cl_type\": \"U256\" \n" +
                "               } \n" +
                "            ],\n" +
                "           \"ret\": \"Unit\"," +
                "           \"access\": \"Public\"," +
                "           \"entry_point_type\": \"Called\"," +
                "           \"entry_point_payment\": \"Caller\"" +
                "      }\n" +
                "     }\n" +
                "    }\n" +
                "  }\n" +
                "}";


        final Effect writeEffect = new ObjectMapper().readValue(json, Effect.class);
        assertThat(writeEffect.getKey(), is(instanceOf(EntryPointKey.class)));
        assertThat(writeEffect.getKey().toString(), is("entry-point-v1-entity-contract-3b6b4d8a3d815372508faa92f3a05dcb50c9c98de05d9a7668cb94b04f1ef9af-768c370eb010604bd19029a409dca8b5fbf9af9bc14a36c2b294a2a7a922161e"));

        final WriteKind<?> kind = writeEffect.getKind();
        assertThat(kind.getWrite(), is(instanceOf(EntryPointKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(EntryPointValue.class)));

        final EntryPointValue entryPointValue = (EntryPointValue) kind.getWrite().getValue();
        final EntryPoint entryPoint = entryPointValue.getV1();
        assertThat(entryPoint.getName(), is("burn"));
        assertThat(entryPoint.getArgs().size(), is(2));
        assertThat(entryPoint.getArgs().get(0).getName(), is("owner"));
        assertThat(entryPoint.getArgs().get(0).getClType().getTypeName(), is("Key"));
        assertThat(entryPoint.getRet().getTypeName(), is("Unit"));
        assertThat(entryPoint.getAccess().getValue(), is(EntryPoint.EntryPointAccessEnum.PUBLIC));
        assertThat(entryPoint.getType(), is(EntryPoint.EntryPointType.CALLED));
        assertThat(entryPoint.getPayment(), is(EntryPoint.EntryPointPayment.CALLER));
    }

    @Test
    void writeKindPackage() throws JsonProcessingException {

        final String json = "{\n" +
                "  \"key\": \"package-ef39f3794dfde8641acc43a8f63d4c0a72a4b33bbb2e4eed29421ee6cfd0d87e\",\n" +
                "  \"kind\": {\n" +
                "    \"Write\": {\n" +
                "      \"Package\": {\n" +
                "           \"versions\": [\n" +
                "              {\n" +
                "                  \"entity_version_key\": {\n" +
                "                       \"protocol_version_major\": 2,\n" +
                "                       \"entity_version\": 3\n" +
                "                   },\n" +
                "                   \"addressable_entity_hash\": \"addressable-entity-3b6b4d8a3d815372508faa92f3a05dcb50c9c98de05d9a7668cb94b04f1ef9af\" \n" +
                "               }\n" +
                "            ],\n" +
                "           \"disabled_versions\": [\n" +
                "              {\n" +
                "                  \"entity_version_key\": {\n" +
                "                       \"protocol_version_major\": 2,\n" +
                "                       \"entity_version\": 1\n" +
                "                   },\n" +
                "                   \"addressable_entity_hash\": \"addressable-entity-ef39f3794dfde8641acc43a8f63d4c0a72a4b33bbb2e4eed29421ee6cfd0d87e\" \n" +
                "               },\n" +
                "               {\n" +
                "                  \"entity_version_key\": {\n" +
                "                       \"protocol_version_major\": 2,\n" +
                "                       \"entity_version\": 2\n" +
                "                   },\n" +
                "                   \"addressable_entity_hash\": \"addressable-entity-aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10\" \n" +
                "               }\n" +
                "            ],\n" +
                "           \"groups\": [" +
                "                     \"group_A\", \"group_B\", \"group_C\"" +
                "                   ],\n" +
                "           \"lock_status\": \"Unlocked\" \n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        final Effect writeEffect = new ObjectMapper().readValue(json, Effect.class);

        assertThat(writeEffect.getKey(), is(instanceOf(Key.class)));
        assertThat(writeEffect.getKey().toString(), is("package-ef39f3794dfde8641acc43a8f63d4c0a72a4b33bbb2e4eed29421ee6cfd0d87e"));

        final WriteKind<?> kind = writeEffect.getKind();
        assertThat(kind.getWrite(), is(instanceOf(PackageKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(Package.class)));

        final Package contractPackage = (Package) kind.getWrite().getValue();
        assertThat(contractPackage.getVersions().get(0).getAddressableEntityHash(), is("addressable-entity-3b6b4d8a3d815372508faa92f3a05dcb50c9c98de05d9a7668cb94b04f1ef9af"));
        assertThat(contractPackage.getVersions().get(0).getEntityVersionKey().getEntityVersion(), is(3));
        assertThat(contractPackage.getVersions().get(0).getEntityVersionKey().getProtocolVersionMajor(), is(2));
        assertThat(contractPackage.getDisabledVersions().size(), is(2));
        assertThat(contractPackage.getDisabledVersions().get(1).getAddressableEntityHash(), is("addressable-entity-aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10"));
        assertThat(contractPackage.getDisabledVersions().get(1).getEntityVersionKey().getEntityVersion(), is(2));
        assertThat(contractPackage.getDisabledVersions().get(1).getEntityVersionKey().getProtocolVersionMajor(), is(2));
        assertThat(contractPackage.getGroups().size(), is(3));
        assertThat(contractPackage.getGroups().get(1), is("group_B"));
        assertThat(contractPackage.getLockStatus(), is(Package.PackageStatus.Unlocked));
    }

    @Test
    void writeKindAddressableEntity() throws JsonProcessingException {

        final String json = "{\n" +
                "  \"key\": \"entity-contract-3b6b4d8a3d815372508faa92f3a05dcb50c9c98de05d9a7668cb94b04f1ef9af\",\n" +
                "  \"kind\": {\n" +
                "    \"Write\": {\n" +
                "      \"AddressableEntity\": {\n" +
                "         \"protocol_version\": \"2.0.0\",\n" +
                "         \"entity_kind\":  {\n" +
                "           \"SmartContract\": \"VmCasperV1\" \n" +
                "         },\n" +
                "         \"package_hash\": \"package-ef39f3794dfde8641acc43a8f63d4c0a72a4b33bbb2e4eed29421ee6cfd0d87e\", \n" +
                "         \"byte_code_hash\": \"byte-code-376c7a7483df3ed53e8fb112c256bb0a99782d4f0260e5608858740a36681ac3\", \n" +
                "         \"main_purse\": \"uref-faa9c882c9721274290109abba23f1baa8d7603debc11bf26dfc6250a6f56cc2-007\", \n" +
                "         \"associated_keys\":  [\n" +
                "              {\n" +
                "                   \"account_hash\": \"account-hash-aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10\",\n" +
                "                   \"weight\": 1 \n" +
                "               }\n" +
                "            ],\n" +
                "         \"action_thresholds\":  {\n" +
                "           \"deployment\": 1, \n" +
                "           \"upgrade_management\": 1, \n" +
                "           \"key_management\": 1 \n" +
                "         },\n" +
                "         \"message_topics\": []\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        final Effect writeEffect = new ObjectMapper().readValue(json, Effect.class);
        assertThat(writeEffect.getKey(), is(instanceOf(AddressableEntityKey.class)));
        assertThat(writeEffect.getKey().toString(), is("entity-contract-3b6b4d8a3d815372508faa92f3a05dcb50c9c98de05d9a7668cb94b04f1ef9af"));

        final WriteKind<?> kind = writeEffect.getKind();
        assertThat(kind.getWrite(), is(instanceOf(AddressableEntityKind.class)));
        assertThat(kind.getWrite().getValue(), is(instanceOf(Entity.class)));

        final Entity entity = (Entity) kind.getWrite().getValue();
        assertThat(entity.getProtocolVersion(), is("2.0.0"));
        assertThat(((SmartContract) entity.getEntityAddressKind()).getSmartContract().name(), is(SmartContract.TransactionRuntime.VMCASPERV1.name()));
        assertThat(entity.getPackageHash(), is("package-ef39f3794dfde8641acc43a8f63d4c0a72a4b33bbb2e4eed29421ee6cfd0d87e"));
        assertThat(entity.getByteCodeHash(), is("byte-code-376c7a7483df3ed53e8fb112c256bb0a99782d4f0260e5608858740a36681ac3"));
        assertThat(Hex.encode(entity.getMainPurse().getAddress()), is("faa9c882c9721274290109abba23f1baa8d7603debc11bf26dfc6250a6f56cc2"));
        assertThat(entity.getAssociatedKeys().get(0).getAccountHash(), is(instanceOf(AccountHashKey.class)));
        assertThat(entity.getAssociatedKeys().get(0).getAccountHash().toString(), is("account-hash-aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10"));
        assertThat(entity.getAssociatedKeys().get(0).getWeight(), is(1));
        assertThat(entity.getActionThresholds().getDeployment(), is(1));
        assertThat(entity.getActionThresholds().getUpgradeManagement(), is(1));
        assertThat(entity.getActionThresholds().getUpgradeManagement(), is(1));
        assertThat(entity.getMessageTopics().size(), is(0));
    }
}
