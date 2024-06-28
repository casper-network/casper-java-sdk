package com.casper.sdk.service;

import com.casper.sdk.exception.CasperInvalidStateException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.helper.CasperTransferHelper;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.block.HeightBlockIdentifier;
import com.casper.sdk.identifier.global.GlobalStateIdentifier;
import com.casper.sdk.identifier.global.StateRootHashIdentifier;
import com.casper.sdk.identifier.purse.MainPurseUnderPublickey;
import com.casper.sdk.identifier.purse.PurseIdentifier;
import com.casper.sdk.model.balance.QueryBalanceData;
import com.casper.sdk.model.block.JsonBlockData;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.DeployResult;
import com.casper.sdk.model.era.EraInfoData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.status.ChainspecData;
import com.casper.sdk.model.status.StatusData;
import com.syntifi.crypto.key.*;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class CasperServiceTestsNctl  extends AbstractJsonRpcTests {

    /**
     * Test if get block matches requested by height
     */
    @Test
    void testIfBlockReturnedMatchesRequestedByHeight() {
        int blocks_to_check = 3;
        for (int i = 0; i < blocks_to_check; i++) {
            JsonBlockData result = casperServiceNctl.getBlock(new HeightBlockIdentifier(i));
            assertEquals(result.getBlock().getHeader().getHeight(), i);
        }
    }

    @Test
    void getStatus() {
        StatusData status = casperServiceNctl.getStatus();
        assertNotNull(status);
        assertNotNull(status.getLastAddedBlockInfo());
        assertNotNull(status.getStartStateRootHash());
    }

    @Test
    void getEraSummaryByHash() {
        JsonBlockData block = casperServiceNctl.getBlock(new HeightBlockIdentifier(10));
        EraInfoData eraInfoData = casperServiceNctl.getEraSummary(new HashBlockIdentifier(block.getBlock().getHash().toString()));

        assertNotNull(eraInfoData);
        assertNotNull(eraInfoData.getEraSummary());
    }

    @Test
    void getEraSummaryByHeight() {
        EraInfoData eraInfoData = casperServiceNctl.getEraSummary(new HeightBlockIdentifier(10));

        assertNotNull(eraInfoData);
        assertNotNull(eraInfoData.getEraSummary());
    }

    @Test
    void queryBalance() throws IOException {


        final URL privateKeyAccount1 = getClass().getResource("/assets/users/user-1/secret_key.pem");

        Assertions.assertNotNull(privateKeyAccount1);
        final AbstractPrivateKey privateKey = new Ed25519PrivateKey();
        privateKey.readPrivateKey(privateKeyAccount1.getFile());
        PublicKey publicKeyAccount1 = PublicKey.fromAbstractPublicKey(privateKey.derivePublicKey());
        GlobalStateIdentifier stateRootHashIdentifier = StateRootHashIdentifier.builder()
                .hash(casperServiceNctl.getStateRootHash().getStateRootHash())
                .build();
        PurseIdentifier mainPurseUnderPublickey = MainPurseUnderPublickey.builder()
                .publicKey(publicKeyAccount1)
                .build();
        QueryBalanceData balanceData = casperServiceNctl.queryBalance(stateRootHashIdentifier, mainPurseUnderPublickey);
        assertNotNull(balanceData);
        assertNotEquals(balanceData.getBalance(), BigInteger.ZERO);
    }

    @Test
    void getChainspec() {
        ChainspecData chainspec = casperServiceNctl.getChainspec();
        assertNotNull(chainspec);
        assertNotNull(chainspec.getChainspec().getChainspecBytes());
    }

    @Test
    void getChainspecAndSaveAsFile() throws IOException {
        ChainspecData chainspec = casperServiceNctl.getChainspec();
        Path temp = Files.createTempFile("chainspec_", ".toml");
        chainspec.saveChainspec(temp);
        assertTrue(Files.exists(temp));
        assertTrue(Files.size(temp) > 0);

        String fileContent = new String(Files.readAllBytes(temp));
        assertTrue(fileContent.contains("[protocol]"));
    }

    @Test
    void getChainspecAndReadAsString() {
        ChainspecData chainspec = casperServiceNctl.getChainspec();
        String chainspecString = chainspec.readChainspecBytesToString();
        assertNotNull(chainspecString);
        assertFalse(chainspecString.isEmpty());
        assertTrue(chainspecString.contains("[protocol]"));
    }

    @Test
    void getGenesisAccountsAndSaveAsFile() throws IOException {
        ChainspecData chainspec = casperServiceNctl.getChainspec();
        Path temp = Files.createTempFile("accounts_", ".toml");
        if (chainspec.getChainspec().getGenesisAccountsBytes() != null && chainspec.getChainspec().getGenesisAccountsBytes().getDigest() != null) {
            chainspec.saveGenesisAccounts(temp);
            assertTrue(Files.exists(temp));
            assertTrue(Files.size(temp) > 0);

            String fileContent = new String(Files.readAllBytes(temp));
            assertTrue(fileContent.contains("[accounts]"));
        } else {
            assertThrowsExactly(CasperInvalidStateException.class, () -> chainspec.saveGenesisAccounts(temp));
        }
    }

    @Test
    void getGenesisAccountsAndReadAsString() {
        ChainspecData chainspec = casperServiceNctl.getChainspec();
        if (chainspec.getChainspec().getGenesisAccountsBytes() != null && chainspec.getChainspec().getGenesisAccountsBytes().getDigest() != null) {
            String genesisAccountsString = chainspec.readGenesisAccountsBytesToString();
            assertNotNull(genesisAccountsString);
            assertFalse(genesisAccountsString.isEmpty());
            assertTrue(genesisAccountsString.contains("[accounts]"));
        } else {
            assertThrowsExactly(CasperInvalidStateException.class, chainspec::readGenesisAccountsBytesToString);
        }
    }

    @Test
    void getGlobalStateAndSaveAsFile() throws IOException {
        ChainspecData chainspec = casperServiceNctl.getChainspec();
        Path temp = Files.createTempFile("global_state_", ".toml");

        // TODO: Validate this when we have a chainspec with global state
        if (chainspec.getChainspec().getGlobalStateBytes() != null && chainspec.getChainspec().getGlobalStateBytes().getDigest() != null) {
            chainspec.saveGlobalState(temp);
            assertTrue(Files.exists(temp));
            assertTrue(Files.size(temp) > 0);

            String fileContent = new String(Files.readAllBytes(temp));
            assertTrue(fileContent.length() > 0);
        } else {
            assertThrowsExactly(CasperInvalidStateException.class, () -> chainspec.saveGlobalState(temp));
        }
    }

    @Test
    void getGlobalStateAndReadAsString() {
        ChainspecData chainspec = casperServiceNctl.getChainspec();

        // TODO: Validate this when we have a chainspec with global state
        if (chainspec.getChainspec().getGlobalStateBytes() != null && chainspec.getChainspec().getGlobalStateBytes().getDigest() != null) {
            String globalStateString = chainspec.readGlobalStateBytesToString();
            assertNotNull(globalStateString);
            assertFalse(globalStateString.isEmpty());
            assertTrue(globalStateString.contains("[protocol]"));
        } else {
            assertThrowsExactly(CasperInvalidStateException.class, chainspec::readGlobalStateBytesToString);
        }
    }

    /**
     * We're testing the 50% Secp problem here
     * When a Secp public key is generated from prime numbers, there's a 50% chance it will 65bit or 66bit
     * When it's 65% the long public key is padded with zero
     * This test uses a Secp private key that will generate a 65bit padded public key
     */
    @Test
    void testDeployWithPaddedPublicKey() throws URISyntaxException, IOException, NoSuchTypeException, GeneralSecurityException, ValueSerializationException {

        DeployData deploy;

        //First fund the private-padded.pem account from the faucet account
        final Ed25519PrivateKey faucetPrivateKey = new Ed25519PrivateKey();
        final URL faucetKey = getClass().getResource("/net-1/faucet/secret_key.pem");
        assert faucetKey != null;
        faucetPrivateKey.readPrivateKey(faucetKey.getFile());

        Secp256k1PrivateKey paddedPrivateKey = new Secp256k1PrivateKey();
        String filePath = getResourcesKeyPath("secp256k1/private-padded.pem");
        paddedPrivateKey.readPrivateKey(filePath);


        byte[] paddedPublicKeyFull = paddedPrivateKey.getKeyPair().getPublicKey().toByteArray();
        assert paddedPublicKeyFull[0] == (byte) 0;

        DeployResult deployResult = doDeploy(faucetPrivateKey, paddedPrivateKey.derivePublicKey());

        assert deployResult != null;

        //wait for deploy to be accepted
        do {
            deploy = casperServiceNctl.getDeploy(deployResult.getDeployHash());
        } while (deploy.getDeploy().getApprovals() == null || deploy.getDeploy().getApprovals().size() <= 0);


        //Now transfer from private-padded.pem to another user
        Secp256k1PrivateKey toPrivateKey = new Secp256k1PrivateKey();
        filePath = getResourcesKeyPath("secp256k1/secret_key.pem");
        toPrivateKey.readPrivateKey(filePath);

        deployResult = doDeploy(paddedPrivateKey, toPrivateKey.derivePublicKey());

        assert deployResult != null;

        //wait for deploy to be accepted
        do {
            deploy = casperServiceNctl.getDeploy(deployResult.getDeployHash());
        } while (deploy.getDeploy().getApprovals() == null || deploy.getDeploy().getApprovals().size() <= 0);


        assert Arrays.equals(deploy.getDeploy().getApprovals().get(0).getSigner().getPubKey().getKey(), paddedPrivateKey.derivePublicKey().getKey());

        //now verify signature

        Secp256k1PublicKey paddedPublicKey = (Secp256k1PublicKey) paddedPrivateKey.derivePublicKey();

        assert paddedPublicKey.verify(deploy.getDeploy().getHash().getDigest(), deploy.getDeploy().getApprovals().get(0).getSignature().getKey());

    }

    private DeployResult doDeploy(final AbstractPrivateKey sk, final AbstractPublicKey pk) throws NoSuchTypeException, GeneralSecurityException, ValueSerializationException {

        final Deploy deploy = CasperTransferHelper.buildTransferDeploy(
                sk,
                PublicKey.fromAbstractPublicKey(pk),
                BigInteger.valueOf(2500000000L),
                "casper-net-1",
                Math.abs(new Random().nextLong()),
                BigInteger.valueOf(100000000L),
                1L,
                Ttl.builder().ttl("30m").build(),
                new Date(),
                new ArrayList<>());

        return casperServiceNctl.putDeploy(deploy);
    }


    protected String getResourcesKeyPath(String filename) throws URISyntaxException {
        return Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).toURI()).toString();
    }

}
