package com;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.identifier.block.HeightBlockIdentifier;
import com.casper.sdk.identifier.entity.ContractHash;
import com.casper.sdk.identifier.era.IdEraIdentifier;
import com.casper.sdk.identifier.global.StateRootHashIdentifier;
import com.casper.sdk.identifier.purse.MainPurseUnderPublickey;
import com.casper.sdk.identifier.purse.PurseIdentifier;
import com.casper.sdk.model.account.PublicKeyIdentifier;
import com.casper.sdk.model.balance.QueryBalanceData;
import com.casper.sdk.model.balance.QueryBalanceDetailsResult;
import com.casper.sdk.model.block.ChainGetBlockResult;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.DelegatorKindAllocation;
import com.casper.sdk.model.deploy.DelegatorKindPublicKey;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.entity.AccountEntity;
import com.casper.sdk.model.entity.StateEntityResult;
import com.casper.sdk.model.era.EraInfoData;
import com.casper.sdk.model.key.Key;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.reward.GetRewardResult;
import com.casper.sdk.model.stateroothash.StateRootHashData;
import com.casper.sdk.model.status.ChainspecData;
import com.casper.sdk.model.status.StatusData;
import com.casper.sdk.model.transaction.*;
import com.casper.sdk.model.transaction.entrypoint.CallEntryPoint;
import com.casper.sdk.model.transaction.entrypoint.TransferEntryPoint;
import com.casper.sdk.model.transaction.execution.ExecutionResultV2;
import com.casper.sdk.model.transaction.field.Fields;
import com.casper.sdk.model.transaction.pricing.PaymentLimited;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.model.transaction.target.VmCasperV1;
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
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author carl@stormeye.co.uk
 */
@SuppressWarnings("NewClassNamingConvention")
@Disabled
public class HowTo {

    final static String receiverAccountPublicKey = "02025d359802a8826fef41efd8a53fbc8226af6d9e98a658a7cce6b5aa0788322095";
    final static String senderAccountPublicKey = "01e5df7f79ac345b279d526616b70d14964aae41c3e58370d001d59f87a32182b0";

    private CasperService casperService;


    @SuppressWarnings("resource")
    @BeforeEach
    public void connect() throws IOException, URISyntaxException {
//        casperService = CasperService.usingPeer("localhost", 21101);

        final String authTokenPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/auth.token")).toURI()).toString();
        final String authToken = Files.lines(Paths.get(authTokenPath), StandardCharsets.UTF_8)
                .collect(Collectors.toList()).get(0);

        casperService = CasperService.usingPeer(new URL("https://node.testnet.cspr.cloud/rpc"),
                new HashMap<String, String>() {{
                    put("Authorization", authToken);
                }});
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
        final PublicKey delegator = ((DelegatorKindPublicKey) ((DelegatorKindAllocation) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorKind()).getPublicKey();
        final PublicKey validator = ((DelegatorKindAllocation) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getValidatorPublicKey();

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
        final PublicKey delegator = ((DelegatorKindPublicKey) ((DelegatorKindAllocation) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorKind()).getPublicKey();
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
        final PublicKey delegator = ((DelegatorKindPublicKey) ((DelegatorKindAllocation) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getDelegatorKind()).getPublicKey();
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
        final PublicKey validator = ((DelegatorKindAllocation) eraSummaryBlockHash.getEraSummary().getStoredValue().getValue().getSeigniorageAllocations().get(0)).getValidatorPublicKey();

        final StateEntityResult stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(
                receiverAccountPublicKey), null);

        final URef mainPurse = ((AccountEntity) stateEntity.getEntity()).getMainPurse();
        assert mainPurse != null;

    }

    @Test
        /* TODO */
    void getDictionaryItem() {
        // Now linked to issue #368
        // Need to first install a Contract with a built in Dictionary
        // Then query it with state_get_dictionary_item
    }


    @Test
    void getStateEntity() {

        final PublicKeyIdentifier publicKeyEntityIdentifier = new PublicKeyIdentifier(senderAccountPublicKey);

        //By public key
        final StateEntityResult stateEntityPublicKey = casperService.getStateEntity(publicKeyEntityIdentifier, null);
        assert stateEntityPublicKey.getEntity() != null;

        //By contract identifier
        final Key contractKey = ((AccountEntity) stateEntityPublicKey.getEntity()).getNamedKeys().get(0).getKey();

//        final StateEntityResult stateEntityContract = casperService.getStateEntity(new EntityAddrIdentifier(contractKey.toString()), null);
        final StateEntityResult stateEntityContract = casperService.getStateEntity(new ContractHash(contractKey.toString()), null);
//        final StateEntityResult stateEntityAccount = casperService.getStateEntity(new EntityAddrIdentifier(((Account) stateEntityPublicKey.getEntity()).getHash().toString()), null);

//        assert stateEntityContract.getEntity() != null;
//        assert stateEntityAccount.getEntity() != null;

    }


    @Test
    void putTransactionNative()
            throws IOException, ValueSerializationException, TimeoutException, URISyntaxException, NoSuchAlgorithmException, DynamicInstanceException {

        //Get the senders private key
        //Add your own private key file here
        final String secretKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/secret_key.pem")).toURI()).toString();
        final Ed25519PrivateKey senderPrivateKey = new Ed25519PrivateKey();
        senderPrivateKey.readPrivateKey(secretKeyPath);

        //Derive the senders public key from the private key
        final AbstractPublicKey senderAbstractPublicKey = senderPrivateKey.derivePublicKey();
        assertThat(senderAbstractPublicKey, is(notNullValue()));
        final PublicKey senderPublicKey = PublicKey.fromAbstractPublicKey(senderAbstractPublicKey);

        //Public key
        final PublicKey receiverPublicKey = PublicKey.fromTaggedHexString(receiverAccountPublicKey);

        //Get the receivers purse
        final StateEntityResult stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(receiverPublicKey), null);
        assertThat(stateEntity, is(notNullValue()));
        final URef receiversPurse = ((AccountEntity) stateEntity.getEntity()).getMainPurse();

        //Add the required arguments
        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("target", new CLValueURef(receiversPurse)),
                new NamedArg<>("amount", new CLValueU512(BigInteger.valueOf(2500000000L))), //2.5 CSPR
                new NamedArg<>("id", new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(System.currentTimeMillis())))))
        );
        //Build the transaction header
        final TransactionV1Payload payload = TransactionV1Payload.builder()
                .chainName(casperService.getStatus().getChainSpecName())
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new PaymentLimited(3, BigInteger.valueOf(2500000000L), true))
                .initiatorAddr(new InitiatorPublicKey(senderPublicKey))
                .fields(Fields.builder()
                        .args(new NamedArgs(args))
                        .target(new Native())
                        .entryPoint(new TransferEntryPoint())
                        .scheduling(new Standard())
                        .build()
                )
                .build();

        //Build the transaction
        final TransactionV1 transactionV1 = TransactionV1.builder()
                .payload(payload)
                .build();

        //Sign the transaction
        final Transaction transaction = new Transaction(transactionV1.sign(senderPrivateKey));

        //Deploy the transaction
        final PutTransactionResult result = casperService.putTransaction(transaction);

        assertThat(result, is(notNullValue()));
        assertThat(result.getTransactionHash(), is(transaction.get().getHash()));

        final GetTransactionResult transactionResult = waitForTransaction(result.getTransactionHash());
        assertThat(transactionResult, is(notNullValue()));

        //TODO Execution result is null
//        assertThat(transactionResult.getExecutionInfo().getExecutionResult(), is(notNullValue()));

    }

    @Test
    void putTransactionContractHelloWorld() throws IOException, URISyntaxException, ValueSerializationException, TimeoutException {

        //Get the senders private key
        //Add your own private key here
//        final String secretKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/secret_key-cctl-faucet.pem")).toURI()).toString();
        final String secretKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/secret_key-testnet.pem")).toURI()).toString();
        final Ed25519PrivateKey senderPrivateKey = new Ed25519PrivateKey();
        senderPrivateKey.readPrivateKey(secretKeyPath);

        final String wasmPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/contracts/contract-hello-world.wasm")).toURI()).toString();
        final URL wasmUrl = new URL("file://" + wasmPath);
        final byte[] wasmBytes = IOUtils.readBytesFromStream(wasmUrl.openStream());


        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("message", new CLValueString("Hello from Stormeye")),
                new NamedArg<>("key-name", new CLValueString("key-storm-001")),
                new NamedArg<>("id", new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(System.currentTimeMillis())))))
        );
        final TransactionV1Payload payload = TransactionV1Payload.builder()
                .chainName(casperService.getStatus().getChainSpecName())
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new PaymentLimited(1, new BigInteger("5000000000"), true))
                .initiatorAddr(new InitiatorPublicKey(PublicKey.fromAbstractPublicKey(senderPrivateKey.derivePublicKey())))
                .fields(Fields.builder()
                        .args(new NamedArgs(args))
                        .scheduling(new Standard())
                        .target(new Session(false, new VmCasperV1(), wasmBytes))
                        .entryPoint(new CallEntryPoint()).build()
                )
                .build();

        final TransactionV1 transactionV1 = TransactionV1.builder()
                .payload(payload)
                .build();

        final Transaction transaction = new Transaction(transactionV1.sign(senderPrivateKey));

        final PutTransactionResult result = casperService.putTransaction(transaction);

        assert result != null;
        assert result.getTransactionHash() != null;

        final GetTransactionResult transactionResult = waitForTransaction(result.getTransactionHash());

        assertThat(transactionResult, is(notNullValue()));
        assertThat(((ExecutionResultV2) transactionResult.getExecutionInfo().getExecutionResult()).getErrorMessage(), is(nullValue()));

    }

    @Test
    void putTransactionContractCep18() throws IOException, URISyntaxException, ValueSerializationException, TimeoutException {

        //Get the senders private key
        //Add your own private key here
        final String secretKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/secret_key-cctl-faucet.pem")).toURI()).toString();
//        final String secretKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/secret_key-testnet.pem")).toURI()).toString();
        final Ed25519PrivateKey senderPrivateKey = new Ed25519PrivateKey();
        senderPrivateKey.readPrivateKey(secretKeyPath);

        final String wasmPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/contracts/cep18.wasm")).toURI()).toString();
        final URL wasmUrl = new URL("file://" + wasmPath);
        final byte[] wasmBytes = IOUtils.readBytesFromStream(wasmUrl.openStream());


        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("decimals", new CLValueU8((byte) 9)),
                new NamedArg<>("name", new CLValueString("Stormeye Token")),
                new NamedArg<>("symbol", new CLValueString("STRM")),
                new NamedArg<>("total_supply", new CLValueU256(BigInteger.valueOf(20000000000L))),
                new NamedArg<>("events_mode", new CLValueU8((byte) 0)),
                new NamedArg<>("id", new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(System.currentTimeMillis())))))
        );
        final TransactionV1Payload payload = TransactionV1Payload.builder()
                .chainName(casperService.getStatus().getChainSpecName())
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new PaymentLimited(1, new BigInteger("350000000000"), true))
                .initiatorAddr(new InitiatorPublicKey(PublicKey.fromAbstractPublicKey(senderPrivateKey.derivePublicKey())))
                .fields(Fields.builder()
                        .args(new NamedArgs(args))
                        .scheduling(new Standard())
                        .target(new Session(true, new VmCasperV1(), wasmBytes))
                        .entryPoint(new CallEntryPoint()).build()
                )
                .build();

        final TransactionV1 transactionV1 = TransactionV1.builder()
                .payload(payload)
                .build();

        final Transaction transaction = new Transaction(transactionV1.sign(senderPrivateKey));

        final PutTransactionResult result = casperService.putTransaction(transaction);

        assert result != null;
        assert result.getTransactionHash() != null;

        final GetTransactionResult transactionResult = waitForTransaction(result.getTransactionHash());

        assertThat(transactionResult, is(notNullValue()));
        assertThat(((ExecutionResultV2) transactionResult.getExecutionInfo().getExecutionResult()).getErrorMessage(), is(nullValue()));

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
