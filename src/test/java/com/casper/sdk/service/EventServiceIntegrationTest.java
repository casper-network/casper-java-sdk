package com.casper.sdk.service;


import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;
import com.casper.sdk.model.event.blockadded.BlockAdded;
import com.casper.sdk.model.event.version.ApiVersion;
import com.casper.sdk.test.MockNode;
import com.casper.sdk.test.PathMatchingResourceDispatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;

//@Disabled
class EventServiceIntegrationTest {

    private static final String MAIN_EVENTS = "/event-samples/main-events.txt";
    private static final String SIGS_EVENTS = "/event-samples/sigs-events.txt";

    private static final String DEPLOYS_EVENTS = "/event-samples/deploys-events.txt";
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
                new PathMatchingResourceDispatcher(MAIN_EVENTS, is("/events/main?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventType.MAIN, EventTarget.RAW, 0L, (Consumer<Event<String>>) event -> {

            assertThat(event, instanceOf(Event.class));
            assertThat(event.getClass().getSimpleName(), is("RawEvent"));

            assertThat(event.getEventType(), is(EventType.MAIN));
            assertThat(event.getVersion(), is("1.0.0"));

            if (count[0] == 0) {
                assertThat(event.getData(), is("{\"ApiVersion\":\"1.0.0\"}"));
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

            assertThat(count[0], is(greaterThan(2)));
        }
    }

    @Test
    void deployRawEvents() throws Exception {

        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(DEPLOYS_EVENTS, is("/events/deploys?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventType.DEPLOYS, EventTarget.RAW, 0L, (Consumer<Event<String>>) event -> {

            assertThat(event.getEventType(), is(EventType.DEPLOYS));

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
                new PathMatchingResourceDispatcher(SIGS_EVENTS, is("/events/sigs?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventType.SIGS, EventTarget.RAW, 0L, (Consumer<Event<String>>) event -> {

            assertThat(event.getEventType(), is(EventType.SIGS));

            if (count[0] == 0) {
                assertThat(event.getData(), is("{\"ApiVersion\":\"1.0.0\"}"));
                assertThat(event.getId().isPresent(), is(false));
            } else if (count[0] == 1) {
                assertThat(
                        event.getData(),
                        is("{\"FinalitySignature\":{\"block_hash\":\"bb878bcf8827649f070c487800a95c35be3eb2e83b5447921675040cea38af1c\"," +
                                "\"era_id\":0,\"signature\":\"0141eba160bb11448c663aa574de7a87554adb01531b1c6e93f31d4e998d5e5b4bdd71aa67442e3e5ffd8c75f709ad68ecbec9f116f4c50c49198098d30486dc02\"," +
                                "\"public_key\":\"01959d01aa68197e8cb91aa06bcc920f8d4a245dff60ea726bb89255349107a565\"}}")
                );
                assertThat(event.getId().isPresent(), is(true));
                assertThat(event.getId().get(), is(1L));
            }
            count[0]++;

        }, Assertions::fail)) {
            Thread.sleep(5000L);
            assertThat(count[0], is(5));
        }

    }


    @Test
        //@Disabled
        // Re-enable once SDK correctly deserializes CLValueMap toy ANY type
    void mainPojoEvents() throws Exception {

        mockNode.setDispatcher(
                new PathMatchingResourceDispatcher(MAIN_EVENTS, is("/events/main?start_from=0"))
                        .setContentType("text/event-stream")
        );

        int[] count = {0};

        //noinspection unused
        try (AutoCloseable closeable = eventService.consumeEvents(EventType.MAIN, EventTarget.POJO, 0L, (Consumer<Event<EventData>>) event -> {

            assertThat(event.getEventType(), is(EventType.MAIN));

            final EventData data = event.getData();

            if (count[0] == 0) {
                assertThat(data, instanceOf(ApiVersion.class));
                assertThat(((ApiVersion) data).getApiVersion(), is("1.0.0"));
                assertThat(event.getId().isPresent(), is(false));
            } else if (count[0] == 1) {
                assertThat(data, instanceOf(BlockAdded.class));
                assertThat(event.getId().isPresent(), is(true));
                assertThat(event.getId().get(), is(2L));
            }

            count[0]++;
        }, Assertions::fail)) {
            Thread.sleep(2000L);

            assertThat(count[0], is(greaterThan(1)));
        }
    }
}