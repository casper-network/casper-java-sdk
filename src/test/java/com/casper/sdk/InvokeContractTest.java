package com.casper.sdk;

import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.types.*;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.time.Instant;

import static com.casper.sdk.IntegrationTestUtils.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

/**
 * Integration tests for invoking a contract
 */
public class InvokeContractTest {

    private static final long AMOUNT_TO_TRANSFER = 2000000000L;

    /**
     * Test that gives an example of using a
     */
    @Test
    void invokeContract() throws Throwable {

        // Step 1: Set casper node client.
        final CasperSdk casperSdk = new CasperSdk("http://localhost", 11101);

        // Step 2: Set contract operator key pair.
        final AsymmetricCipherKeyPair userTwoKeyPair = geUserKeyPair(casperSdk, 2);
        final AsymmetricCipherKeyPair nodeOneKeyPair = getNodeKeyPair(casperSdk, 1);

        // Step 3: Query node for global state root hash.
        final String stateRootHash = casperSdk.getStateRootHash();

        final PublicKey fromPublicKey = new PublicKey(((Ed25519PublicKeyParameters) nodeOneKeyPair.getPublic()).getEncoded(), KeyAlgorithm.ED25519);

        // Step 4: Query node for contract hash.
        final String accountHex = getPublicKeyAccountHex(userTwoKeyPair);
        final byte[] contractHash = casperSdk.getContractHash(accountHex);

        // Make a payment
        final ModuleBytes payment = casperSdk.standardPayment(new BigInteger("10000000000"));

        // Create the transfer
        final Deploy deploy = casperSdk.makeDeploy(
                new DeployParams(
                        fromPublicKey,
                        "casper-net-1",
                        10,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null),
                new StoredContractByHash(
                        new ContractHash(contractHash),
                        "transfer",
                        new DeployNamedArgBuilder()
                                .add("amount", CLValueBuilder.u256(AMOUNT_TO_TRANSFER))
                                .add("recipient", CLValueBuilder.byteArray(accountHex))
                                .build()),
                payment
        );

        // Step 5.2: Sign deploy.
        casperSdk.signDeploy(deploy, nodeOneKeyPair);
        casperSdk.signDeploy(deploy, userTwoKeyPair);

        final Digest digest = casperSdk.putDeploy(deploy);

        assertThat(digest, is(notNullValue()));
    }


    private AsymmetricCipherKeyPair geUserKeyPair(final CasperSdk casperSdk, int userNumber) throws IOException {
        final IntegrationTestUtils.KeyPairStreams streams = geUserKeyPairStreams(userNumber);
        return casperSdk.loadKeyPair(streams.getPublicKeyIn(), streams.getPrivateKeyIn());
    }

    private AsymmetricCipherKeyPair getNodeKeyPair(final CasperSdk casperSdk, final int nodeNumber) throws IOException {
        final IntegrationTestUtils.KeyPairStreams streams = getNodeKeyPairSteams(nodeNumber);
        return casperSdk.loadKeyPair(streams.getPublicKeyIn(), streams.getPrivateKeyIn());
    }
}
