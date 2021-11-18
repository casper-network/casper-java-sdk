package com.casper.sdk.how_to;

import com.casper.sdk.CasperSdk;
import com.casper.sdk.Constants;
import com.casper.sdk.KeyPairStreams;
import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.time.Instant;

import static com.casper.sdk.Constants.AMOUNT;
import static com.casper.sdk.Constants.RECIPIENT;
import static com.casper.sdk.how_to.HowToUtils.getUserKeyPairStreams;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

/**
 * Integration tests for invoking a contract
 */
@Disabled
public class HowToInvokeAContract {

    private static final long AMOUNT_TO_TRANSFER = 2000000000L;

    /**
     * Test that gives an example of using a
     */
    @Test
    void howToInvokeAContract() throws Throwable {

        // Step 1: Set casper node client.
        final CasperSdk casperSdk = new CasperSdk("http://localhost", 11101);

        // Step 2: Set contract operator key pair.
        final KeyPair userTwoKeyPair = geUserKeyPair(casperSdk, 2);
        final KeyPair nodeOneKeyPair = getNodeKeyPair(casperSdk, 1);

        // Step 3: Query node for global state root hash.
        final String stateRootHash = casperSdk.getStateRootHash();

        // Step 4: Query node for contract hash.
        final ContractHash contractHash = casperSdk.getContractHash(nodeOneKeyPair.getPublic());

        // Make a payment
        final ModuleBytes payment = casperSdk.standardPayment(new BigInteger("10000000000"));

        // Create the transfer
        final Deploy deploy = casperSdk.makeDeploy(
                new DeployParams(
                        nodeOneKeyPair.getPublic(),
                        "casper-net-1",
                        10,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null),
                new StoredContractByHash(
                        contractHash,
                        Constants.TRANSFER,
                        new DeployNamedArgBuilder()
                                .add(AMOUNT, CLValueBuilder.u256(AMOUNT_TO_TRANSFER))
                                .add(RECIPIENT, CLValueBuilder.byteArray(userTwoKeyPair.getPublic()))
                                .build()),
                payment
        );

        // Step 5.2: Sign deploy.
        casperSdk.signDeploy(deploy, nodeOneKeyPair);
        final Deploy signedDeploy = casperSdk.signDeploy(deploy, userTwoKeyPair);

        // Assert Approvals
        assertThat(signedDeploy.getApprovals().size(), is(2));
        final DeployApproval approval = signedDeploy.getApprovals().iterator().next();
        assertThat(approval.getSigner(), is(casperSdk.toCLPublicKey(userTwoKeyPair.getPublic())));

        final Digest digest = casperSdk.putDeploy(deploy);

        assertThat(digest, is(notNullValue()));
    }


    private KeyPair geUserKeyPair(final CasperSdk casperSdk, final int userNumber) throws IOException {
        final KeyPairStreams streams = getUserKeyPairStreams(userNumber);
        return casperSdk.loadKeyPair(streams.getPublicKeyIn(), streams.getPrivateKeyIn());
    }

    private KeyPair getNodeKeyPair(final CasperSdk casperSdk, final int nodeNumber) throws IOException {
        final KeyPairStreams streams = getUserKeyPairStreams(nodeNumber);
        return casperSdk.loadKeyPair(streams.getPublicKeyIn(), streams.getPrivateKeyIn());
    }
}
