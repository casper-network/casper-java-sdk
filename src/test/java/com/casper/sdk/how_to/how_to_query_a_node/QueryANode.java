package com.casper.sdk.how_to.how_to_query_a_node;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.how_to.common.Methods;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Uses Local Network Testing network/node control to demonstrate querying a node
 *
 * @see <a href="https://docs.casperlabs.io/en/latest/dapp-dev-guide/setup-nctl.html"></a>
 */
@Disabled // Remove this to run locally
public class QueryANode extends Methods {

    //Path to the NCTL utilities, change to mach your implementation
    private final static String NCTL_HOME = "~/casper-node/utils/nctl";
    //Create new instance of the SDK with default NCTL url and port
    final CasperSdk casperSdk = new CasperSdk("http://localhost", 40101);

    @Test
    public void testGetAuctionInfo() throws Throwable {

        final String auctionInfo = casperSdk.getAuctionInfo();

        assert (auctionInfo != null);

    }

    @Test
    public void testGetNodeStatus() throws Throwable {

        final String nodeStatus = casperSdk.getNodeStatus();

        assert (nodeStatus != null);

    }

    @Test
    public void testGetNodePeers() throws Throwable {

        final String nodePeers = casperSdk.getNodePeers();

        assert (nodePeers != null);

    }

    @Test
    public void testGetStateRootHash() throws Throwable {

        final String stateRootHash = casperSdk.getStateRootHash();

        assert (stateRootHash != null);

    }

    @Test
    public void testGetAccountMainPurseURef() throws Throwable {

        final String accountMainPurseURef = casperSdk.getAccountMainPurseURef(
                getUserKeyPair(1, NCTL_HOME, casperSdk).getPublic()
        );

        assert (accountMainPurseURef != null);

    }

    @Test
    public void testGetAccountHash() throws Throwable {

        final String accountHash = casperSdk.getAccountHash(super.getPublicKeyAccountHex(
                super.getUserKeyPair(1, NCTL_HOME, casperSdk)));

        assert (accountHash != null);

    }

    @Test
    public void testGetAccountBalance() throws Throwable {

        final String accountBalance = casperSdk.getAccountBalance(
                super.getUserKeyPair(1, NCTL_HOME, casperSdk).getPublic()
        );

        assert (accountBalance != null);

    }

    @Test
    public void testGetAccountInfo() throws Throwable {

        final String accountInfo = casperSdk.getAccountInfo(
                super.getUserKeyPair(1, NCTL_HOME, casperSdk).getPublic()
        );

        assert (accountInfo != null);

    }

}
