package com.casper.sdk.service;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.block.HeightBlockIdentifier;
import com.casper.sdk.identifier.global.BlockHashIdentifier;
import com.casper.sdk.identifier.global.GlobalStateIdentifier;
import com.casper.sdk.model.account.AccountData;
import com.casper.sdk.model.auction.AuctionData;
import com.casper.sdk.model.balance.GetBalanceData;
import com.casper.sdk.model.block.JsonBlock;
import com.casper.sdk.model.block.JsonBlockData;
import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.casper.sdk.model.deploy.executabledeploy.StoredContractByHash;
import com.casper.sdk.model.deploy.executionresult.Success;
import com.casper.sdk.model.era.EraInfoData;
import com.casper.sdk.model.globalstate.GlobalStateData;
import com.casper.sdk.model.key.AlgorithmTag;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.peer.PeerData;
import com.casper.sdk.model.stateroothash.StateRootHashData;
import com.casper.sdk.model.storedvalue.StoredValueAccount;
import com.casper.sdk.model.storedvalue.StoredValueContract;
import com.casper.sdk.model.storedvalue.StoredValueData;
import com.casper.sdk.model.storedvalue.StoredValueDeployInfo;
import com.casper.sdk.model.transfer.Transfer;
import com.casper.sdk.model.transfer.TransferData;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.model.validator.ValidatorChangeData;
import com.casper.sdk.test.MockNode;
import com.casper.sdk.test.RcpResponseDispatcher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CasperService}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public class CasperServiceTests extends AbstractJsonRpcTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(CasperServiceTests.class);
    final MockNode mockNode = new MockNode();

    @BeforeEach
    public void before() throws Exception {
        mockNode.start(CasperNetwork.MOCK.getUri());
    }

    @AfterEach
    void tearDown() throws IOException {
        mockNode.shutdown();
    }

    /**
     * Test get block matches requested by hash.
     */
    @Test
    void testIfBlockReturnedMatchesRequestedByHash() {
        LOGGER.debug(String.format("Testing with block height %d", 2346915));

        final RcpResponseDispatcher when = mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block")
                .withBody("$.params.block_identifier.Height", "2346915")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block.json"));

        final JsonBlockData resultByHeight = casperServiceMock.getBlock(new HeightBlockIdentifier(2346915));
        final String hash = resultByHeight.getBlock().getHash().toString();

        when.clear()
                .withMethod("chain_get_block")
                .withBody("$.params.block_identifier.Hash", hash)
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block.json"));

        final JsonBlockData resultByHash = casperServiceMock.getBlock(new HashBlockIdentifier(hash));
        assertEquals(resultByHash.getBlock().getHash().toString(), hash);
    }

    /**
     * Test get public key serialization is correct.
     */
    @Test
    void testFirstBlocksPublicKeySerialization() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block")
                .withBody("$.params.block_identifier.Height", "23469150")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block.json"));

        final JsonBlockData result = casperServiceMock.getBlock(new HeightBlockIdentifier(23469150));
        final PublicKey key = result.getBlock().getBody().getProposer();

        assertEquals(AlgorithmTag.ED25519, key.getTag());
        assertEquals("01753af321afc6906fcd9c897e5328f66190b5842671f16b022b69f2ddb7619c32", key.getAlgoTaggedHex());
    }

    /**
     * Retrieve peers list and assert it has elements
     */
    @Test
    void retrieveNonEmptyListOfPeers() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("info_get_peers")
                .thenDispatch(getClass().getResource("/peer-samples/info_get_peers.json"));

        final PeerData peerData = casperServiceMock.getPeerData();

        assertNotNull(peerData);
        assertFalse(peerData.getPeers().isEmpty());
    }

    @Test
    void retrieveLastBlock() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block.json"));

        final JsonBlockData blockData = casperServiceMock.getBlock();
        assertNotNull(blockData);
    }

    @Test
    void getBlockByHash() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block")
                .withBody("$.params.block_identifier.Hash", "709a31cbaff23da43995e78d2209e7f5980905cf70ef850f6744b8d3cec9af13")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block.json"));

        final JsonBlockData blockData = casperServiceMock.getBlock(new HashBlockIdentifier("709a31cbaff23da43995e78d2209e7f5980905cf70ef850f6744b8d3cec9af13"));
        assertNotNull(blockData);

        final JsonBlock block = blockData.getBlock();
        assertEquals("ee3da162c775f921e836ec6d41dedcb006bb972224d1058738e9413dea61fd5e", block.getHeader().getParentHash().toString());
        assertEquals(2346915, block.getHeader().getHeight());
    }

    @Test
    void getBlockByHeight() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block")
                .withBody("$.params.block_identifier.Height", "2346915")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block.json"));

        JsonBlockData blockData = casperServiceMock.getBlock(new HeightBlockIdentifier(2346915));
        assertNotNull(blockData);
        JsonBlock block = blockData.getBlock();
        assertEquals("ee3da162c775f921e836ec6d41dedcb006bb972224d1058738e9413dea61fd5e", block.getHeader().getParentHash().toString());
        assertEquals("709a31cbaff23da43995e78d2209e7f5980905cf70ef850f6744b8d3cec9af13", block.getHash().toString());
    }

    @Test
    void retrieveLastBlockTransfers() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block_transfers")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block_transfers.json"));

        final TransferData transferData = casperServiceMock.getBlockTransfers();

        assertNotNull(transferData);
        assertEquals(1, transferData.getTransfers().size());
        assertEquals(BigInteger.valueOf(445989400000L), transferData.getTransfers().get(0).getAmount());
    }

    @Test
    void getTransferByHeight() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block_transfers")
                .withBody("$.params.block_identifier.Height", "2346915")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block_transfers.json"));

        final TransferData transferData = casperServiceMock.getBlockTransfers(new HeightBlockIdentifier(2346915));
        assertNotNull(transferData);
        assertEquals(1, transferData.getTransfers().size());

        final Transfer transaction = transferData.getTransfers().get(0);
        assertEquals("c709e727b7eaadb3b7f76450aa5d3ac3dd28b0271b7471a6dcc828cfd29f745a", transaction.getDeployHash());
        assertEquals("account-hash-496d542527e1a29f576ab7c3f4c947bfcdc9b4145f75f6ec40e36089432d7351", transaction.getFrom());
        assertEquals("account-hash-8a35e688eac33089b13f91a78c94221b669a0b13a6ed199228b1da018ecfa9df", transaction.getTo());
        assertEquals(BigInteger.valueOf(445989400000L), transaction.getAmount());
    }

    @Test
    void getTransferByHash() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block_transfers")
                .withBody("$.params.block_identifier.Hash", "cdbbe893b5033093b64e73b18555dae1e90bd046a68c79a74e8239b4aed53535")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block_transfers.json"));

        final TransferData transferData = casperServiceMock.getBlockTransfers(new HashBlockIdentifier("cdbbe893b5033093b64e73b18555dae1e90bd046a68c79a74e8239b4aed53535"));
        assertNotNull(transferData);
        assertEquals(1, transferData.getTransfers().size());

        final Transfer transaction = transferData.getTransfers().get(0);
        assertEquals("c709e727b7eaadb3b7f76450aa5d3ac3dd28b0271b7471a6dcc828cfd29f745a", transaction.getDeployHash());
        assertEquals("account-hash-496d542527e1a29f576ab7c3f4c947bfcdc9b4145f75f6ec40e36089432d7351", transaction.getFrom());
        assertEquals("account-hash-8a35e688eac33089b13f91a78c94221b669a0b13a6ed199228b1da018ecfa9df", transaction.getTo());
        assertEquals(BigInteger.valueOf(445989400000L), transaction.getAmount());
    }

    @Test
    void retrieveLastBlockStateRootHash() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_state_root_hash")
                .thenDispatch(getClass().getResource("/status-samples/chain_get_state_root_hash.json"));

        StateRootHashData stateRootData = casperServiceMock.getStateRootHash();

        assertNotNull(stateRootData);
    }

    @Test
    void getStateRootHashByHeight() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_state_root_hash")
                .withBody("$.params.block_identifier.Height", "2346915")
                .thenDispatch(getClass().getResource("/status-samples/chain_get_state_root_hash.json"));

        final StateRootHashData stateRootHashData = casperServiceMock.getStateRootHash(new HeightBlockIdentifier(2346915));
        assertNotNull(stateRootHashData);
        assertEquals("1f32df883da12781ed1e8e699f0faf7350279b9e88b63274e619387ce28003f9", stateRootHashData.getStateRootHash());
    }

    @Test
    void getStateRootHashByHash() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_state_root_hash")
                .withBody("$.params.block_identifier.Hash", "2fe9630b7790852e4409d815b04ca98f37effcdf9097d317b9b9b8ad658f47c8")
                .thenDispatch(getClass().getResource("/status-samples/chain_get_state_root_hash.json"));

        StateRootHashData stateRootHashData = casperServiceMock.getStateRootHash(new HashBlockIdentifier("2fe9630b7790852e4409d815b04ca98f37effcdf9097d317b9b9b8ad658f47c8"));

        assertNotNull(stateRootHashData);
        assertEquals("1f32df883da12781ed1e8e699f0faf7350279b9e88b63274e619387ce28003f9", stateRootHashData.getStateRootHash());
    }

    @Test
    void getBlockState() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_item")
                .withBody("$.params.state_root_hash", "c0eb76e0c3c7a928a0cb43e82eb4fad683d9ad626bcd3b7835a466c0587b0fff")
                .withBody("$.params.key", "account-hash-a9efd010c7cee2245b5bad77e70d9beb73c8776cbe4698b2d8fdf6c8433d5ba0")
                .withBody("$.params.path[0]", "special_value")
                .thenDispatch(getClass().getResource("/status-samples/state_get_item.json"));

        final String stateRootHash = "c0eb76e0c3c7a928a0cb43e82eb4fad683d9ad626bcd3b7835a466c0587b0fff";
        final String key = "account-hash-a9efd010c7cee2245b5bad77e70d9beb73c8776cbe4698b2d8fdf6c8433d5ba0";
        final List<String> path = Collections.singletonList("special_value");
        //noinspection deprecation
        final StoredValueData result = casperServiceMock.getStateItem(stateRootHash, key, path);

        assertInstanceOf(CLValueString.class, result.getStoredValue().getValue());
        // Should be equal incoming parsed
        assertEquals(((CLValueString) result.getStoredValue().getValue()).getValue(), ((CLValueString) result.getStoredValue().getValue()).getParsed());
    }

    @Test
    void getDeploy() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("info_get_deploy")
                .withBody("$.params.deploy_hash", "4f3e856179e7868787fb696c22b24e08d54a226b24693d807177142c1a84d672")
                .thenDispatch(getClass().getResource("/deploy-samples/info_get_deploy.json"));

        final DeployData deployData = casperServiceMock.getDeploy("4f3e856179e7868787fb696c22b24e08d54a226b24693d807177142c1a84d672");
        assertNotNull(deployData);
        assertNotNull(deployData.getDeploy());
        assertInstanceOf(StoredContractByHash.class, deployData.getDeploy().getSession());
        assertInstanceOf(Success.class, deployData.getExecutionResults().get(0).getResult());
        assertInstanceOf(ModuleBytes.class, deployData.getDeploy().getPayment());

        final String sessionHash = ((StoredContractByHash) deployData.getDeploy().getSession()).getHash();
        assertEquals("ccb576d6ce6dec84a551e48f0d0b7af89ddba44c7390b690036257a04a3ae9ea", sessionHash);
    }


    @Test
    void getStateItem_account() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_item")
                .withBody("$.params.state_root_hash", "c0eb76e0c3c7a928a0cb43e82eb4fad683d9ad626bcd3b7835a466c0587b0fff")
                .withBody("$.params.key", "account-hash-a9efd010c7cee2245b5bad77e70d9beb73c8776cbe4698b2d8fdf6c8433d5ba0")
                .thenDispatch(getClass().getResource("/status-samples/state_get_item_account.json"));

        //noinspection deprecation
        StoredValueData storedValueData = casperServiceMock.getStateItem("c0eb76e0c3c7a928a0cb43e82eb4fad683d9ad626bcd3b7835a466c0587b0fff", "account-hash-a9efd010c7cee2245b5bad77e70d9beb73c8776cbe4698b2d8fdf6c8433d5ba0", new ArrayList<>());

        assertInstanceOf(StoredValueAccount.class, storedValueData.getStoredValue());
        assertEquals("uref-e9e2dbc67809be522dcc953b601c29807d8e831751533509be373f0c72ed8bd5-007", ((StoredValueAccount) storedValueData.getStoredValue()).getValue().getMainPurse());
    }

    @Test
    void getStateItem_contract() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_item")
                .withBody("$.params.state_root_hash", "0aba07d7dd032f6b9a2940b3f9e8ef6da39f941b89bb868ce7a769fb5a775f6f")
                .withBody("$.params.key", "hash-d2469afeb99130f0be7c9ce230a84149e6d756e306ef8cf5b8a49d5182e41676")
                .thenDispatch(getClass().getResource("/status-samples/state_get_item_contract.json"));

        //noinspection deprecation
        final StoredValueData storedValueData = casperServiceMock.getStateItem("0aba07d7dd032f6b9a2940b3f9e8ef6da39f941b89bb868ce7a769fb5a775f6f", "hash-d2469afeb99130f0be7c9ce230a84149e6d756e306ef8cf5b8a49d5182e41676", new ArrayList<>());

        assertInstanceOf(StoredValueContract.class, storedValueData.getStoredValue());
        assertEquals("contract-package-d63c44078a1931b5dc4b80a7a0ec586164fd0470ce9f8b23f6d93b9e86c5944d", ((StoredValueContract) storedValueData.getStoredValue()).getValue().getPackageHash());
        assertEquals("contract-wasm-08d9634a72c58df5d837740174c6e88e52b0370ce8c74960d600e54966a0a4cf", ((StoredValueContract) storedValueData.getStoredValue()).getValue().getWasmHash());
        assertEquals("uref-fe327f9815a1d016e1143db85e25a86341883949fd75ac1c1e7408a26c5b62ef-007", ((StoredValueContract) storedValueData.getStoredValue()).getValue().getNamedKeys().get(0).getKey());
    }

    @Test
    void getAccountStateInfoByBlockHash() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_account_info")
                .withBody("$.params.public_key", "0202f6a9be4f3a65371bb229f34c56de42667ffcfe43defffdc959db256f0ad99c1a")
                .withBody("$.params.block_identifier.Hash", "8a029e8920e3d1644eba29fa080c92e90ee0033bd1179da04288ad56c372a121")
                .thenDispatch(getClass().getResource("/status-samples/state_get_account_info.json"));

        final AccountData account = casperServiceMock.getStateAccountInfo("0202f6a9be4f3a65371bb229f34c56de42667ffcfe43defffdc959db256f0ad99c1a",
                new HashBlockIdentifier("8a029e8920e3d1644eba29fa080c92e90ee0033bd1179da04288ad56c372a121"));

        assertNotNull(account);
        assertNotNull(account.getAccount());
        assertEquals("account-hash-5079fc7bdd40f9226931d5863cb73f263c7944d6c8a8f3570213e9eb3e9464fe", account.getAccount().getHash());
    }

    @Test
    void getAccountStateInfoByBlockHeight() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_account_info")
                .withBody("$.params.public_key", "0202f6a9be4f3a65371bb229f34c56de42667ffcfe43defffdc959db256f0ad99c1a")
                .withBody("$.params.block_identifier.Height", "2435022")
                .thenDispatch(getClass().getResource("/status-samples/state_get_account_info.json"));

        final AccountData account = casperServiceMock.getStateAccountInfo("0202f6a9be4f3a65371bb229f34c56de42667ffcfe43defffdc959db256f0ad99c1a", new HeightBlockIdentifier(2435022));

        assertNotNull(account);
        assertNotNull(account.getAccount());
        assertEquals("account-hash-5079fc7bdd40f9226931d5863cb73f263c7944d6c8a8f3570213e9eb3e9464fe", account.getAccount().getHash());
    }

    @Test
    void getAuctionInfoByBlockHash() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_auction_info")
                .withBody("$.params.block_identifier.Hash", "8a029e8920e3d1644eba29fa080c92e90ee0033bd1179da04288ad56c372a121")
                .thenDispatch(getClass().getResource("/auction-info-samples/state_get_auction_info.json"));

        final AuctionData auction = casperServiceMock.getStateAuctionInfo(new HashBlockIdentifier("8a029e8920e3d1644eba29fa080c92e90ee0033bd1179da04288ad56c372a121"));

        assertNotNull(auction);
        assertNotNull(auction.getAuctionState());
        assertEquals(2435022, auction.getAuctionState().getHeight());
    }

    @Test
    void getAuctionInfoByBlockHeight() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_auction_info")
                .withBody("$.params.block_identifier.Height", "2435022")
                .thenDispatch(getClass().getResource("/auction-info-samples/state_get_auction_info.json"));

        final AuctionData auction = casperServiceMock.getStateAuctionInfo(new HeightBlockIdentifier(2435022));

        assertNotNull(auction);
        assertNotNull(auction.getAuctionState());
        assertEquals(2435022L, auction.getAuctionState().getHeight());
    }

    @Test
    void getEraInfoBySwitchBlockByHeight() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_era_info_by_switch_block")
                .withBody("$.params.block_identifier.Height", "423571")
                .thenDispatch(getClass().getResource("/era-info-samples/era-info-by-switch-block-shrunk.json"));

        final EraInfoData eraInfoData = casperServiceMock.getEraInfoBySwitchBlock(new HeightBlockIdentifier(423571));

        assertNotNull(eraInfoData);
        assertNotNull(eraInfoData.getEraSummary());
        assertEquals("485a33d5c737030432fba0c3b15c1cc6b372fd286677bf9f29d4ab8b1f0c9223", eraInfoData.getEraSummary().getStateRootHash());
    }


    @Test
    void getEraInfoBySwitchBlockByHash() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_era_info_by_switch_block")
                .withBody("$.params.block_identifier.Hash", "6eee8974bd9df0c2ae5469a239c23ff901c4ca884a1fe8b7b5319b04fac3b484")
                .thenDispatch(getClass().getResource("/era-info-samples/era-info-by-switch-block-shrunk.json"));

        final EraInfoData eraInfoData = casperServiceMock.getEraInfoBySwitchBlock(new HashBlockIdentifier("6eee8974bd9df0c2ae5469a239c23ff901c4ca884a1fe8b7b5319b04fac3b484"));

        assertNotNull(eraInfoData);
        assertNotNull(eraInfoData.getEraSummary());
        assertEquals("485a33d5c737030432fba0c3b15c1cc6b372fd286677bf9f29d4ab8b1f0c9223", eraInfoData.getEraSummary().getStateRootHash());
    }

    @Test
    void getBalance() throws IllegalArgumentException, IOException, DynamicInstanceException {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_balance")
                .withBody("$.params.state_root_hash", "78e5d93246bcf9aa3336c4c80cd9359589cbddceda75e31bb3d65b0c6bda3b59")
                .withBody("$.params.purse_uref", "uref-2838aee2e1b1e8dfcf74aac7940a93c6ea34770b159ec18720872ac02d8abc98-007")
                .thenDispatch(getClass().getResource("/balance/state_get_balance.json"));

        final GetBalanceData balance = casperServiceMock.getBalance("78e5d93246bcf9aa3336c4c80cd9359589cbddceda75e31bb3d65b0c6bda3b59",
                URef.fromString("uref-2838aee2e1b1e8dfcf74aac7940a93c6ea34770b159ec18720872ac02d8abc98-007"));

        assertNotNull(balance);
        assertEquals(balance.getValue(), BigInteger.valueOf(240031118595L));
    }

    @Test
    void queryGlobalState() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("query_global_state")
                .withBody("$.params.state_identifier.BlockHash", "0899a5e98a1b742551fc8f15a2393b9218366a12aaaeccf17a89d581f042dbdd")
                .withBody("$.params.key", "deploy-94905b503c07c49564467a0b0b5c10a68642eaa35e7800b98b5e45e7531695e1")
                .thenDispatch(getClass().getResource("/globalstate-samples/query_global_state.json"));

        final GlobalStateIdentifier blockIdentifier = new BlockHashIdentifier("0899a5e98a1b742551fc8f15a2393b9218366a12aaaeccf17a89d581f042dbdd");
        final String key = "deploy-94905b503c07c49564467a0b0b5c10a68642eaa35e7800b98b5e45e7531695e1";
        final GlobalStateData globalState = casperServiceMock.queryGlobalState(blockIdentifier, key, new String[0]);

        assertNotNull(globalState);
        assertThat(((StoredValueDeployInfo) globalState.getStoredValue()).getValue().getFrom(), is("account-hash-229695fc6ec91403b671f53ec65a27bb063f419442a5c61f7460da54ae610537"));
        assertThat(((StoredValueDeployInfo) globalState.getStoredValue()).getValue().getSource().getJsonURef(), is("uref-b1ef3c104836aea006cca6d0c9c9b0953653fda904a1363995a154b2d2d1e889-007"));
        assertThat(((StoredValueDeployInfo) globalState.getStoredValue()).getValue().getGas(), is(BigInteger.valueOf(1631303630L)));
    }

    @Test
    void queryValidatorChanges() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("info_get_validator_changes")
                .thenDispatch(getClass().getResource("/globalstate-samples/info_get_validator_changes.json"));

        final ValidatorChangeData changes = casperServiceMock.getValidatorsChanges();
        assertNotNull(changes);
        assertThat(changes.getChanges(), hasSize(2));
        assertThat(changes.getChanges().get(0).getPublicKey().getAlgoTaggedHex(), is("01098d1758f1ca75350dfec8a1c4c1984a88d1ea5eab5590fbc9e856d67cde31eb"));
        assertThat(changes.getChanges().get(0).getStatusChanges().get(0).getEraId(), is(BigInteger.valueOf(12519L)));
        assertThat(changes.getChanges().get(0).getStatusChanges().get(0).getValidatorChange().name(), is("Added"));
    }

    @Test
    void getCasperClientExceptionExceptionBlockNotKnown() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("info_get_deploy")
                .withBody("$.params.deploy_hash", "abc")
                .thenDispatch(getClass().getResource("/deploy-samples/info_get_deploy_error.json"));

        final String expectedMessage = "Invalid params (code: -32602) Failed to parse 'params' field: could not convert slice to array";

        final CasperClientException casperClientException = assertThrows(CasperClientException.class,
                () -> casperServiceMock.getDeploy("abc"));

        assertEquals(expectedMessage, casperClientException.getMessage());
    }
}
