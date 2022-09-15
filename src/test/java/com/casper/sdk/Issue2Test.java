package com.casper.sdk;

import com.casper.sdk.how_to.HowToUtils;
import com.casper.sdk.service.serialization.cltypes.CLValueBuilder;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.types.ByteSerializerFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.service.serialization.util.CollectionUtils;
import com.casper.sdk.types.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.time.Instant;
import java.util.List;

import static com.casper.sdk.how_to.HowToUtils.getUserKeyPairStreams;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author ian@meywood.com
 */
public class Issue2Test {
    /**
     * The SDK under test the NCTL test nodes must be running for these tests to execute
     */
    private CasperSdk casperSdk;

    private final ByteSerializerFactory serializerFactory = new ByteSerializerFactory();

    private final TypesFactory typesFactory = new TypesFactory();


    @BeforeEach
    void setUp() {
        casperSdk = new CasperSdk("http://localhost", 11101);
    }




    @Test
    @Disabled
    void testIssue2() throws IOException {

        // Dispatch deploy to a node.
        Digest contractHash = new ContractHash("6b6f1b4a38d94956154d20089842ca69f891ea44322df9d20921015ce711dc34");

        System.out.println("ContractHash: " + contractHash);

        byte[] k1Bytes = ByteUtils.decodeHex("e07cA98F1b5C15bC9ce75e8adB8a3b4D334A1B1Fa14DD16CfD3320bf77Cc3aAb");
        final CLValue key1 = CLValueBuilder.byteArray(k1Bytes);


        byte[] k2Bytes = ByteUtils.decodeHex("e3D394334Ce46C6043BCd33E4686D2B7a369C606BfCce4C26ca14d2C73Fac824");
        final CLValue key2 = CLValueBuilder.byteArray(k2Bytes);
        final CLValue value = CLValueBuilder.u256(0.4e6);


        final KeyPair platformKeyPair = getNodeKeyPair(1);

        CLMap map1 = CLValueBuilder.map(CollectionUtils.Map.of(key1, value));
        CLMap map2 = CLValueBuilder.map(CollectionUtils.Map.of(key2, value));

        byte[] map1Bytes = map1.getBytes();
        byte[] map2Byte = map2.getBytes();

        byte[] clMap1Bytes = serializerFactory.getByteSerializer(map1).toBytes(map1);
        // TODO check the same as with the JS SDK

        final DeployNamedArg assetHolders = new DeployNamedArg("asset_holders", map1);
        final DeployNamedArg liabilityHolders = new DeployNamedArg("liability_holders", map2);

        // Test the bytes from both CLMap named args
        byte[] assertHoldersBytes = serializerFactory.getByteSerializer(assetHolders).toBytes(assetHolders);

        // Assert assertHoldersBytes match expected
        // TODO

        byte[] liabilityHoldersBytes = serializerFactory.getByteSerializer(liabilityHolders).toBytes(liabilityHolders);

        // Assert liabilityHoldersBytes match expected
        // TODO

        final List<DeployNamedArg> namedArgs = new DeployNamedArgBuilder()
                .add("token_id", CLValueBuilder.string("token-id"))
                .add("instrument_id", CLValueBuilder.string("c9536033-386a-4bed-9b57-fd67c3d49dc1"))
                .add("asset_decimals", CLValueBuilder.u256(1))
                .add("asset_units", CLValueBuilder.u256(50000))
                .add(assetHolders)
                .add("liability_decimals", CLValueBuilder.u256(1))
                .add("liability_units", CLValueBuilder.u256(40000))
                .add(liabilityHolders)
                .build();

        byte[] namedArgsBytes = serializerFactory.getByteSerializer(namedArgs).toBytes(namedArgs);

        // Assert namedArgsBytes match expected
        // TODO


        final Deploy deploy = casperSdk.makeDeploy(
                new DeployParams(
                        platformKeyPair.getPublic(), "casper-net-1",
                        1,
                        Instant.now().toEpochMilli(),
                        DeployParams.DEFAULT_TTL,
                        null
                ),
                new StoredContractByHash(
                        new ContractHash(contractHash.getHash()),
                        "set_state",
                        namedArgs),
                casperSdk.standardPayment(new BigInteger("10000000000"))
        );

        assertThat(deploy, is(notNullValue()));


        Digest hash = deploy.getHash();
    }

    private KeyPair geUserKeyPair(int userNumber) throws IOException {
        final KeyPairStreams streams = getUserKeyPairStreams(userNumber);
        return casperSdk.loadKeyPair(streams.getPublicKeyIn(), streams.getPrivateKeyIn());
    }

    private KeyPair getNodeKeyPair(final int nodeNumber) throws IOException {
        final KeyPairStreams streams = HowToUtils.getNodeKeyPairStreams(nodeNumber);
        return casperSdk.loadKeyPair(streams.getPublicKeyIn(), streams.getPrivateKeyIn());
    }
}
