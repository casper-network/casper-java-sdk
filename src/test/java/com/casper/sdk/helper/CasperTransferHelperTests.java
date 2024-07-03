package com.casper.sdk.helper;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.deploy.SpeculativeDeployData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.service.AbstractJsonRpcTests;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Casper Deploy Service test
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
@Disabled
public class CasperTransferHelperTests extends AbstractJsonRpcTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(CasperTransferHelperTests.class);

    /**
     * Loads test key file from resources
     *
     * @param filename the file name
     * @return a string with file path from resources
     * @throws URISyntaxException thrown if it can't parse file url to URI for fetching the path
     */
    protected String getResourcesKeyPath(String filename) throws URISyntaxException {
        return Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).toURI()).toString();
    }

    @Test
    void testTransferOnTestnet() throws IOException, NoSuchTypeException, GeneralSecurityException, URISyntaxException, ValueSerializationException {
        Ed25519PrivateKey alice = new Ed25519PrivateKey();
        Ed25519PrivateKey bob = new Ed25519PrivateKey();
        alice.readPrivateKey(getResourcesKeyPath("deploy-accounts/Alice_SyntiFi_secret_key.pem"));
        bob.readPrivateKey(getResourcesKeyPath("deploy-accounts/Bob_SyntiFi_secret_key.pem"));

        long id = Math.abs(new Random().nextInt());
        Ttl ttl = Ttl
                .builder()
                .ttl("30m")
                .build();
        Random rnd = new Random();
        boolean coin = rnd.nextBoolean();
        Ed25519PrivateKey from;
        PublicKey to;
        if (coin) {
            from = alice;
            to = PublicKey.fromAbstractPublicKey(bob.derivePublicKey());
        } else {
            from = bob;
            to = PublicKey.fromAbstractPublicKey(alice.derivePublicKey());
        }

        Deploy deploy = CasperTransferHelper.buildTransferDeploy(from, to,
                BigInteger.valueOf(2500000000L), "casper-test",
                id, BigInteger.valueOf(100000000L), 1L, ttl, new Date(),
                new ArrayList<>());
        DeployResult deployResult = casperServiceTestnet.putDeploy(deploy);
        assertEquals(deployResult.getDeployHash(), Hex.toHexString(deploy.getHash().getDigest()));
    }

    @Test
    void testTransferOnNctl() throws IOException, NoSuchTypeException, GeneralSecurityException, URISyntaxException, ValueSerializationException {
        Ed25519PrivateKey user1 = new Ed25519PrivateKey();
        Ed25519PrivateKey user2 = new Ed25519PrivateKey();
        user1.readPrivateKey(getResourcesKeyPath("assets/users/user-1/secret_key.pem"));
        user2.readPrivateKey(getResourcesKeyPath("assets/users/user-2/secret_key.pem"));

        long id = Math.abs(new Random().nextInt());
        Ttl ttl = Ttl
                .builder()
                .ttl("30m")
                .build();
        Random rnd = new Random();
        boolean coin = rnd.nextBoolean();
        Ed25519PrivateKey from;
        PublicKey to;
        if (coin) {
            from = user1;
            to = PublicKey.fromAbstractPublicKey(user2.derivePublicKey());
        } else {
            from = user2;
            to = PublicKey.fromAbstractPublicKey(user1.derivePublicKey());
        }

        Deploy deploy = CasperTransferHelper.buildTransferDeploy(from, to,
                BigInteger.valueOf(2500000000L), "casper-net-1",
                id, BigInteger.valueOf(100000000L), 1L, ttl, new Date(),
                new ArrayList<>());
        DeployResult deployResult = casperServiceNctl.putDeploy(deploy);
        assertEquals(deployResult.getDeployHash(), Hex.toHexString(deploy.getHash().getDigest()));
    }

    @Test
    void testSpeculativeTransferOnNctl() throws IOException, NoSuchTypeException, GeneralSecurityException, URISyntaxException, ValueSerializationException {
        Ed25519PrivateKey user1 = new Ed25519PrivateKey();
        Ed25519PrivateKey user2 = new Ed25519PrivateKey();
        user1.readPrivateKey(getResourcesKeyPath("assets/users/user-1/secret_key.pem"));
        user2.readPrivateKey(getResourcesKeyPath("assets/users/user-2/secret_key.pem"));

        long id = Math.abs(new Random().nextInt());
        Ttl ttl = Ttl
                .builder()
                .ttl("30m")
                .build();
        Random rnd = new Random();
        boolean coin = rnd.nextBoolean();
        Ed25519PrivateKey from;
        PublicKey to;
        if (coin) {
            from = user1;
            to = PublicKey.fromAbstractPublicKey(user2.derivePublicKey());
        } else {
            from = user2;
            to = PublicKey.fromAbstractPublicKey(user1.derivePublicKey());
        }

        Deploy deploy = CasperTransferHelper.buildTransferDeploy(from, to,
                BigInteger.valueOf(2500000000L), "casper-net-1",
                id, BigInteger.valueOf(100000000L), 1L, ttl, new Date(),
                new ArrayList<>());
        SpeculativeDeployData deployData = speculativeCasperServiceNctl.speculativeExec(deploy);
        assertNotNull(deployData);
        assertEquals(1, deployData.getExecutionResult().getCost().compareTo(BigInteger.ZERO));
    }

    @Test
    void testTransferWithNullIdOnTestnet() throws IOException, NoSuchTypeException, GeneralSecurityException, URISyntaxException, ValueSerializationException {
        Ed25519PrivateKey alice = new Ed25519PrivateKey();
        Ed25519PrivateKey bob = new Ed25519PrivateKey();
        alice.readPrivateKey(getResourcesKeyPath("deploy-accounts/Alice_SyntiFi_secret_key.pem"));
        bob.readPrivateKey(getResourcesKeyPath("deploy-accounts/Bob_SyntiFi_secret_key.pem"));

        Ttl ttl = Ttl
                .builder()
                .ttl("30m")
                .build();
        Random rnd = new Random();
        boolean coin = rnd.nextBoolean();
        Ed25519PrivateKey from;
        PublicKey to;
        if (coin) {
            from = alice;
            to = PublicKey.fromAbstractPublicKey(bob.derivePublicKey());
        } else {
            from = bob;
            to = PublicKey.fromAbstractPublicKey(alice.derivePublicKey());
        }

        Deploy deploy = CasperTransferHelper.buildTransferDeploy(from, to,
                BigInteger.valueOf(2500000000L), "casper-test",
                null, BigInteger.valueOf(100000000L), 1L, ttl, new Date(),
                new ArrayList<>());
        DeployResult deployResult = casperServiceTestnet.putDeploy(deploy);
        LOGGER.debug("deploy hash: " + deployResult.getDeployHash());
        assertEquals(deployResult.getDeployHash(), Hex.toHexString(deploy.getHash().getDigest()));
    }


}
