package com;

import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.block.HeightBlockIdentifier;
import com.casper.sdk.identifier.dictionary.StringDictionaryIdentifier;
import com.casper.sdk.identifier.entity.EntityAddrIdentifier;
import com.casper.sdk.identifier.era.IdEraIdentifier;
import com.casper.sdk.identifier.global.StateRootHashIdentifier;
import com.casper.sdk.identifier.purse.MainPurseUnderPublickey;
import com.casper.sdk.identifier.purse.PurseIdentifier;
import com.casper.sdk.model.account.AccountData;
import com.casper.sdk.model.account.PublicKeyIdentifier;
import com.casper.sdk.model.balance.QueryBalanceData;
import com.casper.sdk.model.balance.QueryBalanceDetailsResult;
import com.casper.sdk.model.block.ChainGetBlockResult;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Delegator;
import com.casper.sdk.model.deploy.NamedArg;
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
import com.casper.sdk.model.transaction.entrypoint.CallEntryPoint;
import com.casper.sdk.model.transaction.entrypoint.TransferEntryPoint;
import com.casper.sdk.model.transaction.pricing.FixedPricingMode;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.model.transaction.target.TransactionRuntime;
import com.casper.sdk.model.transfer.TransferData;
import com.casper.sdk.model.uref.URef;
import com.casper.sdk.service.CasperService;
import com.syntifi.crypto.key.AbstractPublicKey;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.apache.cxf.helpers.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * @author carl@stormeye.co.uk
 */
@Disabled
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
    void queryGlobalState() {
    }


    @Test
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
        final PublicKeyIdentifier publicKeyEntityIdentifier = new PublicKeyIdentifier(delegator);

        //By public key
        final StateEntityResult stateEntityPublicKey = casperService.getStateEntity(publicKeyEntityIdentifier, null);
        assert stateEntityPublicKey.getEntity() != null;

        //By contract identifier
        final String contractKey = ((AddressableEntity) stateEntityPublicKey.getEntity()).getNamedKeys().get(0).getKey();
        final StateEntityResult stateEntityContract = casperService.getStateEntity(new EntityAddrIdentifier(contractKey), null);
        assert stateEntityContract.getEntity() != null;

    }


    @Test
    void putTransactionNative() throws IOException, ValueSerializationException, TimeoutException, URISyntaxException, NoSuchAlgorithmException {

        //Get the senders private key
        //Add your own private key here
        final String secretKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/secret_key.pem")).toURI()).toString();
        final Ed25519PrivateKey senderPrivateKey = new Ed25519PrivateKey();
        senderPrivateKey.readPrivateKey(secretKeyPath);

        //Derive the senders public key from the private key
        final AbstractPublicKey senderAbstractPublicKey = senderPrivateKey.derivePublicKey();
        assertThat(senderAbstractPublicKey, is(notNullValue()));
        final PublicKey senderPublicKey = PublicKey.fromAbstractPublicKey(senderAbstractPublicKey);

        //Get the senders purse
        StateEntityResult stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(senderPublicKey), null);
        assertThat(stateEntity, is(notNullValue()));
        final URef senderPurse = ((AddressableEntity) stateEntity.getEntity()).getEntity().getMainPurse();
        assertThat(senderPurse, is(notNullValue()));

        //Get a receivers public key - random delegator from last block
        final StatusData status = casperService.getStatus();
        final EraInfoData eraSummaryBlockHash = casperService.getEraSummary(new HashBlockIdentifier(status.getLastSwitchBlockHash().toString()));
        final PublicKey delegator = ((Delegator) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorPublicKey();
        final PublicKey receiverPublicKey = PublicKey.fromAbstractPublicKey(delegator.getPubKey());

        //Get the receivers purse
        stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(PublicKey.fromAbstractPublicKey(receiverPublicKey.getPubKey())), null);
        assertThat(stateEntity, is(notNullValue()));
        final URef receiverPurse = ((AddressableEntity) stateEntity.getEntity()).getEntity().getMainPurse();
        assertThat(receiverPurse, is(notNullValue()));

        //Build the transaction header
        final TransactionV1Header header = TransactionV1Header.builder()
                .chainName(status.getChainSpecName())
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new FixedPricingMode(1))
                .initiatorAddr(new InitiatorPublicKey(senderPublicKey))
                .build();

        //Add the required arguments
        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("source", new CLValueOption(Optional.of(new CLValueURef(senderPurse)))),
                new NamedArg<>("target", new CLValueURef(receiverPurse)),
                new NamedArg<>("amount", new CLValueU512(new BigInteger("2500000000"))),
                new NamedArg<>("id", new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(System.currentTimeMillis())))))
        );

        //Build the transaction body
        final TransactionV1Body body = TransactionV1Body.builder()
                .args(args)
                .target(new Native())
                .entryPoint(new TransferEntryPoint())
                .scheduling(new Standard())
                .transactionCategory(TransactionCategory.MINT)
                .build();

        //Build the transaction
        final TransactionV1 transactionV1 = TransactionV1.builder()
                .header(header)
                .body(body)
                .build();

        //Sign the transaction
        final Transaction transaction = new Transaction(transactionV1.sign(senderPrivateKey));

        //Deploy the transaction
        final PutTransactionResult result = casperService.putTransaction(transaction);

        assertThat(result, is(notNullValue()));
        assertThat(result.getTransactionHash(), is(transaction.get().getHash()));

        final GetTransactionResult transactionResult = waitForTransaction(result.getTransactionHash());
        assertThat(transactionResult, is(notNullValue()));

    }

    @Test
    void putTransactionContractCep18() throws IOException, URISyntaxException, ValueSerializationException, TimeoutException {

        //Get the senders private key
        //Add your own private key here
        final String secretKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/secret_key.pem")).toURI()).toString();
        final Ed25519PrivateKey senderPrivateKey = new Ed25519PrivateKey();
        senderPrivateKey.readPrivateKey(secretKeyPath);

        final String wasmPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/contracts/cep18.wasm")).toURI()).toString();
        final URL wasmUrl = new URL("file://" + wasmPath);
        final byte[] wasmBytes = IOUtils.readBytesFromStream(wasmUrl.openStream());


        final TransactionV1Header header = TransactionV1Header.builder()
                .chainName(casperService.getStatus().getChainSpecName())
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new FixedPricingMode(8))
                .initiatorAddr(new InitiatorPublicKey(PublicKey.fromAbstractPublicKey(senderPrivateKey.derivePublicKey())))
                .build();


        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("decimals", new CLValueU8((byte) 11)),
                new NamedArg<>("name", new CLValueString("Acme Token")),
                new NamedArg<>("symbol", new CLValueString("ACME")),
                new NamedArg<>("events_mode", new CLValueU8((byte) 0)),
                new NamedArg<>("id", new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(System.currentTimeMillis())))))
        );

        final TransactionV1Body body = TransactionV1Body.builder()
                .args(args)
                .target(new Session(wasmBytes, TransactionRuntime.VM_CASPER_V2))
                .entryPoint(new CallEntryPoint())
                .scheduling(new Standard())
                .transactionCategory(TransactionCategory.INSTALL_UPGRADE)
                .build();

        final TransactionV1 transactionV1 = TransactionV1.builder()
                .header(header)
                .body(body)
                .build();


        final Transaction transaction = new Transaction(transactionV1.sign(senderPrivateKey));

        final PutTransactionResult result = casperService.putTransaction(transaction);

        assert result != null;
        assert result.getTransactionHash() != null;

        final GetTransactionResult transactionResult = waitForTransaction(result.getTransactionHash());
        assertThat(transactionResult, is(notNullValue()));

    }

    private GetTransactionResult waitForTransaction(final TransactionHash hash) throws TimeoutException {

        final long timeout = 300 * 1000L;
        final long now = System.currentTimeMillis();

        GetTransactionResult result = null;

        while (result == null || result.getExecutionInfo() == null) {

            result = casperService.getTransaction(hash);

            if (result.getExecutionInfo() != null && System.currentTimeMillis() > now + timeout) {
                throw new TimeoutException("Timed-out waiting for transaction deploy " + hash);
            }

            try {
                //noinspection BusyWait
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

        return result;

    }
}
