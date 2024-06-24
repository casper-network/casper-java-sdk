package com.casper.sdk.service;

import com.casper.sdk.exception.CasperInvalidStateException;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.block.HeightBlockIdentifier;
import com.casper.sdk.identifier.global.GlobalStateIdentifier;
import com.casper.sdk.identifier.global.StateRootHashIdentifier;
import com.casper.sdk.identifier.purse.MainPurseUnderPublickey;
import com.casper.sdk.identifier.purse.PurseIdentifier;
import com.casper.sdk.model.balance.QueryBalanceData;
import com.casper.sdk.model.block.ChainGetBlockResult;
import com.casper.sdk.model.era.EraInfoData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.status.ChainspecData;
import com.casper.sdk.model.status.StatusData;
import com.syntifi.crypto.key.AbstractPrivateKey;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

@Disabled
public class CasperServiceTestsNctl extends AbstractJsonRpcTests {

    /**
     * Test if get block matches requested by height
     */
    @Test
    void testIfBlockReturnedMatchesRequestedByHeight() {
        int blocks_to_check = 3;
        for (int i = 0; i < blocks_to_check; i++) {
            ChainGetBlockResult result = casperServiceNctl.getBlock(new HeightBlockIdentifier(i));
            assertEquals(result.getBlockWithSignatures().getBlock().getHeader().getHeight(), i);
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
        ChainGetBlockResult block = casperServiceNctl.getBlock(new HeightBlockIdentifier(10));
        EraInfoData eraInfoData = casperServiceNctl.getEraSummary(new HashBlockIdentifier(block.getBlockWithSignatures().getBlock().getHash().toString()));

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
        PurseIdentifier mainPurseUnderPublicKey = MainPurseUnderPublickey.builder()
                .publicKey(publicKeyAccount1)
                .build();
        QueryBalanceData balanceData = casperServiceNctl.queryBalance(stateRootHashIdentifier, mainPurseUnderPublicKey);
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
            assertFalse(fileContent.isEmpty());
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


}
