package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.e2e.utils.ContextMap;
import com.casper.sdk.model.peer.PeerData;
import com.casper.sdk.model.peer.PeerEntry;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * The Step definitions for info_get_peers tests.
 *
 * @author ian@meywood.com
 */
public class InfoGetPeersStepDefinitions {

    private final ContextMap contextMap = ContextMap.getInstance();
    private final Logger logger = LoggerFactory.getLogger(InfoGetPeersStepDefinitions.class);


    @Given("that the info_get_peers RPC method is invoked against a node")
    public void thatTheInfo_get_peersRPCMethodIsInvokedAgainstANode() {
        logger.info("Given that the info_get_peers RPC method is invoked against a node");
        final PeerData peerData = CasperClientProvider.getInstance().getCasperService().getPeerData();
        contextMap.put(StepConstants.PEER_DATA, peerData);
    }

    @Then("the node returns an info_get_peers_result")
    public void theNodeReturnsAnInfo_get_peers_result() {
        logger.info("Then the node returns an info_get_peers_result");
        assertThat(getPeerData(), is(notNullValue()));
    }

    @And("the info_get_peers_result has an API version of {string}")
    public void theInfo_get_peers_resultHasAnAPIVersionOf(final String apiVersion) {
        logger.info("And the info_get_peers_result has an API version of {}", apiVersion);
        assertThat(getPeerData().getApiVersion(), is(apiVersion));
    }

    @And("the info_get_peers_result contains {int} peers")
    public void theInfo_get_peers_resultContainsPeers(final int peerCount) {
        logger.info("And info_get_peers_result contains {} peers", peerCount);
        assertThat(getPeerData().getPeers(), hasSize(peerCount));
    }

    @And("the info_get_peers_result contains a valid peer with a port number of {int}")
    public void theInfo_get_peers_resultContainAPeerWithAPortNumberOf(final int port) {
        logger.info("And the info_get_peers_result contains a valid peer with a port number of {}", port);
        final Optional<PeerEntry> match = getPeerData().getPeers().stream()
                .filter(peerEntry -> isValidPeer(port, peerEntry))
                .findFirst();
        assertThat(match.isPresent(), is(true));
    }

    private static boolean isValidPeer(final int port, final PeerEntry peerEntry) {
        return peerEntry.getAddress().endsWith(":" + port) && peerEntry.getNodeId().startsWith("tls:");
    }

    private PeerData getPeerData() {
        return contextMap.get(StepConstants.PEER_DATA);
    }
}
