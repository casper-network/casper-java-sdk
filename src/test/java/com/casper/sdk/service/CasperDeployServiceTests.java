package com.casper.sdk.service;

import com.casper.sdk.exception.CLValueEncodeException;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.InvalidByteStringException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.DeployResult;
import com.syntifi.crypto.key.Ed25519PrivateKey;

import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

/**
 * Casper Deploy Service test
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
public class CasperDeployServiceTests extends AbstractJsonRpcTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(CasperDeployService.class);
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
    void testDeployTransferOnTestnet() throws IOException, InvalidByteStringException, CLValueEncodeException, NoSuchTypeException, GeneralSecurityException, DynamicInstanceException, URISyntaxException {
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

        Deploy deploy = CasperDeployService.buildTransferDeploy(from, to,
                BigInteger.valueOf(2500000000L), "casper-test",
                id, BigInteger.valueOf(100000000L), 1L, ttl, new Date(),
                new ArrayList<>());
        DeployResult deployResult =  casperServiceTestnet.putDeploy(deploy);
        Assertions.assertEquals(deployResult.getDeployHash(), Hex.toHexString(deploy.getHash().getDigest()));
    }
}
