package com.casper.sdk.how_to;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.KeyPairStreams;
import com.casper.sdk.types.URef;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.math.BigInteger;
import java.security.PublicKey;

import static com.casper.sdk.how_to.HowToUtils.getUserKeyPairStreams;

/**
 * Uses Local Network Testing network/node control to demonstrate querying a node
 *
 * @see <a href="https://docs.casperlabs.io/en/latest/dapp-dev-guide/setup-nctl.html"></a>
 */
@Disabled // Remove this to run locally
public class HowToQueryANode {

    /** Create new instance of the SDK with default NCTL url and port */
    final CasperSdk casperSdk = new CasperSdk("http://localhost", 40101);

    @Test
    public void testGetAuctionInfo() {

        final String auctionInfo = casperSdk.getAuctionInfo();

        assert (auctionInfo != null);

    }

    @Test
    public void testGetNodeStatus() {

        final String nodeStatus = casperSdk.getNodeStatus();

        assert (nodeStatus != null);

    }

    @Test
    public void testGetNodePeers() {

        final String nodePeers = casperSdk.getNodePeers();

        assert (nodePeers != null);
    }

    @Test
    public void testGetStateRootHash() {

        final String stateRootHash = casperSdk.getStateRootHash();

        assert (stateRootHash != null);

    }

    @Test
    public void testGetAccountMainPurseURef() throws Throwable {

        final InputStream publicKeyIn = getUserKeyPairStreams(1).getPublicKeyIn();
        final PublicKey publicKey = casperSdk.loadKey(publicKeyIn);
        final URef accountMainPurseURef = casperSdk.getAccountMainPurseURef(publicKey);

        assert (accountMainPurseURef != null);
    }

    @Test
    public void testGetAccountHash() throws Throwable {

        final KeyPairStreams userKeyPairStreams = getUserKeyPairStreams(1);
        final PublicKey publicKey = casperSdk.loadKey(userKeyPairStreams.getPublicKeyIn());
        final String accountHash = casperSdk.getAccountHash(publicKey);

        assert (accountHash != null);

    }

    @Test
    public void testGetAccountBalance() throws Throwable {

        final PublicKey publicKey = casperSdk.loadKey(
                getUserKeyPairStreams(1).getPublicKeyIn()
        );
        final BigInteger accountBalance = casperSdk.getAccountBalance(publicKey);

        assert (accountBalance != null);

    }

    @Test
    public void testGetAccountInfo() throws Throwable {

        final PublicKey publicKey = casperSdk.loadKey(
                getUserKeyPairStreams(1).getPublicKeyIn()
        );
        final String accountInfo = casperSdk.getAccountInfo(publicKey);

        assert (accountInfo != null);
    }
}
