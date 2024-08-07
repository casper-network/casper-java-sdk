package com;

import com.casper.sdk.identifier.block.BlockIdentifier;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.block.HeightBlockIdentifier;
import com.casper.sdk.identifier.dictionary.AccountNamedKeyDictionaryIdentifier;
import com.casper.sdk.identifier.dictionary.StringDictionaryIdentifier;
import com.casper.sdk.identifier.entity.EntityAddrIdentifier;
import com.casper.sdk.identifier.entity.EntityAddrIdentifier;
import com.casper.sdk.identifier.entity.EntityIdentifier;
import com.casper.sdk.identifier.era.IdEraIdentifier;
import com.casper.sdk.identifier.global.StateRootHashIdentifier;
import com.casper.sdk.identifier.purse.MainPurseUnderPublickey;
import com.casper.sdk.identifier.purse.PurseIdentifier;
import com.casper.sdk.model.account.AccountData;
import com.casper.sdk.model.account.PublicKeyIdentifier;
import com.casper.sdk.model.auction.AuctionData;
import com.casper.sdk.model.balance.QueryBalanceData;
import com.casper.sdk.model.balance.QueryBalanceDetailsResult;
import com.casper.sdk.model.block.ChainGetBlockResult;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Delegator;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.deploy.Validator;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.casper.sdk.model.dictionary.DictionaryData;
import com.casper.sdk.model.entity.AddressableEntity;
import com.casper.sdk.model.entity.StateEntityResult;
import com.casper.sdk.model.era.EraInfoData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.reward.GetRewardResult;
import com.casper.sdk.model.stateroothash.StateRootHashData;
import com.casper.sdk.model.status.ChainspecData;
import com.casper.sdk.model.status.StatusData;
import com.casper.sdk.model.transaction.*;
import com.casper.sdk.model.transaction.entrypoint.TransferEntryPoint;
import com.casper.sdk.model.transaction.pricing.FixedPricingMode;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.model.transfer.TransferData;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.service.CasperService;
import com.syntifi.crypto.key.*;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.apache.cxf.helpers.IOUtils;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static com.casper.sdk.helper.CasperDeployHelper.getPaymentModuleBytes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author carl@stormeye.co.uk
 */
public class HowTo {

    private CasperService casperService;

    @BeforeEach
    public void connect() throws MalformedURLException {
        casperService = CasperService.usingPeer("3.20.57.210", 7777);
//        casperService = CasperService.usingPeer("127.0.0.1", 21101);
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

        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLastSwitchBlockHash().toString()));
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
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLastSwitchBlockHash().toString()));
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
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLastSwitchBlockHash().toString()));
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
    @Deprecated
    void getAccountInfo() {

        final StatusData status = casperService.getStatus();
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLastSwitchBlockHash().toString()));
        final PublicKey delegator = ((Delegator) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorPublicKey();

        //By public key and block hash
        final AccountData stateAccountInfoKeyAndHash = casperService.getStateAccountInfo(delegator.toString(), new HashBlockIdentifier(status.getLastSwitchBlockHash().toString()));
        assert stateAccountInfoKeyAndHash.getAccount().getMainPurse() != null;

        //By public key
        final AccountData stateAccountInfoKey = casperService.getStateAccountInfo(delegator.toString(), null);
        assert stateAccountInfoKey.getAccount().getMainPurse() != null;

    }

    @Test
    /* TODO: Make it work */
    void getDictionaryItem() throws IOException {

        final StatusData status = casperService.getStatus();
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLastSwitchBlockHash().toString()));
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
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLastSwitchBlockHash().toString()));
        final PublicKey delegator = ((Delegator) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorPublicKey();
        final EntityIdentifier publicKeyEntityIdentifier = new PublicKeyIdentifier(delegator);

        //By public key
        final StateEntityResult stateEntityPublicKey = casperService.getStateEntity(publicKeyEntityIdentifier, null);
        assert stateEntityPublicKey.getEntity() != null;

        //By contract identifier
        final String contractKey = ((AddressableEntity) stateEntityPublicKey.getEntity()).getNamedKeys().get(0).getKey();
        final StateEntityResult stateEntityContract = casperService.getStateEntity(new EntityAddrIdentifier(contractKey), null);
        assert stateEntityContract.getEntity() != null;

    }

    @Test
    void getAuctionInfo() {
        final ChainGetBlockResult block = casperService.getBlock();
        assert block.getBlockWithSignatures().getBlock() != null;

        //By block hash
        final AuctionData auctionInfoHash = casperService.getStateAuctionInfo(
                new HashBlockIdentifier(block.getBlockWithSignatures().getBlock().getHash().toString()));
        assert auctionInfoHash.getAuctionState() != null;

        //By block height
        final AuctionData auctionInfoHeight = casperService.getStateAuctionInfo(
                new HeightBlockIdentifier(block.getBlockWithSignatures().getBlock().getHeader().getHeight()));
        assert auctionInfoHeight.getAuctionState() != null;

    }


    @Test
    void nativeTransfer() throws IOException, ValueSerializationException {
        final StatusData status = casperService.getStatus();

        final String user1 = "howto/keys/user-1/secret_key.pem";
        final Ed25519PrivateKey user1PrivateKey = new Ed25519PrivateKey();
        user1PrivateKey.readPrivateKey(user1);

        final AbstractPublicKey user1DerivedPublicKey = user1PrivateKey.derivePublicKey();
        final PublicKey user1PublicKey = PublicKey.fromAbstractPublicKey(user1DerivedPublicKey);

        StateEntityResult stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(user1PublicKey), null);
        final URef user1Purse = ((AddressableEntity) stateEntity.getEntity()).getEntity().getMainPurse();

        final String user2 = "howto/keys/user-2/public.pem";
        final Ed25519PublicKey user2PublicKey = new Ed25519PublicKey();
        user2PublicKey.readPublicKey(user2);

        stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(PublicKey.fromAbstractPublicKey(user2PublicKey)), null);
        final URef user2Purse = ((AddressableEntity) stateEntity.getEntity()).getEntity().getMainPurse();

        final TransactionV1Header header = TransactionV1Header.builder()
                .chainName(status.getChainSpecName())
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new FixedPricingMode(1))
                .initiatorAddr(new InitiatorPublicKey(PublicKey.fromAbstractPublicKey(user2PublicKey)))
                .build();

        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("source", new CLValueOption(Optional.of(new CLValueURef(user1Purse)))),
                new NamedArg<>("target", new CLValueURef(user2Purse)),
                new NamedArg<>("amount", new CLValueU512(new BigInteger("2500000000"))),
                new NamedArg<>("id", new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(System.currentTimeMillis())))))
        );


        final TransactionV1Body body = TransactionV1Body.builder()
                .args(args)
                .target(new Native())
                .entryPoint(new TransferEntryPoint())
                .scheduling(new Standard())
                .transactionCategory(TransactionCategory.MINT)
                .build();

        final TransactionV1 transactionV1 = TransactionV1.builder()
                .header(header)
                .body(body)
                .build();

        final Transaction transaction = new Transaction(transactionV1.sign(user1PrivateKey));

        final PutTransactionResult result = casperService.putTransaction(transaction);

        assert result != null;
        assert result.getTransactionHash() != null;

    }

    @Test
    void installContract() throws URISyntaxException, IOException, ValueSerializationException {
        final StatusData status = casperService.getStatus();

        final AbstractPrivateKey privKey = new Ed25519PrivateKey();
        final String privateKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/secret_key.pem")).toURI()).toString();
        privKey.readPrivateKey(privateKeyPath);

        final String wasmPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/contracts/cep18.wasm")).toURI()).toString();
        final URL wasmUrl = new URL("file://" + wasmPath);
        final byte[] wasmBytes = IOUtils.readBytesFromStream(wasmUrl.openStream());

        final TransactionV1Header header = TransactionV1Header.builder()
                .chainName(status.getChainSpecName())
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new FixedPricingMode(8))
                .initiatorAddr(new InitiatorPublicKey(PublicKey.fromAbstractPublicKey(privKey.derivePublicKey())))
                .build();

        final List<NamedArg<?>> paymentArgs = new LinkedList<>();
        paymentArgs.add(new NamedArg<>("token_decimals", new CLValueU8((byte) 11)));
        paymentArgs.add(new NamedArg<>("token_name", new CLValueString("Acme Token")));
        paymentArgs.add(new NamedArg<>("token_symbol", new CLValueString("ACME")));
        paymentArgs.add(new NamedArg<>("events_mode", new CLValueU8((byte) 0)));
        paymentArgs.add(new NamedArg<>("token_total_supply", new CLValueU256(BigDecimal.valueOf(1e15).toBigInteger())));

        final ModuleBytes session = ModuleBytes.builder().bytes(wasmBytes).args(paymentArgs).build();
        final ModuleBytes paymentModuleBytes = getPaymentModuleBytes(new BigInteger("50000000000"));

        final TransactionV1Body body = TransactionV1Body.builder()
                .args(paymentArgs)
                .target(new Native())
                .entryPoint(new TransferEntryPoint())
                .scheduling(new Standard())
                .transactionCategory(TransactionCategory.MINT)
                .build();

        final TransactionV1 transactionV1 = TransactionV1.builder()
                .header(header)
                .body(body)
                .build();


//        final Transaction transaction = new Transaction()



    }


    @Test
    void getTransaction() {

        GetTransactionResult getTransactionResult = casperService.getTransaction(new TransactionHashV1("d529a5ada83e6368da1e1f48a1390d6b09ac746fdf4269cee3beacfa945fd98c"));
        assert getTransactionResult.getTransaction() != null;

    }


}
