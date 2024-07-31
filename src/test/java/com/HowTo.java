package com;

import com.casper.sdk.identifier.block.BlockIdentifier;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.block.HeightBlockIdentifier;
import com.casper.sdk.identifier.dictionary.AccountNamedKeyDictionaryIdentifier;
import com.casper.sdk.identifier.dictionary.StringDictionaryIdentifier;
import com.casper.sdk.identifier.entity.EntityAddrIdentifier;
import com.casper.sdk.identifier.entity.PublicKeyEntityIdentifier;
import com.casper.sdk.identifier.era.IdEraIdentifier;
import com.casper.sdk.identifier.global.StateRootHashIdentifier;
import com.casper.sdk.identifier.purse.MainPurseUnderPublickey;
import com.casper.sdk.identifier.purse.PurseIdentifier;
import com.casper.sdk.model.account.AccountData;
import com.casper.sdk.model.balance.QueryBalanceData;
import com.casper.sdk.model.balance.QueryBalanceDetailsResult;
import com.casper.sdk.model.block.ChainGetBlockResult;
import com.casper.sdk.model.deploy.Delegator;
import com.casper.sdk.model.deploy.Validator;
import com.casper.sdk.model.dictionary.DictionaryData;
import com.casper.sdk.model.entity.AddressableEntity;
import com.casper.sdk.model.entity.StateEntityResult;
import com.casper.sdk.model.era.EraInfoData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.reward.GetRewardResult;
import com.casper.sdk.model.stateroothash.StateRootHashData;
import com.casper.sdk.model.status.ChainspecData;
import com.casper.sdk.model.status.StatusData;
import com.casper.sdk.model.transfer.TransferData;
import com.casper.sdk.service.CasperService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.security.NoSuchAlgorithmException;

/**
 * @author carl@stormeye.co.uk
 */
public class HowTo {

    private CasperService casperService;

    @BeforeEach
    public void connect() throws MalformedURLException {
        casperService = CasperService.usingPeer("3.20.57.210", 7777);
    }

    @Test
    void getChainSpec() {

        final ChainspecData chainspec = casperService.getChainspec();
        assert chainspec.getChainspec() != null;

    }

    @Test
    void getBlock() {

        //Latest
        final ChainGetBlockResult block = casperService.getBlock();
        assert block.getBlockWithSignatures().getBlock() != null;

        //By block hash
        final ChainGetBlockResult blockHash = casperService.getBlock(new HashBlockIdentifier(block.getBlockWithSignatures().getBlock().getHash().toString()));
        assert blockHash.getBlockWithSignatures().getBlock() != null;

        //By block height
        final ChainGetBlockResult blockHeight = casperService.getBlock(new HeightBlockIdentifier(block.getBlockWithSignatures().getBlock().getHeader().getHeight()));
        assert blockHeight.getBlockWithSignatures().getBlock() != null;

    }

    @Test
    void getBlockTransfers() {

        //Latest
        final TransferData transfers = casperService.getBlockTransfers();
        assert transfers.getBlockHash() != null;

        //By block hash
        final TransferData blockHashTransfer = casperService.getBlockTransfers(new HashBlockIdentifier(transfers.getBlockHash()));
        assert blockHashTransfer.getBlockHash() != null;

        //By block height
        final ChainGetBlockResult block = casperService.getBlock(new HashBlockIdentifier(blockHashTransfer.getBlockHash()));
        final TransferData blockHeightTransfer = casperService.getBlockTransfers(new HeightBlockIdentifier(block.getBlockWithSignatures().getBlock().getHeader().getHeight()));
        assert blockHeightTransfer.getBlockHash() != null;

    }


    @Test
    void queryEra() {

        final ChainGetBlockResult block = casperService.getBlock();
        assert block.getBlockWithSignatures().getBlock() != null;

        //By block hash
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(block.getBlockWithSignatures().getBlock().getHash().toString()));
        assert eraSummaryBlockHash.getEraSummary().getEraId() != null;

        //By block height
        final EraInfoData eraSummaryBlockHeight = casperService.getEraSummary(new HeightBlockIdentifier(block.getBlockWithSignatures().getBlock().getHeader().getHeight()));
        assert eraSummaryBlockHeight.getEraSummary().getEraId() != null;

    }

    @Test
    void queryStateRootHash() {
        final ChainGetBlockResult block = casperService.getBlock();
        assert block.getBlockWithSignatures().getBlock() != null;

        //By last block
        final StateRootHashData stateRootData = casperService.getStateRootHash();
        assert stateRootData.getStateRootHash() != null;

        //By block hash
        final StateRootHashData stateRootDataBlockHash = casperService.getStateRootHash(new HashBlockIdentifier(block.getBlockWithSignatures().getBlock().getHash().toString()));
        assert stateRootDataBlockHash.getStateRootHash() != null;

        //By block height
        final StateRootHashData stateRootDataBlockHeight = casperService.getStateRootHash(new HeightBlockIdentifier(block.getBlockWithSignatures().getBlock().getHeader().getHeight()));
        assert stateRootDataBlockHeight.getStateRootHash() != null;

    }

    @Test
    void getRewards() {

        final StatusData status = casperService.getStatus();

        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLatestSwitchBlockHash()));
        assert eraSummaryBlockHash.getEraSummary().getEraId() != null;

        //By validator, era identifier and delegator
        final PublicKey delegator = ((Delegator) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorPublicKey();
        final PublicKey validator = ((Delegator) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getValidatorPublicKey();

        GetRewardResult reward = casperService.getReward(new IdEraIdentifier(eraSummaryBlockHash.getEraSummary().getEraId()),
                validator,
                delegator
        );
        assert reward.getRewardAmount() != null;

        //By validator
        reward = casperService.getReward(null,
                validator,
                null
        );
        assert reward.getRewardAmount() != null;

        //By validator and era
        reward = casperService.getReward(new IdEraIdentifier(eraSummaryBlockHash.getEraSummary().getEraId()),
                validator,
                null
        );
        assert reward.getRewardAmount() != null;

        //By validator and delegator
        reward = casperService.getReward(null,
                validator,
                delegator
        );
        assert reward.getRewardAmount() != null;
    }


    @Test
    /* TODO - When put transaction is working in the SDK */
    void queryTransaction() {}

    @Test
    /* TODO - When put transaction is working in the SDK */
    void putTransaction() {}

    @Test
    void queryBalance() {
        final StatusData status = casperService.getStatus();
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLatestSwitchBlockHash()));
        final PublicKey delegator = ((Delegator) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorPublicKey();
        final PurseIdentifier purseIdentifier = new MainPurseUnderPublickey(delegator);
        final StateRootHashIdentifier stateRootHashIdentifier = new StateRootHashIdentifier(casperService.getStateRootHash().getStateRootHash());

        //By state identifier and purse identifier
        final QueryBalanceData queryBalanceStateAndPurse = casperService.queryBalance(stateRootHashIdentifier, purseIdentifier);
        assert queryBalanceStateAndPurse.getBalance() != null;

        //By purse identifier
        final QueryBalanceData queryBalanceDataPurse = casperService.queryBalance(null, purseIdentifier);
        assert queryBalanceDataPurse.getBalance() != null;

    }

    @Test
    void queryBalanceDetails() {
        final StatusData status = casperService.getStatus();
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLatestSwitchBlockHash()));
        final PublicKey delegator = ((Delegator) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorPublicKey();
        final PurseIdentifier purseIdentifier = new MainPurseUnderPublickey(delegator);
        final StateRootHashIdentifier stateRootHashIdentifier = new StateRootHashIdentifier(casperService.getStateRootHash().getStateRootHash());

        //By state identifier and purse identifier
        final QueryBalanceDetailsResult queryBalanceDetailsStateAndPurse = casperService.queryBalanceDetails(purseIdentifier, stateRootHashIdentifier);
        assert queryBalanceDetailsStateAndPurse.getAvailableBalance() != null;

        //By purse identifier
        final QueryBalanceDetailsResult queryBalanceDetailsDataPurse = casperService.queryBalanceDetails(purseIdentifier, null);
        assert queryBalanceDetailsDataPurse.getAvailableBalance() != null;

    }

    @Test
    /* TODO - When put transaction is working in the SDK */
    void queryGlobalState() {}


    @Test
    void getAccountInfo() {

        final StatusData status = casperService.getStatus();
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLatestSwitchBlockHash()));
        final PublicKey delegator = ((Delegator) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorPublicKey();

        //By public key and block hash
        final AccountData stateAccountInfoKeyAndHash = casperService.getStateAccountInfo(delegator.toString(), new HashBlockIdentifier(status.getLatestSwitchBlockHash()));
        assert stateAccountInfoKeyAndHash.getAccount().getMainPurse() != null;

        //By public key
        final AccountData stateAccountInfoKey = casperService.getStateAccountInfo(delegator.toString(), null);
        assert stateAccountInfoKey.getAccount().getMainPurse() != null;

    }

    @Test
    /* TODO: Make it work */
    void getDictionaryItem() throws IOException {

        final StatusData status = casperService.getStatus();
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLatestSwitchBlockHash()));
        final PublicKey delegator = ((Delegator) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorPublicKey();

        String accountHash = delegator.generateAccountHash(true);

        final StringDictionaryIdentifier key = StringDictionaryIdentifier.builder().dictionary(accountHash).build();

        final DictionaryData stateDictionaryItem = casperService.getStateDictionaryItem(
                casperService.getStateRootHash().getStateRootHash(),
                key
        );

        assert stateDictionaryItem.getDictionaryKey() != null;

    }


    @Test
    void getStateEntity() {
        final StatusData status = casperService.getStatus();
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLatestSwitchBlockHash()));
        final PublicKey delegator = ((Delegator) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorPublicKey();
        final PublicKeyEntityIdentifier publicKeyEntityIdentifier = new PublicKeyEntityIdentifier(delegator);

        //By public key
        final StateEntityResult stateEntityPublicKey = casperService.getStateEntity(publicKeyEntityIdentifier, null);
        assert stateEntityPublicKey.getEntity() != null;

        //By contract identifier
        final String contractKey = ((AddressableEntity) stateEntityPublicKey.getEntity()).getNamedKeys().get(0).getKey();
        final StateEntityResult stateEntityContract = casperService.getStateEntity(new EntityAddrIdentifier(contractKey), null);
        assert stateEntityContract.getEntity() != null;

    }


}
