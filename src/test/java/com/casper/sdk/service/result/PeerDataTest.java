package com.casper.sdk.service.result;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Unit tests for PeerData
 */
class PeerDataTest {

    @Test
    void jsonParsePeerData() throws IOException {

        final ObjectMapper objectMapper = new ObjectMapper();

        final PeerData peerData = objectMapper.readerFor(PeerData.class).readValue(PeerDataTest.class.getResourceAsStream("/com/casper/sdk/service/result/peer-data.json"));

        assertThat(peerData.getApiVersion(), is("1.0.0"));
        assertThat(peerData.getPeers(), hasSize(4));

        final Peer peerZero = peerData.getPeers().get(0);
        assertThat(peerZero.getNodeId(), is("tls:1148..8ea0"));
        assertThat(peerZero.getAddress(), is("127.0.0.1:22104"));

        final Peer peerThree = peerData.getPeers().get(3);
        assertThat(peerThree.getNodeId(), is("tls:a90c..21a3"));
        assertThat(peerThree.getAddress(), is("127.0.0.1:22105"));
    }
}