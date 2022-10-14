package com.casper.sdk.service.impl.event;


import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;
import com.casper.sdk.model.event.blockadded.BlockAdded;
import com.casper.sdk.model.event.deployaccepted.DeployAccepted;
import com.casper.sdk.model.event.deployexpired.DeployExpired;
import com.casper.sdk.model.event.deployprocessed.DeployProcessed;
import com.casper.sdk.model.event.fault.Fault;
import com.casper.sdk.model.event.finalitysignature.FinalitySignature;
import com.casper.sdk.model.event.shutdown.Shutdown;
import com.casper.sdk.model.event.step.Step;
import com.casper.sdk.model.event.version.ApiVersion;
import com.casper.sdk.model.key.PublicKey;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static org.hamcrest.core.StringStartsWith.startsWith;

class EventBuilderTest {

    private static final String BLOCK_ADDED_EVENT = "event-samples/block-added-event.txt";

    private static final String DEPLOY_ACCEPTED_EVENT = "event-samples/deploy-accepted-event.txt";
    private static final String DEPLOY_EXPIRED_EVENT = "event-samples/deploy-expired-event.txt";

    private static final String FINALITY_SIGNATURE_EVENT = "event-samples/finality-signature-event.txt";

    private static final String STEP_EVENT = "event-samples/step-event.txt";

    private static final String DEPLOY_PROCESSED_EVENT = "event-samples/deploy-processed-event.txt";

    private static final String API_VERSION_EVENT = "event-samples/api-version-event.txt";

   private static final String FAULT_EVENT = "event-samples/fault-event.txt";

    private static final String SHUTDOWN_EVENT = "event-samples/shutdown-event.txt";


    @Test
    void buildRawMainEvent() throws IOException {

        final AbstractEvent<?> abstractEvent = getEvent(EventType.MAIN, EventTarget.RAW, BLOCK_ADDED_EVENT);

        assertThat(abstractEvent, instanceOf(RawEvent.class));
        assertThat(abstractEvent.getEventType(), is(EventType.MAIN));
        assertThat(abstractEvent.getId().isPresent(), is(true));
        assertThat(abstractEvent.getId().get(), is(2L));
        assertThat(abstractEvent.getData().toString(), startsWith("data:{\"BlockAdded\":{\"block_hash\":\"bb878bcf8827649f070c487800a95c35be3eb2e83b5447921675040cea38af1c\",\"block\":"));
        assertThat(abstractEvent.getData().toString(), endsWith("\"transfer_hashes\":[]},\"proofs\":[]}}}"));
    }

    @Test
    void buildApiVersionEvent() throws IOException {

        final InputStream in = EventBuilderTest.class.getClassLoader().getResourceAsStream(API_VERSION_EVENT);
        final EventBuilder builder = new EventBuilder(EventType.MAIN, EventTarget.POJO, "http://localhost:81012");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        builder.processLine(reader.readLine());

        assertThat(builder.isComplete(), is(true));
        final AbstractEvent<ApiVersion> abstractEvent = builder.buildEvent();
        assertThat(builder.isComplete(), is(false));

        assertThat(abstractEvent, instanceOf(PojoEvent.class));
        assertThat(abstractEvent.getData(), instanceOf(ApiVersion.class));
        assertThat(abstractEvent.getVersion(), is("1.0.0"));

        final ApiVersion apiVersion = abstractEvent.getData();
        assertThat(apiVersion.getApiVersion(), is("1.0.0"));
    }

    @Test
    void buildPojoBlockAddedMainEvent() throws IOException, NoSuchAlgorithmException {

        final AbstractEvent<BlockAdded> abstractEvent = getEvent(EventType.MAIN, EventTarget.POJO, BLOCK_ADDED_EVENT);

        assertThat(abstractEvent, instanceOf(PojoEvent.class));
        assertThat(abstractEvent.getData(), instanceOf(BlockAdded.class));

        final BlockAdded blockAdded = abstractEvent.getData();
        assertThat(blockAdded.getBlockHash(), is(new Digest("bb878bcf8827649f070c487800a95c35be3eb2e83b5447921675040cea38af1c")));

        assertThat(blockAdded.getBlock().getHash(), is(new Digest("bb878bcf8827649f070c487800a95c35be3eb2e83b5447921675040cea38af1c")));

        assertThat(blockAdded.getBlock().getHeader().getParentHash(), is(new Digest("0000000000000000000000000000000000000000000000000000000000000000")));
        assertThat(blockAdded.getBlock().getHeader().getStateRootHash(), is(new Digest("fbd89036ca934a53b14ebc99abcf64351008ac073848c0b384771036121a25cc")));
        assertThat(blockAdded.getBlock().getHeader().getBodyHash(), is(new Digest("5187b7a8021bf4f2c004ea3a54cfece1754f11c7624d2363c7f4cf4fddd1441e")));
        assertThat(blockAdded.getBlock().getHeader().isRandomBit(), is(false));
        assertThat(blockAdded.getBlock().getHeader().getAccumulatedSeed(), is(new Digest("d8908c165dee785924e7421a0fd0418a19d5daeec395fd505a92a0fd3117e428")));
        assertThat(blockAdded.getBlock().getHeader().getEraEnd().getEraReport().getEquivocators().isEmpty(), is(true));
        assertThat(blockAdded.getBlock().getHeader().getEraEnd().getEraReport().getRewards().isEmpty(), is(true));
        assertThat(blockAdded.getBlock().getHeader().getEraEnd().getEraReport().getInactiveValidators().isEmpty(), is(true));
        assertThat(blockAdded.getBlock().getHeader().getEraEnd().getNextEraValidatorWeights().isEmpty(), is(false));
        assertThat(blockAdded.getBlock().getHeader().getEraEnd().getNextEraValidatorWeights(), hasSize(5));
        assertThat(blockAdded.getBlock().getHeader().getEraEnd().getNextEraValidatorWeights().get(4).getValidator(), is (PublicKey.fromTaggedHexString("01fcf1392c59c7d89190bfcd1b00902cc0801700eab98034aa4e56816d338f6c25")));
        assertThat(blockAdded.getBlock().getHeader().getEraEnd().getNextEraValidatorWeights().get(4).getWeight(), is(new BigInteger("2000000000000008")));
        assertThat(blockAdded.getBlock().getHeader().getTimeStamp(), is(new DateTime("2022-07-22T16:56:37.891Z").toDate()));
        assertThat(blockAdded.getBlock().getHeader().getEraId(), is(0L));
        assertThat(blockAdded.getBlock().getHeader().getHeight(), is(0L));
        assertThat(blockAdded.getBlock().getHeader().getProtocolVersion(), is("1.0.0"));
        assertThat(blockAdded.getBlock().getBody().getProposer(), is(nullValue()));
        assertThat(blockAdded.getBlock().getBody().getDeployHashes().isEmpty(), is(true));
        assertThat(blockAdded.getBlock().getBody().getTransferHashes().isEmpty(), is(true));
        assertThat(blockAdded.getBlock().getProofs().isEmpty(), is(true));
    }

    @Test
    void buildFinalitySignatureSigEvent() throws IOException, NoSuchAlgorithmException {

        final PojoEvent<FinalitySignature> abstractEvent = getEvent(EventType.SIGS, EventTarget.POJO, FINALITY_SIGNATURE_EVENT);
        assertThat(abstractEvent.getData(), instanceOf(FinalitySignature.class));

        final FinalitySignature finalitySignature = abstractEvent.getData();
        assertThat(finalitySignature.getBlockHash(), is(new Digest("bb878bcf8827649f070c487800a95c35be3eb2e83b5447921675040cea38af1c")));
        assertThat(finalitySignature.getEraId(), is(0L));
        assertThat(finalitySignature.getSignature(), is("0141eba160bb11448c663aa574de7a87554adb01531b1c6e93f31d4e998d5e5b4bdd71aa67442e3e5ffd8c75f709ad68ecbec9f116f4c50c49198098d30486dc02"));
        assertThat(finalitySignature.getPublicKey(), is(PublicKey.fromTaggedHexString("01959d01aa68197e8cb91aa06bcc920f8d4a245dff60ea726bb89255349107a565")));
    }


    @Test
    void buildDeployAcceptedEvent() throws IOException {

        final PojoEvent<DeployAccepted> deployAcceptedEvent = getEvent(EventType.MAIN, EventTarget.POJO, DEPLOY_ACCEPTED_EVENT);
        assertThat(deployAcceptedEvent, is(notNullValue()));
        assertThat(deployAcceptedEvent.getData(), is(instanceOf(DeployAccepted.class)));

        final DeployAccepted deployAccepted = deployAcceptedEvent.getData();
        assertThat(deployAccepted.getDeploy(), is(notNullValue()));
        assertThat(deployAccepted.getDeploy().getHash(), is(new Digest("fb81219f33aa58a2c2f50f7eea20c3065963f61bc3c74810729f10dc21981087")));

        // TODO rest of fields
    }

    @Test
    void buildDeployProcessedEvent() throws IOException {

        final PojoEvent<DeployProcessed> deployProcessedEvent = getEvent(EventType.MAIN, EventTarget.POJO, DEPLOY_PROCESSED_EVENT);
        assertThat(deployProcessedEvent, is(notNullValue()));
        // TODO: Shouldn't this be a DeployProcessed?
        //assertThat(deployProcessedEvent.getData(), is(instanceOf(DeployAccepted.class)));
        assertThat(deployProcessedEvent.getData(), is(instanceOf(DeployProcessed.class)));

        final DeployProcessed deployProcessed = deployProcessedEvent.getData();
        // TODO: Shouldn't this be getDeployHash?
        //assertThat(deployProcessed.getBlockHash(), is(new Digest("fb81219f33aa58a2c2f50f7eea20c3065963f61bc3c74810729f10dc21981087")));
        assertThat(deployProcessed.getDeployHash(), is(new Digest("fb81219f33aa58a2c2f50f7eea20c3065963f61bc3c74810729f10dc21981087")));
        assertThat(deployProcessed.getAccount(), is(new Digest("01959d01aa68197e8cb91aa06bcc920f8d4a245dff60ea726bb89255349107a565")));

        // TODO rest of fields
    }

    @Test
    void buildDeployExpiredEvent() throws IOException {

        final PojoEvent<DeployExpired> deployProcessedEvent = getEvent(EventType.MAIN, EventTarget.POJO, DEPLOY_EXPIRED_EVENT);
        assertThat(deployProcessedEvent, is(notNullValue()));
        assertThat(deployProcessedEvent.getData(), is(instanceOf(DeployExpired.class)));

        final DeployExpired deployProcessed = deployProcessedEvent.getData();
        assertThat(deployProcessed.getDeployHash(), is(new Digest("7ecf22fc284526c6db16fb6455f489e0a9cbf782834131c010cf3078fb9be353")));
    }

    @Test
    void buildFaultEvent() throws IOException, NoSuchAlgorithmException {

        final PojoEvent<Fault> faultPojoEvent = getEvent(EventType.MAIN, EventTarget.POJO, FAULT_EVENT);
        assertThat(faultPojoEvent, is(notNullValue()));
        assertThat(faultPojoEvent.getData(), instanceOf(Fault.class));

        final Fault fault = faultPojoEvent.getData();
        assertThat(fault.getEraId(), is(4591448806312642506L));
        assertThat(fault.getPublicKey(), is(PublicKey.fromTaggedHexString("012fa85eb06279da42e68530e1116be04bfd2aaa5ed8d63401ebff4d9153a609a9")));
        assertThat(fault.getTimestamp(), is("2020-08-07T01:26:58.364Z"));
    }

    @Test
    @Disabled
    void buildStepEvent() throws IOException {

        final PojoEvent<Step> stepEvent = getEvent(EventType.MAIN, EventTarget.POJO, STEP_EVENT);
        assertThat(stepEvent, is(notNullValue()));
    }


    @Test
    @Disabled
    void buildRawShutDownEvent() throws IOException {

        final RawEvent shutdownEvent = getEvent(EventType.MAIN, EventTarget.RAW, SHUTDOWN_EVENT);
        assertThat(shutdownEvent, is(notNullValue()));
        assertThat(shutdownEvent.getData(), is("data:\"Shutdown\""));
    }

    @Test
    void buildPojoShutDownEvent() throws IOException {

        final PojoEvent<Shutdown> shutdownEvent = getEvent(EventType.MAIN, EventTarget.POJO, SHUTDOWN_EVENT);
        assertThat(shutdownEvent, is(notNullValue()));
    }

    private <T extends Event<?>> T getEvent(final EventType eventType,
                                            final EventTarget eventTarget,
                                            final String eventPath) throws IOException {

        final InputStream in = EventBuilderTest.class.getClassLoader().getResourceAsStream(eventPath);
        final EventBuilder builder = new EventBuilder(eventType, eventTarget, "http://localhost:81012");
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        builder.processLine(reader.readLine());
        assertThat(builder.isComplete(), is(false));
        builder.processLine(reader.readLine());
        assertThat(builder.isComplete(), is(true));

        final T e = builder.buildEvent();

        // Assert builder has reset
        assertThat(builder.isComplete(), is(false));

        return e;
    }


}