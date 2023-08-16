package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.AssetUtils;
import com.casper.sdk.helper.CasperTransferHelper;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.event.blockadded.BlockAdded;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.service.CasperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.casper.sdk.e2e.utils.TestProperties;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import com.syntifi.crypto.key.Ed25519PublicKey;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author ian@meywood.com
 */
public class PutTransferDeployTest {

    @Test
    void putTransferDeploy() throws Exception {

        TestProperties testProperties = new TestProperties();

        final CasperService casperService = CasperService.usingPeer(testProperties.getHostname(), testProperties.getRcpPort());

        final Ed25519PrivateKey senderKey = new Ed25519PrivateKey();
        final Ed25519PublicKey receiverKey = new Ed25519PublicKey();

        senderKey.readPrivateKey(getUserKeyAsset(1, 1, "secret_key.pem").getFile());
        receiverKey.readPublicKey(getUserKeyAsset(1, 2, "public_key.pem").getFile());


        final Deploy deploy = CasperTransferHelper.buildTransferDeploy(
                senderKey,
                PublicKey.fromAbstractPublicKey(receiverKey),
                BigInteger.valueOf(2500000000L),
                "casper-net-1",
                Math.abs(new Random().nextLong()),
                BigInteger.valueOf(100000000L),
                1L,
                Ttl.builder().ttl("30m").build(),
                new Date(),
                new ArrayList<>());

        final DeployResult deployResult = casperService.putDeploy(deploy);
        assertThat(deployResult.getDeployHash().length(), is(64));
        assertThat(deployResult.getApiVersion(), is("1.0.0"));
    }

    @Test
    void parseBlockAddedEvent() throws Exception {
        String json = "{\"BlockAdded\":{\"block_hash\":\"f75da3af54e9d6884f158b9c54b292151234fcdbd237954d5095d8aa95b425c9\",\"block\":{\"hash\":\"f75da3af54e9d6884f158b9c54b292151234fcdbd237954d5095d8aa95b425c9\",\"header\":{\"parent_hash\":\"02e177fb0ce77b8c1082d7832847079e8e31e7115dd0a9ac5bfef84804ef96c6\",\"state_root_hash\":\"4fb73294ee06b0768dd127f073b425bb09f4a4b9a9105126191950b3ddccaa3a\",\"body_hash\":\"e459ad9d5b7b6212d85583de647f7b047c048dec211b3a2f177d76e0393430f3\",\"random_bit\":false,\"accumulated_seed\":\"de82487ff4990485f1c0f7c2c708ba3986dc785182739d1b41bd0df91f9f94e6\",\"era_end\":null,\"timestamp\":\"2023-03-21T11:03:25.952Z\",\"era_id\":31,\"height\":348,\"protocol_version\":\"1.0.0\"},\"body\":{\"proposer\":\"01c5d4320de80e6862841b594b9cce59574f3e5a9739f3f1d774b924b6f44ea68b\",\"deploy_hashes\":[],\"transfer_hashes\":[\"143eac3dd8b0a14094ccc4c3a6c4ee8fe848eafaed08f4f8ed52561067c3b015\"]},\"proofs\":[]}}}";
        Object eventRoot = new ObjectMapper().readValue(json, Class.forName("com.casper.sdk.service.impl.event.EventRoot"));
        assertThat(eventRoot, is(notNullValue()));
        Method getData = eventRoot.getClass().getDeclaredMethod("getData");
        getData.setAccessible(true);
        BlockAdded blockAdded = (BlockAdded) getData.invoke(eventRoot);
        assertThat(blockAdded, is(notNullValue()));

        assertThat(blockAdded.getBlockHash().toString(), is("f75da3af54e9d6884f158b9c54b292151234fcdbd237954d5095d8aa95b425c9"));
        assertThat(blockAdded.getBlock().getHash().toString(), is("f75da3af54e9d6884f158b9c54b292151234fcdbd237954d5095d8aa95b425c9"));
        assertThat(blockAdded.getBlock().getHeader().getParentHash().toString(), is("02e177fb0ce77b8c1082d7832847079e8e31e7115dd0a9ac5bfef84804ef96c6"));
        assertThat(blockAdded.getBlock().getHeader().getStateRootHash().toString(), is("4fb73294ee06b0768dd127f073b425bb09f4a4b9a9105126191950b3ddccaa3a"));
        assertThat(blockAdded.getBlock().getHeader().getBodyHash().toString(), is("e459ad9d5b7b6212d85583de647f7b047c048dec211b3a2f177d76e0393430f3"));
        // Random bit missing
        // assertThat(blockAdded.getBlock().getHeader().getRandomBit(), is(false));
        assertThat(blockAdded.getBlock().getHeader().getAccumulatedSeed().toString(), is("de82487ff4990485f1c0f7c2c708ba3986dc785182739d1b41bd0df91f9f94e6"));
        assertThat(blockAdded.getBlock().getHeader().getEraEnd(), is(nullValue()));
        assertThat(blockAdded.getBlock().getHeader().getTimeStamp(), is(notNullValue()));
        assertThat(blockAdded.getBlock().getHeader().getTimeStamp(), is(notNullValue()));
        assertThat(blockAdded.getBlock().getHeader().getEraId(), is(31L));
        assertThat(blockAdded.getBlock().getHeader().getHeight(), is(348L));
        assertThat(blockAdded.getBlock().getHeader().getProtocolVersion(), is("1.0.0"));
        assertThat(blockAdded.getBlock().getBody().getProposer().getAlgoTaggedHex(), is("01c5d4320de80e6862841b594b9cce59574f3e5a9739f3f1d774b924b6f44ea68b"));
        assertThat(blockAdded.getBlock().getBody().getDeployHashes(), hasSize(0));
        assertThat(blockAdded.getBlock().getBody().getTransferHashes(), hasSize(1));
        assertThat(blockAdded.getBlock().getBody().getTransferHashes().get(0), is("143eac3dd8b0a14094ccc4c3a6c4ee8fe848eafaed08f4f8ed52561067c3b015"));
    }


    /**
     * Obtains the user key from nctl assets folder
     */
    public static URL getUserKeyAsset(final int networkId, final int userId, final String keyFilename) {
        String path = String.format("/net-%d/user-%d/%s", networkId, userId, keyFilename);
        return Objects.requireNonNull(AssetUtils.class.getResource(path), "missing resource " + path);
    }
}
