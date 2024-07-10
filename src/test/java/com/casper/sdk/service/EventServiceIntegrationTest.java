package com.casper.sdk.service;


import com.casper.sdk.model.block.BlockV1;
import com.casper.sdk.model.block.BlockV2;
import com.casper.sdk.model.clvalue.CLValueMap;
import com.casper.sdk.model.clvalue.CLValueU64;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.blockadded.BlockAdded;
import com.casper.sdk.model.event.finalitysignature.FinalitySignatureV1;
import com.casper.sdk.model.event.finalitysignature.FinalitySignatureV2;
import com.casper.sdk.model.event.step.Step;
import com.casper.sdk.model.event.transaction.StringMessagePayload;
import com.casper.sdk.model.event.transaction.TransactionAccepted;
import com.casper.sdk.model.event.transaction.TransactionProcessed;
import com.casper.sdk.model.event.version.ApiVersion;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.key.Signature;
import com.casper.sdk.model.transaction.execution.ExecutionResultV2;
import com.casper.sdk.model.transaction.kind.IdentityKind;
import com.casper.sdk.model.transaction.kind.WriteKind;
import com.casper.sdk.test.MockNode;
import com.casper.sdk.test.PathMatchingResourceDispatcher;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

//@Disabled
class EventServiceIntegrationTest {

    private static final String MAIN_EVENTS = "/event-samples/main-events.txt";
    private static final String SIGS_EVENTS = "/event-samples/sigs-events.txt";
    private static final String V2_BLOCK_EVENTS = "/event-samples/block-added-v2.txt";
    private static final String V2_STEP_EVENT = "/event-samples/step-event-v2.txt";
    private static final String DEPLOYS_EVENTS = "/event-samples/deploys-events.txt";
    private static final String TRANSACTION_ACCEPTED = "/event-samples/transaction_accepted.txt";
    private static final String TRANSACTION_PROCESSED = "/event-samples/transaction_processed.txt";

    private final MockNode mockNode = new MockNode();
    private final EventService eventService;

    EventServiceIntegrationTest() throws URISyntaxException {
        eventService = EventService.usingPeer(new URI("http://localhost:28101"));
    }

    @BeforeEach
    void setUp() throws URISyntaxException, IOException {
        mockNode.start(new URI("http://localhost:28101"));
    }

    @AfterEach
    void tearDown() throws IOException {
        mockNode.shutdown();
    }


    @Test
    void consumeMainRawEvents() throws Exception {

        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(MAIN_EVENTS, is("/events?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventTarget.RAW, 0L, (Consumer<Event<String>>) event -> {

            assertThat(event, instanceOf(Event.class));
            assertThat(event.getClass().getSimpleName(), is("RawEvent"));

            assertThat(event.getVersion(), is("2.0.0"));

            if (count[0] == 0) {
                assertThat(event.getData(), is("{\"ApiVersion\":\"2.0.0\"}"));
                assertThat(event.getId().isPresent(), is(false));
            } else if (count[0] == -1) {
                // TODO REINSTATE ONCE STEP CAN BE READ
                assertThat(event.getData(), startsWith("{\"Step\":{\"era_id\":0,\"execution_effect\":{\"operations\":[],"));
                assertThat(event.getData(), endsWith("\"bytes\":\"03f5d62682010000\",\"parsed\":1658508997891}}}]}}}"));
                assertThat(event.getId().isPresent(), is(true));
                assertThat(event.getId().get(), is(0L));

            } else if (count[0] == 2) {
                assertThat(
                        event.getData(),
                        is("{\"BlockAdded\":{\"block_hash\":\"c77080456598933f9b0a68827314f8da3a0b10d1a7532d1737352d9f3a36a534\"," +
                                "\"block\":{\"hash\":\"c77080456598933f9b0a68827314f8da3a0b10d1a7532d1737352d9f3a36a534\",\"header\":" +
                                "{\"parent_hash\":\"bb878bcf8827649f070c487800a95c35be3eb2e83b5447921675040cea38af1c\"," +
                                "\"state_root_hash\":\"fbd89036ca934a53b14ebc99abcf64351008ac073848c0b384771036121a25cc\"," +
                                "\"body_hash\":\"980531392fb02fd03d632abaa17f0a59bc788ea5b86ff9ce4630851cf9c2b4cf\"," +
                                "\"random_bit\":true,\"accumulated_seed\":\"ab5d756563bf09545590bd95f9a8e7978d51760be95ea8e5c9bab58ba1129186\"," +
                                "\"era_end\":null,\"timestamp\":\"2022-07-22T16:56:40.704Z\",\"era_id\":1,\"height\":1," +
                                "\"protocol_version\":\"1.0.0\"},\"body\":{\"proposer\":\"010d23984fefcce099679a24496f1d3072a540b95d321f8ba951df0cfe2c0691e5\"," +
                                "\"deploy_hashes\":[],\"transfer_hashes\":[]},\"proofs\":[]}}}")
                );
                assertThat(event.getId().isPresent(), is(true));
                assertThat(event.getId().get(), is(7L));

            }
            count[0]++;

        }, Assertions::fail)) {
            Thread.sleep(4000L);

            assertThat(count[0], is(greaterThan(1)));
        }
    }

    @Test
    void deployRawEvents() throws Exception {

        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(DEPLOYS_EVENTS, is("/events?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventTarget.RAW, 0L, (Consumer<Event<String>>) event -> {

            if (count[0] == 0) {
                assertThat(event.getData(), is("{\"ApiVersion\":\"1.0.0\"}"));
                assertThat(event.getId().isPresent(), is(false));
            } else if (count[0] == 1) {
                assertThat(
                        event.getData(),
                        is("{\"DeployAccepted\":{\"hash\":\"fb81219f33aa58a2c2f50f7eea20c3065963f61bc3c74810729f10dc21981087\"," +
                                "\"header\":{\"account\":\"01959d01aa68197e8cb91aa06bcc920f8d4a245dff60ea726bb89255349107a565\"," +
                                "\"timestamp\":\"2022-07-26T14:37:15.150Z\",\"ttl\":\"30m\",\"gas_price\":10," +
                                "\"body_hash\":\"c2930c761cdc90241e7bfd2c5bbc5805ec9511845cb4820f997fa57334a33723\"," +
                                "\"dependencies\":[],\"chain_name\":\"casper-net-1\"},\"payment\":{\"ModuleBytes\":" +
                                "{\"module_bytes\":\"\",\"args\":[[\"amount\",{\"cl_type\":\"U512\"," +
                                "\"bytes\":\"0500e40b5402\",\"parsed\":\"10000000000\"}]]}},\"session\":" +
                                "{\"Transfer\":{\"args\":[[\"amount\",{\"cl_type\":\"U512\",\"bytes\":\"0400f90295\"," +
                                "\"parsed\":\"2500000000\"}],[\"target\",{\"cl_type\":{\"ByteArray\":32},\"bytes\":" +
                                "\"a6cdb6f049363f6ab119be0c961c36e4a3c09319589341dd861f405d9836fc67\",\"parsed\":" +
                                "\"a6cdb6f049363f6ab119be0c961c36e4a3c09319589341dd861f405d9836fc67\"}]," +
                                "[\"id\",{\"cl_type\":{\"Option\":\"U64\"},\"bytes\":\"010100000000000000\",\"parsed\":1}]]}}," +
                                "\"approvals\":[{\"signer\":\"01959d01aa68197e8cb91aa06bcc920f8d4a245dff60ea726bb89255349107a565\"," +
                                "\"signature\":\"01e57c01fc538fe057eac09d55360c70b6b7830548582c9931832af78149c66a698a41f33ca06904898138bda6767cfc5f60f26a11980ad5f95f489dcccc2fa80d\"}]}}")
                );
                assertThat(event.getId().isPresent(), is(true));
                assertThat(event.getId().get(), is(2951L));
            }
            count[0]++;
        }, Assertions::fail)) {
            Thread.sleep(4000L);
            assertThat(count[0], is(2));
        }
    }

    @Test
    void sigsRawEvents() throws Exception {

        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(SIGS_EVENTS, is("/events?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventTarget.RAW, 0L, (Consumer<Event<String>>) event -> {

            if (count[0] == 0) {
                assertThat(event.getData(), is("{\"ApiVersion\":\"2.0.0\"}"));
                assertThat(event.getId().isPresent(), is(false));
            } else if (count[0] == 1) {
                assertThat(
                        event.getData(),
                        is("{\"FinalitySignature\":{\"V1\":{\"block_hash\":\"bb878bcf8827649f070c487800a95c35be3eb2e83b5447921675040cea38af1c\"," +
                                "\"era_id\":0,\"signature\":\"0141eba160bb11448c663aa574de7a87554adb01531b1c6e93f31d4e998d5e5b4bdd71aa67442e3e5ffd8c75f709ad68ecbec9f116f4c50c49198098d30486dc02\"," +
                                "\"public_key\":\"01959d01aa68197e8cb91aa06bcc920f8d4a245dff60ea726bb89255349107a565\"}}}")
                );
                assertThat(event.getId().isPresent(), is(true));
                assertThat(event.getId().get(), is(1L));
            } else if (count[0] == 2) {
                assertThat(
                        event.getData(),
                        is("{\"FinalitySignature\":{\"V2\":{\"block_hash\":\"941c89d620ab29052ce26b974e5670cf6dd971447b84161cf4711a74162bbbad\"," +
                                "\"block_height\":94001,\"era_id\":215,\"chain_name_hash\":\"f087a92e6e7077b3deb5e00b14a904e34c7068a9410365435bc7ca5d3ac64301\"," +
                                "\"signature\":\"010159951a3d420ac0bda95b0946eed14e43270500a37d43d4477141ab448bfb55ad29a41475c23c5825d628f7af39745cab03fecf67731f2c768da229fbaf570a\"," +
                                "\"public_key\":\"01032146b0b9de01e26aaec7b0d1769920de94681dbd432c3530bfe591752ded6c\"}}}")
                );
                assertThat(event.getId().isPresent(), is(true));
                assertThat(event.getId().get(), is(1L));
            }
            count[0]++;

        }, Assertions::fail)) {
            Thread.sleep(5000L);
            assertThat(count[0], is(2));
        }
    }

    @Test
    void sigPojoEvents() throws Exception {
        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(SIGS_EVENTS, is("/events?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventTarget.POJO, 0L, (Consumer<Event<EventData>>) event -> {

            final EventData data = event.getData();

            if (count[0] == 0) {
                assertThat(data, instanceOf(ApiVersion.class));
                assertThat(((ApiVersion) data).getApiVersion(), is("2.0.0"));
                assertThat(event.getId().isPresent(), is(false));
            } else if (count[0] == 1) {
                assertThat(data, is(instanceOf(FinalitySignatureV1.class)));
                FinalitySignatureV1 sig = (FinalitySignatureV1) data;
                assertThat(sig.getBlockHash(), is(new Digest("bb878bcf8827649f070c487800a95c35be3eb2e83b5447921675040cea38af1c")));
                assertThat(sig.getEraId(), is(0L));
                try {
                    assertThat(sig.getSignature(), is(Signature.fromHex("0141eba160bb11448c663aa574de7a87554adb01531b1c6e93f31d4e998d5e5b4bdd71aa67442e3e5ffd8c75f709ad68ecbec9f116f4c50c49198098d30486dc02")));
                    assertThat(sig.getPublicKey(), is(PublicKey.fromTaggedHexString("01959d01aa68197e8cb91aa06bcc920f8d4a245dff60ea726bb89255349107a565")));
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            } else if (count[0] == 2) {
                assertThat(data, is(instanceOf(FinalitySignatureV2.class)));
                FinalitySignatureV2 sig = (FinalitySignatureV2) data;
                assertThat(sig.getBlockHash(), is(new Digest("941c89d620ab29052ce26b974e5670cf6dd971447b84161cf4711a74162bbbad")));
                assertThat(sig.getBlockHeight(), is(new BigInteger("94001")));
                assertThat(sig.getEraId(), is(215L));
                assertThat(sig.getChainNameHash(), is(new Digest("f087a92e6e7077b3deb5e00b14a904e34c7068a9410365435bc7ca5d3ac64301")));
                try {
                    assertThat(sig.getSignature(), is(Signature.fromHex("010159951a3d420ac0bda95b0946eed14e43270500a37d43d4477141ab448bfb55ad29a41475c23c5825d628f7af39745cab03fecf67731f2c768da229fbaf570a")));
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }

            count[0]++;
        }, Assertions::fail)) {
            Thread.sleep(3000L);
            assertThat(count[0], is(greaterThan(2)));
        }
    }


    @Test
    void mainPojoEvents() throws Exception {

        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(MAIN_EVENTS, is("/events?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventTarget.POJO, 0L, (Consumer<Event<EventData>>) event -> {

            final EventData data = event.getData();

            if (count[0] == 0) {
                assertThat(data, instanceOf(ApiVersion.class));
                assertThat(((ApiVersion) data).getApiVersion(), is("2.0.0"));
                assertThat(event.getId().isPresent(), is(false));
            } else if (count[0] == 1) {
                assertThat(data, instanceOf(BlockAdded.class));
                assertThat(((BlockAdded) data).getBlock(), instanceOf(BlockV1.class));
                assertThat(event.getId().isPresent(), is(true));
                assertThat(event.getId().get(), is(2L));
            }

            count[0]++;
        }, Assertions::fail)) {
            Thread.sleep(2000L);

            assertThat(count[0], is(greaterThan(1)));
        }
    }

    @Test
    void blockAddedEventVersion2Raw() throws Exception {
        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(V2_BLOCK_EVENTS, is("/events?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventTarget.RAW, 0L, (Consumer<Event<EventData>>) event -> {

            if (count[0] == 0) {
                assertThat(event.getData(), is("{\"ApiVersion\":\"2.0.0\"}"));
                assertThat(event.getId().isPresent(), is(false));
            } else if (count[0] == 1) {
                assertThat("" + event.getData(), containsString("\"block\":{\"Version2\""));
            }

            count[0]++;
        }, Assertions::fail)) {
            Thread.sleep(2000L);
            assertThat(count[0], is(greaterThan(1)));
        }
    }

    @Test
    void blockAddedEventVersion2Pojo() throws Exception {
        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(V2_BLOCK_EVENTS, is("/events?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventTarget.POJO, 0L, (Consumer<Event<EventData>>) event -> {

            final EventData data = event.getData();

            if (count[0] == 0) {
                assertThat(data, instanceOf(ApiVersion.class));
                assertThat(((ApiVersion) data).getApiVersion(), is("2.0.0"));
                assertThat(event.getId().isPresent(), is(false));
            } else if (count[0] == 1) {
                assertThat(data, instanceOf(BlockAdded.class));
                assertThat(((BlockAdded) data).getBlock(), instanceOf(BlockV2.class));
                assertThat(event.getId().isPresent(), is(true));
                assertThat(event.getId().get(), is(1132420L));
            }

            count[0]++;
        }, Assertions::fail)) {
            Thread.sleep(3000L);
            assertThat(count[0], is(greaterThan(1)));
        }
    }

    @Test
    void stepEventV2Pojo() throws Exception {
        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(V2_STEP_EVENT, is("/events?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventTarget.POJO, 0L, (Consumer<Event<EventData>>) event -> {

            final EventData data = event.getData();

            if (count[0] == 0) {
                assertThat(data, instanceOf(Step.class));
                assertThat(event.getId().isPresent(), is(true));
                assertThat(event.getId().get(), is(67L));

                Step step = (Step) data;
                assertThat(step.getExecutionEffects(), hasSize(37));
                assertThat(step.getExecutionEffects().get(0).getKey(), is("entity-system-001501686b8d0a46fa1d47462b11b07f25ff80a13f0d889e7fd56cda81f4d642"));
                assertThat(step.getExecutionEffects().get(0).getKind(), is(instanceOf(IdentityKind.class)));

                assertThat(step.getExecutionEffects().get(34).getKey(), is("uref-da840e3134fed2d355e2cc24e762649b74d46493815de375053f5f82f0e18852-000"));
                assertThat(step.getExecutionEffects().get(34).getKind(), is(instanceOf(WriteKind.class)));
                assertThat(((WriteKind) step.getExecutionEffects().get(34).getKind()).getWrite().getValue(), is(instanceOf(CLValueMap.class)));
                CLValueMap clValueMap = (CLValueMap) ((WriteKind) step.getExecutionEffects().get(34).getKind()).getWrite().getValue();
                assertThat(clValueMap.getClType().getKeyValueTypes().getKeyType().getTypeName(), is(AbstractCLType.U64));
                assertThat(clValueMap.getClType().getKeyValueTypes().getValueType().getTypeName(), is(AbstractCLType.MAP));
                assertThat(clValueMap.getBytes(), is("0300000001000000000000000500000001297c041e1af9b14f2ea215641adbe231cf395364aed00d288fdeba849f34b0ac08010064a7b3b6e00d0101000000017fab55ab50c80193b142ddf9c6cbd877a0ed1c64831889620750b1c327eb99ef08010064a7b3b6e00d0131afbb18e108ea19dea4ec2142a8de1623dcaab87823ed87ec70922c8d05217b08020064a7b3b6e00d0201000000010ac86a5c07d75343ca3dfa1fbcadd0723b1e3df3863907520be8dbd5a229ccd908020064a7b3b6e00d01653b55b810f692c8d8d8c6ea2db60d25fc661578f07fc9b9be9ae26f47af8d4508040064a7b3b6e00d04010000000167cfe9c230684c4b3cbb2b410493da66de7764271730584cfb8d418b28d06d3e08040064a7b3b6e00d01c0bd8e47de46c1ae7d477eb245a235efabd0c03ff29991d1c2a766186c80e03a08030064a7b3b6e00d0301000000010b37cccaf40ed3723c0933770f0b291b01a82ddb0c9086365424ce5e0407201208030064a7b3b6e00d01e1c98abaead1998334e69c73ef0f75af74844568273bbb523295604b1af0dea608050064a7b3b6e00d050100000001676de7c258615c085a9ca7bd6d597d07ec88ba3184c653de60ab7fc6f8b4b67c08050064a7b3b6e00d02000000000000000500000001297c041e1af9b14f2ea215641adbe231cf395364aed00d288fdeba849f34b0ac08010064a7b3b6e00d0101000000017fab55ab50c80193b142ddf9c6cbd877a0ed1c64831889620750b1c327eb99ef08010064a7b3b6e00d0131afbb18e108ea19dea4ec2142a8de1623dcaab87823ed87ec70922c8d05217b08020064a7b3b6e00d0201000000010ac86a5c07d75343ca3dfa1fbcadd0723b1e3df3863907520be8dbd5a229ccd908020064a7b3b6e00d01653b55b810f692c8d8d8c6ea2db60d25fc661578f07fc9b9be9ae26f47af8d4508040064a7b3b6e00d04010000000167cfe9c230684c4b3cbb2b410493da66de7764271730584cfb8d418b28d06d3e08040064a7b3b6e00d01c0bd8e47de46c1ae7d477eb245a235efabd0c03ff29991d1c2a766186c80e03a08030064a7b3b6e00d0301000000010b37cccaf40ed3723c0933770f0b291b01a82ddb0c9086365424ce5e0407201208030064a7b3b6e00d01e1c98abaead1998334e69c73ef0f75af74844568273bbb523295604b1af0dea608050064a7b3b6e00d050100000001676de7c258615c085a9ca7bd6d597d07ec88ba3184c653de60ab7fc6f8b4b67c08050064a7b3b6e00d03000000000000000500000001297c041e1af9b14f2ea215641adbe231cf395364aed00d288fdeba849f34b0ac080290eac47bfded0d0101000000017fab55ab50c80193b142ddf9c6cbd877a0ed1c64831889620750b1c327eb99ef0801f0334d2ebaed0d0131afbb18e108ea19dea4ec2142a8de1623dcaab87823ed87ec70922c8d05217b0804804763a98ef30d0201000000010ac86a5c07d75343ca3dfa1fbcadd0723b1e3df3863907520be8dbd5a229ccd90802802a9d7cd1f20d01653b55b810f692c8d8d8c6ea2db60d25fc661578f07fc9b9be9ae26f47af8d450806802f940f78f90d04010000000167cfe9c230684c4b3cbb2b410493da66de7764271730584cfb8d418b28d06d3e080400965a9290f70d01c0bd8e47de46c1ae7d477eb245a235efabd0c03ff29991d1c2a766186c80e03a080430a13cc940ee0d0301000000010b37cccaf40ed3723c0933770f0b291b01a82ddb0c9086365424ce5e040720120803507dd5e076ed0d01e1c98abaead1998334e69c73ef0f75af74844568273bbb523295604b1af0dea6080720e87e734dff0d050100000001676de7c258615c085a9ca7bd6d597d07ec88ba3184c653de60ab7fc6f8b4b67c0806e0305eaa63fc0d"));

                assertThat(step.getExecutionEffects().get(36).getKey(), is("uref-10ad3536d8be35beb06864d5589ac74d47b9440148c07c92ca1edaf1176469d0-000"));
                assertThat(step.getExecutionEffects().get(36).getKind(), is(instanceOf(WriteKind.class)));
                assertThat(((WriteKind) step.getExecutionEffects().get(36).getKind()).getWrite().getValue(), is(instanceOf(CLValueU64.class)));
                CLValueU64 clValueU64 = (CLValueU64) ((WriteKind) step.getExecutionEffects().get(36).getKind()).getWrite().getValue();
                assertThat(clValueU64.getValue(), is(new BigInteger("1720017392950")));
                assertThat(clValueU64.getBytes(), is("3695067990010000"));
            }

            count[0]++;
        }, Assertions::fail)) {
            Thread.sleep(3000L);
            assertThat(count[0], is(greaterThan(0)));
        }
    }

    @Test
    void transactionAccepted() throws Exception {
        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(TRANSACTION_ACCEPTED, is("/events?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventTarget.POJO, 0L, (Consumer<Event<EventData>>) event -> {

            final EventData data = event.getData();

            if (count[0] == 0) {
                assertThat(data, instanceOf(TransactionAccepted.class));
                final TransactionAccepted transactionAccepted = (TransactionAccepted) data;
                assertThat(transactionAccepted.getTransaction(), is(instanceOf(Deploy.class)));
                final Deploy deploy = (Deploy) transactionAccepted.getTransaction();
                assertThat(deploy.getHash(), is(new Digest("37c80db9d769cb23ab482f44c2e8d8a73d9e24a1801e81d423953b8ba04b275d")));
                try {
                    assertThat(deploy.getHeader().getAccount(), is(PublicKey.fromTaggedHexString("01197debef24d5abef5251c35925d79b21fada5bca6b0afd212216b5c63c22be6f")));
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            }

            count[0]++;
        }, Assertions::fail)) {
            Thread.sleep(3000L);
            assertThat(count[0], is(greaterThan(0)));
        }
    }

    @Test
    void transactionProcessed() throws Exception {
        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(TRANSACTION_PROCESSED, is("/events?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventTarget.POJO, 0L, (Consumer<Event<EventData>>) event -> {

            final EventData data = event.getData();

            if (count[0] == 0) {
                assertThat(data, instanceOf(TransactionProcessed.class));
                final TransactionProcessed transactionProcessed = (TransactionProcessed) data;
                assertThat(transactionProcessed.getTransactionHash(), is(new Digest("7141f95d3336e63be9fc166e14effea44dd90f5845acff34966c4f950e56c3f9")));
                try {
                    assertThat(transactionProcessed.getInitiatorAddr().getAddress(), is(PublicKey.fromTaggedHexString("0138329930033bca4773a6623574ad7870ee39c554f153f15609e200e50049a7de")));
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                assertThat(transactionProcessed.getTimestamp(), is(new DateTime("2024-07-09T09:48:14.752Z").toDate()));
                assertThat(transactionProcessed.getBlockHash(), is(new Digest("b1d2019c39f2f4897e79f8b460e9862d523e40c675378e23ba308a2e58483a09")));
                assertThat(transactionProcessed.getExecutionResult(), is(instanceOf(ExecutionResultV2.class)));
                ExecutionResultV2 resultV2 = (ExecutionResultV2) transactionProcessed.getExecutionResult();
                assertThat(resultV2.getErrorMessage(), is("unsupported mode for deploy-hash(7141..c3f9) attempting transfer"));
                assertThat(resultV2.getEffects(), hasSize(9));
                assertThat(transactionProcessed.getMessages(), hasSize(1));
                assertThat(transactionProcessed.getMessages().get(0).getMessage(), is(instanceOf(StringMessagePayload.class)));
                assertThat(transactionProcessed.getMessages().get(0).getMessage().getMessage(), is("Thequickbrownfoxjumpsoverthelazydog"));
            }

            count[0]++;
        }, Assertions::fail)) {
            Thread.sleep(3000L);
            assertThat(count[0], is(greaterThan(0)));
        }
    }
}
