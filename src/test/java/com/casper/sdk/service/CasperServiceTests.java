package com.casper.sdk.service;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchKeyTagException;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.block.HeightBlockIdentifier;
import com.casper.sdk.identifier.entity.EntityAddrIdentifier;
import com.casper.sdk.identifier.era.BlockEraIdentifier;
import com.casper.sdk.identifier.global.BlockHashIdentifier;
import com.casper.sdk.identifier.global.GlobalStateIdentifier;
import com.casper.sdk.identifier.global.StateRootHashIdentifier;
import com.casper.sdk.identifier.purse.PurseUref;
import com.casper.sdk.model.account.AccountData;
import com.casper.sdk.model.account.AccountHashIdentifier;
import com.casper.sdk.model.account.PublicKeyIdentifier;
import com.casper.sdk.model.auction.AuctionData;
import com.casper.sdk.model.balance.GetBalanceData;
import com.casper.sdk.model.balance.QueryBalanceDetailsResult;
import com.casper.sdk.model.bid.BidKind;
import com.casper.sdk.model.bid.ValidatorCredit;
import com.casper.sdk.model.block.*;
import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.cltype.CLTypePublicKey;
import com.casper.sdk.model.clvalue.cltype.CLTypeUnit;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.contract.Contract;
import com.casper.sdk.model.contract.ContractPackage;
import com.casper.sdk.model.contract.NamedKey;
import com.casper.sdk.model.contract.entrypoint.*;
import com.casper.sdk.model.deploy.*;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.casper.sdk.model.deploy.executabledeploy.Transfer;
import com.casper.sdk.model.entity.System;
import com.casper.sdk.model.entity.*;
import com.casper.sdk.model.era.EraEndV2;
import com.casper.sdk.model.era.EraInfoData;
import com.casper.sdk.model.globalstate.GlobalStateData;
import com.casper.sdk.model.key.*;
import com.casper.sdk.model.peer.PeerData;
import com.casper.sdk.model.reward.GetRewardResult;
import com.casper.sdk.model.stateroothash.StateRootHashData;
import com.casper.sdk.model.storedvalue.*;
import com.casper.sdk.model.transaction.*;
import com.casper.sdk.model.transaction.entrypoint.TransferEntryPoint;
import com.casper.sdk.model.transaction.execution.Effect;
import com.casper.sdk.model.transaction.execution.ExecutionInfo;
import com.casper.sdk.model.transaction.execution.ExecutionResult;
import com.casper.sdk.model.transaction.execution.ExecutionResultV2;
import com.casper.sdk.model.transaction.field.Fields;
import com.casper.sdk.model.transaction.kind.PruneKind;
import com.casper.sdk.model.transaction.kind.WriteKind;
import com.casper.sdk.model.transaction.pricing.FixedPricingMode;
import com.casper.sdk.model.transaction.pricing.PaymentLimited;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.model.transfer.TransferData;
import com.casper.sdk.model.transfer.TransferV1;
import com.casper.sdk.model.transfer.TransferV2;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.model.validator.ValidatorChangeData;
import com.casper.sdk.test.MockNode;
import com.casper.sdk.test.RcpResponseDispatcher;
import com.syntifi.crypto.key.Secp256k1PrivateKey;
import com.syntifi.crypto.key.Secp256k1PublicKey;
import org.joda.time.DateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.jayway.jsonassert.impl.matcher.IsCollectionWithSize.hasSize;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsIterableContaining.hasItems;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
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
        LOGGER.debug("Testing with block height {}", 2346915);

        final RcpResponseDispatcher when = mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block")
                .withBody("$.params.block_identifier.Height", "2346915")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block.json"));

        final ChainGetBlockResult resultByHeight = casperServiceMock.getBlock(new HeightBlockIdentifier(2346915));
        final String hash = resultByHeight.getBlockWithSignatures().getBlock().getHash().toString();

        when.clear()
                .withMethod("chain_get_block")
                .withBody("$.params.block_identifier.Hash", hash)
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block.json"));

        final ChainGetBlockResult resultByHash = casperServiceMock.getBlock(new HashBlockIdentifier(hash));
        assertEquals(resultByHash.getBlockWithSignatures().getBlock().getHash().toString(), hash);
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

        final ChainGetBlockResult result = casperServiceMock.getBlock(new HeightBlockIdentifier(23469150));
        final PublicKey key = result.getBlockWithSignatures().getBlock().getBody().getProposer();

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

        final ChainGetBlockResult blockData = casperServiceMock.getBlock();
        assertNotNull(blockData);
        assertThat(blockData.getBlockWithSignatures().getBlock().getHash(), is(new Digest("709a31cbaff23da43995e78d2209e7f5980905cf70ef850f6744b8d3cec9af13")));
    }

    @Test
    void getBlockByHash() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block")
                .withBody("$.params.block_identifier.Hash", "709a31cbaff23da43995e78d2209e7f5980905cf70ef850f6744b8d3cec9af13")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block.json"));

        final ChainGetBlockResult blockData = casperServiceMock.getBlock(
                new HashBlockIdentifier("709a31cbaff23da43995e78d2209e7f5980905cf70ef850f6744b8d3cec9af13")
        );
        assertNotNull(blockData);

        final BlockV1 block = blockData.getBlockWithSignatures().getBlock();

        assertEquals("ee3da162c775f921e836ec6d41dedcb006bb972224d1058738e9413dea61fd5e", block.getHeader().getParentHash().toString());
        assertEquals(2346915, block.getHeader().getHeight());
    }

    @Test
    void getBlockByHeight() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block")
                .withBody("$.params.block_identifier.Height", "2346915")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block.json"));

        ChainGetBlockResult blockData = casperServiceMock.getBlock(new HeightBlockIdentifier(2346915));
        assertNotNull(blockData);
        BlockV1 block = blockData.getBlockWithSignatures().getBlock();
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
        assertEquals(BigInteger.valueOf(445989400000L), transferData.getTransfers().get(0).getTransferV1().getAmount());
    }

    @Test
    void getTransferByHeight() throws NoSuchKeyTagException {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block_transfers")
                .withBody("$.params.block_identifier.Height", "2346915")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block_transfers.json"));

        final TransferData transferData = casperServiceMock.getBlockTransfers(new HeightBlockIdentifier(2346915));
        assertNotNull(transferData);
        assertEquals(1, transferData.getTransfers().size());

        final TransferV1 transaction = transferData.getTransfers().get(0).getTransferV1();
        assertEquals("c709e727b7eaadb3b7f76450aa5d3ac3dd28b0271b7471a6dcc828cfd29f745a", transaction.getDeployHash());
        assertEquals(Key.create("account-hash-496d542527e1a29f576ab7c3f4c947bfcdc9b4145f75f6ec40e36089432d7351"), transaction.getFrom());
        assertEquals(Key.create("account-hash-8a35e688eac33089b13f91a78c94221b669a0b13a6ed199228b1da018ecfa9df"), transaction.getTo());
        assertEquals(BigInteger.valueOf(445989400000L), transaction.getAmount());
    }

    @Test
    void getTransferByHash() throws NoSuchKeyTagException {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block_transfers")
                .withBody("$.params.block_identifier.Hash", "cdbbe893b5033093b64e73b18555dae1e90bd046a68c79a74e8239b4aed53535")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block_transfers.json"));

        final TransferData transferData = casperServiceMock.getBlockTransfers(new HashBlockIdentifier("cdbbe893b5033093b64e73b18555dae1e90bd046a68c79a74e8239b4aed53535"));
        assertNotNull(transferData);
        assertEquals(1, transferData.getTransfers().size());

        final TransferV1 transaction = transferData.getTransfers().get(0).getTransferV1();
        assertEquals("c709e727b7eaadb3b7f76450aa5d3ac3dd28b0271b7471a6dcc828cfd29f745a", transaction.getDeployHash());
        assertEquals(Key.create("account-hash-496d542527e1a29f576ab7c3f4c947bfcdc9b4145f75f6ec40e36089432d7351"), transaction.getFrom());
        assertEquals(Key.create("account-hash-8a35e688eac33089b13f91a78c94221b669a0b13a6ed199228b1da018ecfa9df"), transaction.getTo());
        assertEquals(BigInteger.valueOf(445989400000L), transaction.getAmount());
    }

    @Test
    void retrieveLastBlockStateRootHash() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_state_root_hash")
                .thenDispatch(getClass().getResource("/status-samples/chain_get_state_root_hash.json"));

        final StateRootHashData stateRootData = casperServiceMock.getStateRootHash();
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

        StateRootHashData stateRootHashData = casperServiceMock.getStateRootHash(
                new HashBlockIdentifier("2fe9630b7790852e4409d815b04ca98f37effcdf9097d317b9b9b8ad658f47c8")
        );

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
    void getDeploy() throws NoSuchKeyTagException {

        mockNode.withRcpResponseDispatcher()
                .withMethod("info_get_deploy")
                .withBody("$.params.deploy_hash", "cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139")
                .thenDispatch(getClass().getResource("/deploy-samples/info_get_deployV2.json"));

        final DeployData deployData = casperServiceMock.getDeploy("cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139");
        assertNotNull(deployData);
        assertNotNull(deployData.getDeploy());
        assertInstanceOf(Transfer.class, deployData.getDeploy().getSession());
        assertInstanceOf(ExecutionResultV2.class, deployData.getExecutionInfo().getExecutionResult());
        assertThat(((ExecutionResultV2) deployData.getExecutionInfo().getExecutionResult()).getErrorMessage(), is("unsupported mode for deploy-hash(cb04..c139) attempting transfer"));
        assertThat(((ExecutionResultV2) deployData.getExecutionInfo().getExecutionResult()).getEffects().get(0).getKey(), is(Key.create("balance-hold-01fe139a5aa36aa69c04a6b630c9993bc03d868ffde46d3f60c3fbe6e6e762016f78bec10c90010000")));
        assertThat(((ExecutionResultV2) deployData.getExecutionInfo().getExecutionResult()).getEffects().get(0).getKind(), is(instanceOf(WriteKind.class)));
        assertThat(((WriteKind<?>) ((ExecutionResultV2) deployData.getExecutionInfo().getExecutionResult()).getEffects().get(0).getKind()).getWrite(), is(instanceOf(StoredValueCLValue.class)));
        assertInstanceOf(ModuleBytes.class, deployData.getDeploy().getPayment());
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
    void getStateItemContract() {

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
        assertEquals("uref-fe327f9815a1d016e1143db85e25a86341883949fd75ac1c1e7408a26c5b62ef-007", ((StoredValueContract) storedValueData.getStoredValue()).getValue().getNamedKeys().get(0).getKey().toString());
    }

    @Test
    void getAccountStateInfoByBlockHash() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_account_info")
                .withBody("$.params.account_identifier", "0202f6a9be4f3a65371bb229f34c56de42667ffcfe43defffdc959db256f0ad99c1a")
                .withBody("$.params.block_identifier.Hash", "8a029e8920e3d1644eba29fa080c92e90ee0033bd1179da04288ad56c372a121")
                .thenDispatch(getClass().getResource("/status-samples/state_get_account_info.json"));

        final AccountData account = casperServiceMock.getStateAccountInfo("0202f6a9be4f3a65371bb229f34c56de42667ffcfe43defffdc959db256f0ad99c1a", new HashBlockIdentifier("8a029e8920e3d1644eba29fa080c92e90ee0033bd1179da04288ad56c372a121"));

        assertNotNull(account);
        assertNotNull(account.getAccount());
        assertEquals("account-hash-5079fc7bdd40f9226931d5863cb73f263c7944d6c8a8f3570213e9eb3e9464fe", account.getAccount().getHash().toString());
    }

    @Test
    void getAccountStateInfoByBlockHeight() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_account_info")
                .withBody("$.params.account_identifier", "0202f6a9be4f3a65371bb229f34c56de42667ffcfe43defffdc959db256f0ad99c1a")
                .withBody("$.params.block_identifier.Height", "2435022")
                .thenDispatch(getClass().getResource("/status-samples/state_get_account_info.json"));

        final AccountData account = casperServiceMock.getStateAccountInfo("0202f6a9be4f3a65371bb229f34c56de42667ffcfe43defffdc959db256f0ad99c1a", new HeightBlockIdentifier(2435022));

        assertNotNull(account);
        assertNotNull(account.getAccount());
        assertThat(account.getAccount().getHash(), is(instanceOf(AccountHashKey.class)));
        assertEquals("account-hash-5079fc7bdd40f9226931d5863cb73f263c7944d6c8a8f3570213e9eb3e9464fe", account.getAccount().getHash().toString());
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
        assertEquals(10, auction.getAuctionState().getHeight());
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
        assertEquals(10L, auction.getAuctionState().getHeight());
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
    void getEraInfoBySwitchBlockByHeightCondor() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_era_info_by_switch_block")
                .withBody("$.params.block_identifier.Height", "423571")
                .thenDispatch(getClass().getResource("/era-info-samples/era-info-by-switch-block-condor.json"));

        final EraInfoData eraInfoData = casperServiceMock.getEraInfoBySwitchBlock(new HeightBlockIdentifier(423571));

        assertNotNull(eraInfoData);

        assertThat(((Delegator) eraInfoData.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorKind(), is(instanceOf(DelegatorKindPublicKey.class)));
        assertThat(((Delegator) eraInfoData.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(2)).getDelegatorKind(), is(instanceOf(DelegatorKindPurse.class)));

        assertThat(((DelegatorKindPublicKey) ((Delegator) eraInfoData.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorKind()).getPublicKey().toString(),
                is("01af79f28bde4522b27edb8dc9df146c9d3a65f944bbdcf153ea107b291bae232d"));

        assertThat(((DelegatorKindPurse) ((Delegator) eraInfoData.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(2)).getDelegatorKind()).getPurse().getJsonURef(),
                is("uref-3c4bf80070d2667eaacc51506ba49ba0f26452649622d7cb9815c9f81ea92bd7-007"));

        assertNotNull(eraInfoData.getEraSummary());
        assertEquals("ddd2c62a342e0cbc8f979d0fb783f9764af937ac514cd78bfba08d886bfcd758", eraInfoData.getEraSummary().getStateRootHash());
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

        final GetBalanceData balance = casperServiceMock.getBalance(
                "78e5d93246bcf9aa3336c4c80cd9359589cbddceda75e31bb3d65b0c6bda3b59",
                URef.fromString("uref-2838aee2e1b1e8dfcf74aac7940a93c6ea34770b159ec18720872ac02d8abc98-007")
        );

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

        final CasperClientException casperClientException = assertThrows(CasperClientException.class, () -> casperServiceMock.getDeploy("abc"));

        assertEquals(expectedMessage, casperClientException.getMessage());
    }

    @Test
    void chainGetBlockV2() {
        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block")
                //.withBody("$.params.deploy_hash", "abc")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block_v2.json"));

        final ChainGetBlockResult blockWithSignatures = casperServiceMock.getBlock();
        assertThat(blockWithSignatures, is(notNullValue()));
        assertThat(blockWithSignatures.getApiVersion(), is("2.0.0"));
        assertThat(blockWithSignatures.getBlockWithSignatures(), is(notNullValue()));
        assertThat(blockWithSignatures.getBlockWithSignatures().getBlock(), is(instanceOf(BlockV2.class)));

        BlockV2 block = blockWithSignatures.getBlockWithSignatures().getBlock();

        assertThat(block.getHeader(), is(instanceOf(BlockHeaderV2.class)));
        BlockHeaderV2 header = block.getHeader();
        assertThat(header.getParentHash(), is(new Digest("714787cd96712bdfe0815e85f12b44b6551fdeb1021b55c9efe94639ad744a97")));
        assertThat(header.getStateRootHash(), is(new Digest("9cf8fd13904d7143ad2f9e2dc716ef3bd53a8cde7cbad707b8f44041a711a747")));
        assertThat(header.getBodyHash(), is(new Digest("18937e8cf4338b5f5fdc2581f8d7d6a47de736d2799e3f3bc9b0ff9f1e7cf106")));
        assertThat(header.isRandomBit(), is(true));
        assertThat(header.getAccumulatedSeed(), is(new Digest("9aec85a8a66302e9679ea269bf344958848d4a00a565c8f5c9f576a8a2ac68a0")));
        assertThat(header.getEraEnd(), is(nullValue()));
        assertThat(header.getTimeStamp(), is(new DateTime("2024-06-05T10:55:38.908Z").toDate()));
        assertThat(header.getEraId(), is(18L));
        assertThat(header.getEraId(), is(18L));
        assertThat(header.getProtocolVersion(), is("2.0.0"));
        assertThat(header.getProposer(), is(new Digest("010b277da84a12c8814d5723eeb57123ff287f22466fd13faca1bb1fae57d2679b")));
        assertThat(header.getCurrentGasPrice(), is(1L));
        assertThat(header.getLastSwitchBlockHash(), is(new Digest("56ae7d9b11d0bc21134e10fd86918c8e66a7bc69066fafa7070e77eca25f7d3e")));

        assertThat(blockWithSignatures.getBlockWithSignatures().getBlock().getBody(), is(instanceOf(BlockBodyV2.class)));
        BlockBodyV2 blockBody = block.getBody();
        assertThat(blockBody.getRewardedSignatures(), hasSize(3));
        assertThat(blockBody.getRewardedSignatures().get(0), hasSize(1));
        assertThat(blockBody.getRewardedSignatures().get(0).get(0), is(248L));
        assertThat(blockBody.getRewardedSignatures().get(1).get(0), is(0L));
        assertThat(blockBody.getRewardedSignatures().get(2).get(0), is(0L));
    }

    @Test
    void chainGetBlockEraEndV2() throws NoSuchAlgorithmException {

        mockNode.withRcpResponseDispatcher()
                .withMethod("chain_get_block")
                //.withBody("$.params.deploy_hash", "abc")
                .thenDispatch(getClass().getResource("/block-samples/chain_get_block_era_end_v2.json"));

        final ChainGetBlockResult blockWithSignatures = casperServiceMock.getBlock();
        assertThat(blockWithSignatures, is(notNullValue()));
        assertThat(blockWithSignatures.getApiVersion(), is("2.0.0"));
        assertThat(blockWithSignatures.getBlockWithSignatures(), is(notNullValue()));
        assertThat(blockWithSignatures.getBlockWithSignatures().getBlock(), is(instanceOf(BlockV2.class)));

        BlockV2 block = blockWithSignatures.getBlockWithSignatures().getBlock();
        EraEndV2 eraEnd = block.getHeader().getEraEnd();
        assertThat(eraEnd, is(notNullValue()));
        assertThat(eraEnd.getNextEraGasPrice(), is(1));
        assertThat(eraEnd.getInactiveValidators(), hasSize(0));

        assertThat(eraEnd.getNextEraValidatorWeights(), hasSize(5));
        assertThat(eraEnd.getNextEraValidatorWeights().get(0).getValidator(), is(PublicKey.fromTaggedHexString("010b277da84a12c8814d5723eeb57123ff287f22466fd13faca1bb1fae57d2679b")));
        assertThat(eraEnd.getNextEraValidatorWeights().get(0).getWeight(), is(new BigInteger("11823890605832274469")));

        assertThat(eraEnd.getRewards().size(), is(5));
        assertThat(eraEnd.getRewards().get(PublicKey.fromTaggedHexString("010b277da84a12c8814d5723eeb57123ff287f22466fd13faca1bb1fae57d2679b")), hasItems(new BigInteger("4026058477024681"), new BigInteger("402627925137076")));
    }

    @Test
    @Disabled
        // TODO Reinstate with new JSON file
    void infoGetTransactionByDeployHash() throws NoSuchAlgorithmException, IOException, DynamicInstanceException {

        mockNode.withRcpResponseDispatcher()
                .withMethod("info_get_transaction")
                .withBody("$.params.transaction_hash.Deploy", "cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139")
                .thenDispatch(getClass().getResource("/transaction-samples/info_get_transaction.json"));

        final GetTransactionResult result = casperServiceMock.getTransaction(new TransactionHashDeploy("cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139"));
        assertNotNull(result);
        assertThat(result.getTransaction().get(), is(instanceOf(Deploy.class)));
        assertThat(result.getTransaction().get().getHash(), is(new Digest("cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139")));
        assertThat(((Deploy) result.getTransaction().get()).getHeader().getBodyHash(), is(new Digest("9fae773a27a4aafb161321c0779031a077b63f608f0743a2abbc4359c080a965")));
        assertThat(result.getExecutionInfo().getBlockHash(), is(new Digest("3ad051b1df071e4d539eab28986bb55ec4fb5fd31f63942ed788738186709303")));
        assertThat(result.getExecutionInfo().getBlockHeight(), is(new BigInteger("4139")));
        assertThat(result.getExecutionInfo().getExecutionResult(), is(instanceOf(ExecutionResultV2.class)));

        final ExecutionResultV2 executionResult = result.getExecutionInfo().getExecutionResult();
        assertThat(executionResult.getErrorMessage(), is("unsupported mode for deploy-hash(cb04..c139) attempting transfer"));
        assertThat(executionResult.getInitiator().getAddress(), is(instanceOf(PublicKey.class)));
        assertThat(executionResult.getInitiator().getAddress(), is(PublicKey.fromTaggedHexString("010b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd")));
        assertThat(executionResult.getLimit(), is(new BigInteger("10000")));
        assertThat(executionResult.getCost(), is(new BigInteger("10000")));
        assertThat(executionResult.getConsumed(), is(new BigInteger("0")));
        assertThat(executionResult.getSizeEstimate(), is(366L));

        assertThat(executionResult.getPayment(), hasSize(1));
        assertThat(executionResult.getPayment().get(0).getSource(), is(URef.fromString("uref-575de810398cc4f39e88c56f3684965b82a5c79e14df8d5fbe74a62962de0bb5-007")));

        assertThat(executionResult.getTransfers(), hasSize(1));
        assertThat(executionResult.getTransfers().get(0).getTransferV2(), is(instanceOf(TransferV2.class)));
        final TransferV2 transfer = executionResult.getTransfers().get(0).getTransferV2();
        assertThat(transfer.getTransactionHash(), is(new Digest("cb04018ad3a09fc15fda0e5c18def392a135652c73c864c25968be9b2376c139")));
        assertThat(transfer.getSource(), is(URef.fromString("uref-b22bc80d357df47447074e243b4d888de67c1cc7565fa82d0bb2b9b023146748-007")));
        assertThat(transfer.getTarget(), is(URef.fromString("uref-575de810398cc4f39e88c56f3684965b82a5c79e14df8d5fbe74a62962de0bb5-007")));
        assertThat(transfer.getFrom().getAddress(), is(PublicKey.fromTaggedHexString("010b42b381f087e65a1b6d4c9538027502307ebeff02044c9507f30ddffdb02ebd")));
        assertThat(transfer.getAmount(), is(new BigInteger("2500000000")));
        assertThat(transfer.getGas(), is(1));
        assertThat(transfer.getId(), is(BigInteger.valueOf(12345L)));
    }

    @Test
    void infoGetInstalledContractTransactionByHash() throws Exception {

        mockNode.withRcpResponseDispatcher()
                .withMethod("info_get_transaction")
                .withBody("$.params.transaction_hash.Deploy", "9a35bd593202c587ec21fabb1caa44e5e40284a5228c50d35f78585b3e90279b")
                .thenDispatch(getClass().getResource("/transaction-samples/get_transaction_install_contract.json"));

        final GetTransactionResult result = casperServiceMock.getTransaction(new TransactionHashDeploy("9a35bd593202c587ec21fabb1caa44e5e40284a5228c50d35f78585b3e90279b"));
        assertNotNull(result);
        assertThat(result.getTransaction().get(), is(instanceOf(TransactionV1.class)));
        assertThat(result.getTransaction().get().getHash(), is(new Digest("37eea39f298565a1a7fb136927c2956d896f3e9c41e0c87b06ec03ce33482584")));

        final TransactionV1 transaction = result.getTransaction().getVersion1();

        assertThat(transaction.getPayload().getPricingMode(), is(instanceOf(PaymentLimited.class)));
        assertThat(((PaymentLimited) transaction.getPayload().getPricingMode()).getGasPriceTolerance(), is(1));
        assertThat(transaction.getPayload().getInitiatorAddr(), is(instanceOf(InitiatorPublicKey.class)));
        assertThat(transaction.getPayload().getInitiatorAddr().getAddress(), is(PublicKey.fromTaggedHexString("0184f6d260f4ee6869ddb36affe15456de6ae045278fa2f467bb677561ce0dad55")));

        final ExecutionInfo executionInfo = result.getExecutionInfo();

        assertThat(executionInfo.getBlockHash(), is(new Digest("b60c3d043d75678ac2422bbbef9f6e32eee023016fea78830414d9bd6e0cf0ef")));
        assertThat(executionInfo.getBlockHeight(), is(new BigInteger("6")));
        assertThat(result.getExecutionInfo().getExecutionResult(), is(instanceOf(ExecutionResultV2.class)));

        final ExecutionResultV2 executionResult = executionInfo.getExecutionResult();

        assertThat(executionResult.getInitiator().getAddress(), is(instanceOf(PublicKey.class)));
        assertThat(executionResult.getInitiator().getAddress(), is(PublicKey.fromTaggedHexString("0184f6d260f4ee6869ddb36affe15456de6ae045278fa2f467bb677561ce0dad55")));
        assertThat(executionResult.getLimit(), is(new BigInteger("500000000000")));
        assertThat(executionResult.getCost(), is(new BigInteger("500000000000")));
        assertThat(executionResult.getConsumed(), is(new BigInteger("311158169340")));
        assertThat(executionResult.getSizeEstimate(), is(241198L));

        assertThat(executionResult.getEffects().size(), is(61));

        final Optional<Effect> maybeEffect = executionResult.getEffectByKey("hash-94b21891ae273b17eeb6a1899a52ab952bcc4da2e19626563f88d6cf7ab6a2bd");
        assertThat(maybeEffect.isPresent(), is(true));
        assertThat(maybeEffect.get().getKind(), is(instanceOf(WriteKind.class)));
        assertThat(((WriteKind<?>) maybeEffect.get().getKind()).getWrite().getValue(), is(instanceOf(Contract.class)));

        //noinspection unchecked
        final Contract value = ((WriteKind<Contract>) maybeEffect.get().getKind()).getWrite().getValue();
        assertThat(value.getPackageHash(), is("contract-package-b71675d8cf701d9bc584cb5152706873110e5158b004fb966ed28be49c66b39a"));
        assertThat(value.getWasmHash(), is("contract-wasm-a9fb7ec293465829432a8e543a2c2d5bba6d622c512af7e0cf204f6f536a1cbf"));
        assertThat(value.getProtocolVersion(), is("2.0.0"));
        assertThat(value.getEntryPoints(), hasSize(25));

        EntryPointV1 entryPoint = value.getEntryPoints().get(2);
        assertThat(entryPoint.getAccess(), is(instanceOf(PublicAccess.class)));
        assertThat(entryPoint.getEntryPointType(), is(EntryPointType.CALLED));
        assertThat(entryPoint.getRet().getTypeName(), is("Unit"));
        assertThat(entryPoint.getName(), is("approve"));
        assertThat(entryPoint.getArgs(), hasSize(2));
        assertThat(entryPoint.getArgs().get(0).getName(), is("spender"));
        assertThat(entryPoint.getArgs().get(0).getClType().getTypeName(), is("Key"));
        assertThat(entryPoint.getArgs().get(1).getName(), is("amount"));
        assertThat(entryPoint.getArgs().get(1).getClType().getTypeName(), is("U256"));

        assertThat(value.getNamedKeys(), hasSize(5));
        assertThat(value.getNamedKeys().get(4).getName(), is("state"));
        assertThat(value.getNamedKeys().get(4).getKey().toString(), is("uref-c5ae802a50fb72194d3c543805bab1a612186bdf2cc5d62758595694a1928fff-007"));

        Optional<Effect> maybeEffectWithGroup = executionResult.getEffectByKey("hash-b71675d8cf701d9bc584cb5152706873110e5158b004fb966ed28be49c66b39a");
        assertThat(maybeEffectWithGroup.isPresent(), is(true));

        Effect effectWithGroup = executionResult.getEffects().get(29);
        assertThat(effectWithGroup.getKey().toString(), is("hash-b71675d8cf701d9bc584cb5152706873110e5158b004fb966ed28be49c66b39a"));
        assertThat(effectWithGroup.getKind(), is(instanceOf(WriteKind.class)));
        assertThat(((WriteKind<?>) effectWithGroup.getKind()).getWrite().getValue(), is(instanceOf(ContractPackage.class)));
        ContractPackage contractPackage = (ContractPackage) ((WriteKind<?>) effectWithGroup.getKind()).getWrite().getValue();
        assertThat(contractPackage.getAccessKey(), is("uref-5268245ec93d03335ed91c860ac48885fa6ee15156871458a84daa93b2d04f06-007"));

        assertThat(contractPackage.getVersions(), hasSize(1));
        assertThat(contractPackage.getVersions().get(0).getHash(), is("contract-94b21891ae273b17eeb6a1899a52ab952bcc4da2e19626563f88d6cf7ab6a2bd"));
        assertThat(contractPackage.getVersions().get(0).getVersion(), is(1));
        assertThat(contractPackage.getVersions().get(0).getProtocolVersionMajor(), is(2));

        assertThat(contractPackage.getGroups(), hasSize(1));
        assertThat(contractPackage.getGroups().get(0).getName(), is("constructor_group"));
        assertThat(contractPackage.getGroups().get(0).getKeys().get(0), is("uref-248a7b93a71529a51bf94824b76b047d9da6cd504532951ce8abff937355781a-007"));

        Optional<EntryPointV1> maybeEntryPoint = value.getEntryPoint("init");
        assertThat(maybeEntryPoint.isPresent(), is(true));

        entryPoint = maybeEntryPoint.get();
        assertThat(entryPoint.getName(), is("init"));
        assertThat(entryPoint.getEntryPointType(), is(EntryPointType.CALLED));
        assertThat(entryPoint.getAccess(), is(instanceOf(GroupsAccess.class)));
        assertThat(((GroupsAccess) entryPoint.getAccess()).getGroups(), is(hasSize(1)));
        assertThat(((GroupsAccess) entryPoint.getAccess()).getGroups().get(0), is("constructor_group"));
    }

    @Test
    void infoGetTransactionWithDelegatorKind() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("info_get_transaction")
                .withBody("$.params.transaction_hash.Deploy", "67594388afce12d027d2098f0bee6742702738647fb8d422d85f1b2c8b72192c")
                .thenDispatch(getClass().getResource("/transaction-samples/get_transaction_with_delegator_kind.json"));

        final GetTransactionResult result = casperServiceMock.getTransaction(new TransactionHashDeploy("67594388afce12d027d2098f0bee6742702738647fb8d422d85f1b2c8b72192c"));
        assertNotNull(result);
        assertThat(result.getTransaction().get(), is(instanceOf(TransactionV1.class)));
        assertThat(result.getTransaction().get().getHash(), is(new Digest("67594388afce12d027d2098f0bee6742702738647fb8d422d85f1b2c8b72192c")));
    }

    @Test
    void accountPutTransferV1() throws Exception {

        final Secp256k1PublicKey delegator = (Secp256k1PublicKey) Secp256k1PrivateKey.deriveRandomKey().derivePublicKey();
        final Secp256k1PrivateKey privateKey = Secp256k1PrivateKey.deriveRandomKey();

        PublicKey address = PublicKey.fromAbstractPublicKey(privateKey.derivePublicKey());
        final List<NamedArg<?>> args = Arrays.asList(new NamedArg<>("amount", new CLValueU512(BigInteger.valueOf(2500000000L))), new NamedArg<>("delegator", new CLValuePublicKey(PublicKey.fromAbstractPublicKey(delegator))), new NamedArg<>("validator", new CLValuePublicKey(address)), new NamedArg<>("amount", new CLValueU512(new BigInteger("2500000000"))));

        final TransactionV1Payload payload = TransactionV1Payload.builder()
                .fields(Fields.builder()
                        .args(new NamedArgs(args))
                        .target(new Native())
                        .entryPoint(new TransferEntryPoint())
                        .scheduling(new Standard())
                        .build())
                .initiatorAddr(new InitiatorPublicKey(address))
                .ttl(Ttl.builder().ttl("30m").build())
                .chainName("test-chain-name")
                .pricingMode(new FixedPricingMode(0, 5))
                .build();

        final TransactionV1 transaction = TransactionV1.builder()
                .payload(payload)
                .build();

        // generate hashes and sign transaction
        transaction.sign(privateKey);

        assertThat(transaction.getApprovals(), hasSize(1));
        assertThat(transaction.getHash().isValid(), is(true));

        mockNode.withRcpResponseDispatcher()
                .withMethod("account_put_transaction")
                .withBody("$.params.[0].Version1.hash", transaction.getHash().toString())
                .withBody("$.params.[0].Version1.payload.chain_name", "test-chain-name")
                .withBody("$.params.[0].Version1.payload.ttl", "30m")
                .withBody("$.params.[0].Version1.payload.initiator_addr.PublicKey", address.getAlgoTaggedHex())
                .withBody("$.params.[0].Version1.payload.pricing_mode.Fixed.gas_price_tolerance", "5")
                .withBody("$.params.[0].Version1.payload.fields.target", "Native")
                .withBody("$.params.[0].Version1.payload.fields.entry_point", "Transfer")
                .withBody("$.params.[0].Version1.payload.fields.scheduling", "Standard")
                //     .withBody("$.params.[0].Version1.body.transaction_category", "0")
                .withBody("params.[0].Version1.payload.fields.args.Named.[0].[0]", "amount")
                .withBody("params.[0].Version1.payload.fields.args.Named.[0].[1].bytes", "0400f90295")
                .withBody("$.params.[0].Version1.payload.fields.args.Named.[0].[1].cl_type", "U512")
                .thenDispatch(getClass().getResource("/transaction-samples/put-transaction-result.json"));

        final PutTransactionResult result = casperServiceMock.putTransaction(new Transaction(transaction));
        assertThat(result.getApiVersion(), is("2.0.0"));
        assertThat(result.getTransactionHash().toString(), is("52a75f3651e450cc2c3ed534bf130bae2515950707d70bb60067aada30b97ca8"));
    }

    @Test
    void infoGetNativeTargetTransactionByHash() throws Exception {

        mockNode.withRcpResponseDispatcher()
                .withMethod("info_get_transaction")
                .withBody("$.params.transaction_hash.Version1", "9f0bad6b425d30e716cf5418de52ec2bcc6d67a79853a363be8f8843ca195adb")
                .thenDispatch(getClass().getResource("/transaction-samples/get_transaction_native_target.json"));

        final GetTransactionResult txResult = casperServiceMock.getTransaction(new TransactionHashV1("9f0bad6b425d30e716cf5418de52ec2bcc6d67a79853a363be8f8843ca195adb"));
        assertNotNull(txResult);
        assertThat(txResult.getTransaction().get(), is(instanceOf(TransactionV1.class)));
        assertThat(txResult.getTransaction().get().getHash(), is(new Digest("9f0bad6b425d30e716cf5418de52ec2bcc6d67a79853a363be8f8843ca195adb")));

        final ExecutionResult executionResult = txResult.getExecutionInfo().getExecutionResult();
        assertThat(executionResult, is(notNullValue()));
        assertThat(executionResult, is(instanceOf(ExecutionResultV2.class)));

        ExecutionResultV2 resultV2 = (ExecutionResultV2) executionResult;
        assertThat(resultV2.getTransfers(), hasSize(1));
        assertThat(resultV2.getCost(), is(BigInteger.valueOf(100000000L)));
        assertThat(resultV2.getConsumed(), is(BigInteger.valueOf(100000000L)));
        assertThat(resultV2.getLimit(), is(BigInteger.valueOf(100000000L)));
        assertThat(resultV2.getSizeEstimate(), is(465L));
        assertThat(resultV2.getEffects(), hasSize(15));

        final Effect pruneEffect = resultV2.getEffects().get(11);
        assertThat(pruneEffect.getKind(), is(instanceOf(com.casper.sdk.model.transaction.kind.PruneKind.class)));
        assertThat(((PruneKind) pruneEffect.getKind()).getPrune(), is("balance-hold-01254bd59bbd36b5aa106170c96076b24f8e498bfdafd203fe19775e6a3df1d450b52ef96d93010000"));
        assertThat(pruneEffect.getKey(), is(Key.create("balance-hold-01254bd59bbd36b5aa106170c96076b24f8e498bfdafd203fe19775e6a3df1d450b52ef96d93010000")));

        final Effect validatorCreditEffect = resultV2.getEffects().get(14);
        assertThat(validatorCreditEffect.getKind(), is(instanceOf(WriteKind.class)));
        final BidKind value = (BidKind) ((WriteKind) validatorCreditEffect.getKind()).getWrite().getValue();
        assertThat(value, is(instanceOf(ValidatorCredit.class)));
        assertThat(((ValidatorCredit) value).getAmount(), is(BigInteger.valueOf(100000000L)));
        assertThat(((ValidatorCredit) value).getEraId(), is(497L));
        assertThat(((ValidatorCredit) value).getValidatorPublicKey(), is(PublicKey.fromTaggedHexString("01509254f22690fbe7fb6134be574c4fbdb060dfa699964653b99753485e518ea6")));

        final TransferV2 transfer = resultV2.getTransfers().get(0).getTransferV2();
        final AccountHashKey address = (AccountHashKey) transfer.getFrom().getAddress();
        assertThat(address, is(Key.create("account-hash-56befc13a6fd62e18f361700a5e08f966901c34df8041b36ec97d54d605c23de")));
        assertThat(transfer.getTransactionHash(), is(new Digest("9f0bad6b425d30e716cf5418de52ec2bcc6d67a79853a363be8f8843ca195adb")));
        assertThat(transfer.getSource(), is(URef.fromString("uref-254bd59bbd36b5aa106170c96076b24f8e498bfdafd203fe19775e6a3df1d450-007")));
        assertThat(transfer.getTarget(), is(URef.fromString("uref-35c0cdd72775794e15ea73670f9ed6bbc96747229bbdb29f6ea798e4ff0b9f6a-004")));
        assertThat(transfer.getAmount(), is(new BigInteger("2500000000")));
        assertThat(transfer.getGas(), is(new BigInteger("100000000")));

    }


    @Test
    void queryBalanceDetails() throws IOException, DynamicInstanceException {

        mockNode.withRcpResponseDispatcher()
                .withMethod("query_balance_details")
                .withBody("$.params.purse_identifier.purse_uref", "uref-328c317bc7f9fd7d2b5fa9cf3b4c09fc5a8fb59b012b367f096d2426b768179a-007")
                .withBody("$.params.state_identifier.StateRootHash", "a47d8d8bf5226707cc34c4d869e89b6e02096a6dc50b247f34f0b7260050714f")
                .thenDispatch(getClass().getResource("/balance/query_balance_details_result.json"));

        final QueryBalanceDetailsResult result = casperServiceMock.queryBalanceDetails(new PurseUref(URef.fromString("uref-328c317bc7f9fd7d2b5fa9cf3b4c09fc5a8fb59b012b367f096d2426b768179a-007")), new StateRootHashIdentifier("a47d8d8bf5226707cc34c4d869e89b6e02096a6dc50b247f34f0b7260050714f"));

        assertThat(result.getApiVersion(), is("2.0.0"));
        assertThat(result.getTotalBalance(), is(new BigInteger("2000000000000000000000000000000000")));
        assertThat(result.getAvailableBalance(), is(new BigInteger("1000000000000000000000000000000000")));
        assertThat(result.getTotalBalanceProof(), is("0100000006328c317bc7f9fd7d2b5fa9cf3b4c09fc5a8f"));
        assertThat(result.getHolds(), hasSize(1));
        assertThat(result.getHolds().get(0).getTime(), is(1234567890L));
        assertThat(result.getHolds().get(0).getAmount(), is(new BigInteger("1000000000000000000000000000000000")));
        assertThat(result.getHolds().get(0).getProof(), is("0100000006328c317bc7f9fd7d2b5fa9cf3b4c09fc5a8f"));
    }

    @Test
    void stateGetEntityAccount() throws NoSuchAlgorithmException, NoSuchKeyTagException {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_entity")
                .withBody("$.params.entity_identifier.PublicKey", "0138329930033bca4773a6623574ad7870ee39c554f153f15609e200e50049a7de")
                .thenDispatch(getClass().getResource("/entity/getstateentity-account-result.json"));

        final StateEntityResult stateEntityResult = casperServiceMock.getStateEntity(new PublicKeyIdentifier(PublicKey.fromTaggedHexString("0138329930033bca4773a6623574ad7870ee39c554f153f15609e200e50049a7de")), null);

        assertThat(stateEntityResult.getApiVersion(), is("2.0.0"));

        assertInstanceOf(AddressableEntity.class, stateEntityResult.getEntity());

        AddressableEntity entity = (AddressableEntity) stateEntityResult.getEntity();

        assertInstanceOf(Account.class, entity.getEntity().getEntityAddressKind());

        assertThat(entity.getEntity().getByteCodeHash(), is("byte-code-0000000000000000000000000000000000000000000000000000000000000000"));
        assertThat(entity.getEntity().getPackageHash(), is("package-22138fb22c624016fd73cd2abd3285ccd27cb80e20c29f3d94d34e95ee182030"));

        Account account = (Account) entity.getEntity().getEntityAddressKind();

        assertThat(account.getAccount(), is(Key.create("account-hash-aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10")));

        assertThat(entity.getEntity().getMainPurse().getJsonURef(), is("uref-3dfdbde34845bd2e731cf39fba450745eba645b75d5feb3dcf953fd06d69bf14-007"));
        assertThat(entity.getEntity().getAssociatedKeys().get(0).getAccountHash(), is(instanceOf(AccountHashKey.class)));
        assertThat(entity.getEntity().getAssociatedKeys().get(0).getAccountHash(), is(Key.create("account-hash-aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10")));
        assertThat(entity.getEntity().getAssociatedKeys().get(0).getWeight(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getDeployment(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getKeyManagement(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getUpgradeManagement(), is(1));
    }

    @Test
    void stateGetEntitySmartContract() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_entity")
                .withBody("$.params.entity_identifier.EntityAddr", "entity-system-8edaacea88a5bd982c0c1d8cb54ac89564ffb116c1c2fb4475170755a88a6f5")
                .thenDispatch(getClass().getResource("/entity/getstateentity-smartcontract-result.json"));

        final StateEntityResult stateEntityResult = casperServiceMock.getStateEntity(new EntityAddrIdentifier("entity-system-8edaacea88a5bd982c0c1d8cb54ac89564ffb116c1c2fb4475170755a88a6f5"), null);

        assertThat(stateEntityResult.getApiVersion(), is("2.0.0"));

        assertInstanceOf(AddressableEntity.class, stateEntityResult.getEntity());

        AddressableEntity entity = (AddressableEntity) stateEntityResult.getEntity();

        assertInstanceOf(SmartContract.class, entity.getEntity().getEntityAddressKind());

        assertThat(entity.getEntity().getByteCodeHash(), is("byte-code-0000000000000000000000000000000000000000000000000000000000000000"));
        assertThat(entity.getEntity().getPackageHash(), is("package-22138fb22c624016fd73cd2abd3285ccd27cb80e20c29f3d94d34e95ee182030"));

        SmartContract smartContract = (SmartContract) entity.getEntity().getEntityAddressKind();
        assertThat(smartContract.getSmartContract().name(), is((SmartContract.TransactionRuntime.VMCASPERV1.name())));

        assertThat(entity.getEntity().getMainPurse().getJsonURef(), is("uref-3dfdbde34845bd2e731cf39fba450745eba645b75d5feb3dcf953fd06d69bf14-007"));
        assertThat(entity.getEntity().getAssociatedKeys().get(0).getAccountHash().toString(), is("account-hash-aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10"));
        assertThat(entity.getEntity().getAssociatedKeys().get(0).getWeight(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getDeployment(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getKeyManagement(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getUpgradeManagement(), is(1));
    }

    @Test
    void stateGetEntitySystemEntryPointV1() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_entity")
                .withBody("$.params.entity_identifier.EntityAddr", "entity-system-8edaacea88a5bd982c0c1d8cb54ac89564ffb116c1c2fb4475170755a88a6f5")
                .thenDispatch(getClass().getResource("/entity/getstateentity-system-entry-point-v1-result.json"));

        final StateEntityResult stateEntityResult = casperServiceMock.getStateEntity(new EntityAddrIdentifier("entity-system-8edaacea88a5bd982c0c1d8cb54ac89564ffb116c1c2fb4475170755a88a6f5"), null);

        assertThat(stateEntityResult.getApiVersion(), is("2.0.0"));

        assertInstanceOf(AddressableEntity.class, stateEntityResult.getEntity());

        AddressableEntity entity = (AddressableEntity) stateEntityResult.getEntity();

        assertInstanceOf(System.class, entity.getEntity().getEntityAddressKind());

        assertThat(entity.getEntity().getByteCodeHash(), is("byte-code-0000000000000000000000000000000000000000000000000000000000000000"));
        assertThat(entity.getEntity().getPackageHash(), is("package-a8935ee5e02c22cf74f3c6b2b323517897635efc38ea4c03d8aa70a2822f5ac1"));

        System system = (System) entity.getEntity().getEntityAddressKind();
        assertThat(system.getSystem().name(), is(System.SystemEntityType.AUCTION.name()));

        assertThat(entity.getEntity().getActionThresholds().getDeployment(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getKeyManagement(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getUpgradeManagement(), is(1));


        assertThat(entity.getEntryPoints().size(), is(12));
        assertInstanceOf(EntryPointValue.class, entity.getEntryPoints().get(0));
        EntryPoint entryPointV1 = entity.getEntryPoints().get(0).getV1();

        assertThat(entryPointV1.getAccess().getValue().toString(), is(EntryPoint.EntryPointAccessEnum.PUBLIC.name()));

        assertThat(entryPointV1.getArgs().get(0).getName(), is("validator"));
        assertInstanceOf(CLTypePublicKey.class, entryPointV1.getArgs().get(0).getClType());

        assertThat(entryPointV1.getType().name(), is(EntryPoint.EntryPointType.CALLED.name()));
        assertThat(entryPointV1.getPayment().name(), is(EntryPoint.EntryPointPayment.CALLER.name()));
        assertThat(entryPointV1.getName(), is("activate_bid"));

        assertInstanceOf(CLTypeUnit.class, entryPointV1.getRet());

        assertThat(entity.getNamedKeys().size(), is(7));
        assertInstanceOf(NamedKey.class, entity.getNamedKeys().get(0));
    }

    @Test
    void stateGetEntitySystemEntryPointV2() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_entity")
                .withBody("$.params.entity_identifier.EntityAddr", "entity-system-8edaacea88a5bd982c0c1d8cb54ac89564ffb116c1c2fb4475170755a88a6f5")
                .thenDispatch(getClass().getResource("/entity/getstateentity-system-entry-point-v2-result.json"));

        final StateEntityResult stateEntityResult = casperServiceMock.getStateEntity(new EntityAddrIdentifier("entity-system-8edaacea88a5bd982c0c1d8cb54ac89564ffb116c1c2fb4475170755a88a6f5"), null);

        assertThat(stateEntityResult.getApiVersion(), is("2.0.0"));

        assertInstanceOf(AddressableEntity.class, stateEntityResult.getEntity());

        AddressableEntity entity = (AddressableEntity) stateEntityResult.getEntity();

        assertInstanceOf(System.class, entity.getEntity().getEntityAddressKind());

        assertThat(entity.getEntity().getByteCodeHash(), is("byte-code-0000000000000000000000000000000000000000000000000000000000000000"));
        assertThat(entity.getEntity().getPackageHash(), is("package-a8935ee5e02c22cf74f3c6b2b323517897635efc38ea4c03d8aa70a2822f5ac1"));

        System system = (System) entity.getEntity().getEntityAddressKind();
        assertThat(system.getSystem().name(), is(System.SystemEntityType.AUCTION.name()));

        assertThat(entity.getEntity().getActionThresholds().getDeployment(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getKeyManagement(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getUpgradeManagement(), is(1));

        assertThat(entity.getEntryPoints().size(), is(4));
        assertInstanceOf(EntryPointValue.class, entity.getEntryPoints().get(0));
        EntryPointV2 entryPointV2 = entity.getEntryPoints().get(0).getV2();

        assertThat(entryPointV2.getFlags(), is(0));
        assertThat(entryPointV2.getFunctionIndex(), is(1));

        assertThat(entity.getNamedKeys().size(), is(7));
        assertInstanceOf(NamedKey.class, entity.getNamedKeys().get(0));
    }

    @Test
    void stateGetEntityLegacyAccount() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_entity")
                .withBody("$.params.entity_identifier.AccountHash", "account-hash-f1075fce3b8cd4eab748b8705ca02444a5e35c0248662649013d8a5cb2b1a87c")
                .thenDispatch(getClass().getResource("/entity/getstateentity-legacy-account-result.json"));

        final StateEntityResult stateEntityResult = casperServiceMock.getStateEntity(new AccountHashIdentifier("account-hash-f1075fce3b8cd4eab748b8705ca02444a5e35c0248662649013d8a5cb2b1a87c"), null);

        assertThat(stateEntityResult.getApiVersion(), is("2.0.0"));

        assertInstanceOf(LegacyAccount.class, stateEntityResult.getEntity());

        LegacyAccount account = (LegacyAccount) stateEntityResult.getEntity();

        assertThat(account.getHash().toString(), is("account-hash-f1075fce3b8cd4eab748b8705ca02444a5e35c0248662649013d8a5cb2b1a87c"));
        assertThat(account.getMainPurse(), is("uref-b22bc80d357df47447074e243b4d888de67c1cc7565fa82d0bb2b9b023146748-007"));
        assertThat(account.getAssociatedKeys().get(0).getAccountHash(), is(instanceOf(AccountHashKey.class)));
        assertThat(account.getAssociatedKeys().get(0).getAccountHash().toString(), is("account-hash-f1075fce3b8cd4eab748b8705ca02444a5e35c0248662649013d8a5cb2b1a87c"));
        assertThat(account.getAssociatedKeys().get(0).getWeight(), is(1));
    }

    @Test
    void stateGetEntityAccountWithHashBlockIdentifier() throws NoSuchKeyTagException {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_entity")
                .withBody("$.params.entity_identifier.AccountHash", "account-hash-f1075fce3b8cd4eab748b8705ca02444a5e35c0248662649013d8a5cb2b1a87c")
                .withBody("$.params.block_identifier.Hash", "b0b28b6e89522a2c9476d477208f603cb9911dae77dad61da66346d56764ee8b")
                .thenDispatch(getClass().getResource("/entity/getstateentity-account-result.json"));

        final StateEntityResult stateEntityResult = casperServiceMock.getStateEntity(new AccountHashIdentifier("account-hash-f1075fce3b8cd4eab748b8705ca02444a5e35c0248662649013d8a5cb2b1a87c"), new HashBlockIdentifier("b0b28b6e89522a2c9476d477208f603cb9911dae77dad61da66346d56764ee8b") {
        });

        assertThat(stateEntityResult.getApiVersion(), is("2.0.0"));

        assertInstanceOf(AddressableEntity.class, stateEntityResult.getEntity());

        AddressableEntity entity = (AddressableEntity) stateEntityResult.getEntity();

        assertInstanceOf(Account.class, entity.getEntity().getEntityAddressKind());

        assertThat(entity.getEntity().getByteCodeHash(), is("byte-code-0000000000000000000000000000000000000000000000000000000000000000"));
        assertThat(entity.getEntity().getPackageHash(), is("package-22138fb22c624016fd73cd2abd3285ccd27cb80e20c29f3d94d34e95ee182030"));

        Account account = (Account) entity.getEntity().getEntityAddressKind();
        assertThat(account.getAccount(), is(Key.create("account-hash-aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10")));

        assertThat(entity.getEntity().getMainPurse().getJsonURef(), is("uref-3dfdbde34845bd2e731cf39fba450745eba645b75d5feb3dcf953fd06d69bf14-007"));
        assertThat(entity.getEntity().getAssociatedKeys().get(0).getAccountHash(), is(instanceOf(AccountHashKey.class)));
        assertThat(entity.getEntity().getAssociatedKeys().get(0).getAccountHash().toString(), is("account-hash-aab0da01340446cee477f28410f8af5d6e0f3a88fb26c0cafb8d1625f5cc9c10"));
        assertThat(entity.getEntity().getAssociatedKeys().get(0).getWeight(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getDeployment(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getKeyManagement(), is(1));
        assertThat(entity.getEntity().getActionThresholds().getUpgradeManagement(), is(1));
    }

    @Test
    void stateGetEntityContractActualCCTLReturnedData() {

        mockNode.withRcpResponseDispatcher()
                .withMethod("state_get_entity")
                .withBody("$.params.entity_identifier.EntityAddr", "entity-contract-77a0481b28572054cbdd19c944a7176ce9670be616d6de0e2ec3f89ca378dd79")
                .thenDispatch(getClass().getResource("/entity/getstateentity-cctl-returned-contract-entity.json"));

        final StateEntityResult stateEntityResult = casperServiceMock.getStateEntity(new EntityAddrIdentifier("entity-contract-77a0481b28572054cbdd19c944a7176ce9670be616d6de0e2ec3f89ca378dd79"), null);

        assertThat(stateEntityResult.getApiVersion(), is("2.0.0"));

        assertInstanceOf(AddressableEntity.class, stateEntityResult.getEntity());

        AddressableEntity entity = (AddressableEntity) stateEntityResult.getEntity();

        assertInstanceOf(SmartContract.class, entity.getEntity().getEntityAddressKind());

        assertThat(entity.getEntity().getByteCodeHash(), is("byte-code-10fcbe236e5a946b16a86db1584c6c1690ef6dfa9da2ffae68c256f9738e1e66"));
        assertThat(entity.getEntity().getPackageHash(), is("package-69131083f5ea0b6d8e3df0b80b03e6e20788d594ad9c7d4de79700010f767448"));
        assertThat(entity.getEntity().getMainPurse().getJsonURef(), is("uref-0249b6da571bad31cfed8269767ba77ce1b2f99446d395d80865457a440c9cb3-007"));

        final SmartContract contract = (SmartContract) entity.getEntity().getEntityAddressKind();
        assertThat(contract.getSmartContract(), is(SmartContract.TransactionRuntime.VMCASPERV1));

        assertThat(entity.getEntryPoints().size(), is(15));
        assertThat(entity.getNamedKeys().size(), is(11));

        assertThat(entity.getNamedKeys().get(0).getKey(), is(instanceOf(URefKey.class)));
        assertThat(entity.getNamedKeys().get(0).getKey().toString(), is("uref-a18b3997bc0bafe8612973531e6782be4f1251f90f0513c0b34fea69df1a95ff-007"));
        assertThat(entity.getNamedKeys().get(0).getName(), is("allowances"));

        assertThat(entity.getEntryPoints().get(14).getV1().getAccess(), is(EntryPoint.EntryPointAccessEnum.PUBLIC));
        assertThat(entity.getEntryPoints().get(14).getV1().getArgs().get(0).getName(), is("address"));
        assertThat(entity.getEntryPoints().get(14).getV1().getArgs().get(0).getClType().getTypeName(), is("Key"));
        assertThat(entity.getEntryPoints().get(14).getV1().getType(), is(EntryPoint.EntryPointType.CALLED));
        assertThat(entity.getEntryPoints().get(14).getV1().getPayment(), is(EntryPoint.EntryPointPayment.CALLER));
        assertThat(entity.getEntryPoints().get(14).getV1().getRet().getTypeName(), is("U256"));
    }

    @Test
    void infoGetReward() throws NoSuchAlgorithmException {

        mockNode.withRcpResponseDispatcher()
                .withMethod("info_get_reward")
                .withBody("$.params.validator", "010b277da84a12c8814d5723eeb57123ff287f22466fd13faca1bb1fae57d2679b").withBody("$.params.delegator", "01098d1758f1ca75350dfec8a1c4c1984a88d1ea5eab5590fbc9e856d67cde31eb")
                .withBody("$.params.era_identifier.Block.Hash", "709a31cbaff23da43995e78d2209e7f5980905cf70ef850f6744b8d3cec9af13")
                .thenDispatch(getClass().getResource("/reward/info_get_reward_result.json"));

        final GetRewardResult rewardInfo = casperServiceMock.getReward(BlockEraIdentifier.builder().blockIdentifier(HashBlockIdentifier.builder().hash("709a31cbaff23da43995e78d2209e7f5980905cf70ef850f6744b8d3cec9af13").build()).build(), PublicKey.fromTaggedHexString("010b277da84a12c8814d5723eeb57123ff287f22466fd13faca1bb1fae57d2679b"), PublicKey.fromTaggedHexString("01098d1758f1ca75350dfec8a1c4c1984a88d1ea5eab5590fbc9e856d67cde31eb"));

        assertThat(rewardInfo.getApiVersion(), is("2.0.0"));
        assertThat(rewardInfo.getRewardAmount(), is(new BigInteger("100000000000000")));
        assertThat(rewardInfo.getEraId(), is(123456L));
    }
}
