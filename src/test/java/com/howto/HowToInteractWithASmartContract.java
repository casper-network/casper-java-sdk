package com.howto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

import com.casper.sdk.identifier.entity.ContractHash;
import com.casper.sdk.model.account.PublicKeyIdentifier;
import com.casper.sdk.model.clvalue.CLValueOption;
import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.clvalue.CLValueU256;
import com.casper.sdk.model.clvalue.CLValueU64;
import com.casper.sdk.model.clvalue.CLValueU8;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.contract.NamedKey;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.entity.AccountEntity;
import com.casper.sdk.model.entity.ContractEntity;
import com.casper.sdk.model.entity.StateEntityResult;
import com.casper.sdk.model.globalstate.GlobalStateData;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.GetTransactionResult;
import com.casper.sdk.model.transaction.InitiatorPublicKey;
import com.casper.sdk.model.transaction.NamedArgs;
import com.casper.sdk.model.transaction.PutTransactionResult;
import com.casper.sdk.model.transaction.TransactionV1;
import com.casper.sdk.model.transaction.TransactionV1Payload;
import com.casper.sdk.model.transaction.entrypoint.CustomEntryPoint;
import com.casper.sdk.model.transaction.entrypoint.TransferEntryPoint;
import com.casper.sdk.model.transaction.execution.ExecutionResultV2;
import com.casper.sdk.model.transaction.field.Fields;
import com.casper.sdk.model.transaction.pricing.PaymentLimited;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.ByPackageHash;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.Stored;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.model.transaction.target.VmCasperV1;
import com.casper.sdk.model.transaction.target.VmCasperV2;
import com.casper.sdk.service.CasperService;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.util.ByteUtils;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;
import org.apache.cxf.helpers.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HowToInteractWithASmartContract extends HowToMethods {

    private CasperService casperService;

    @BeforeEach
    public void init() throws IOException, URISyntaxException {
        casperService = connect();
    }

    @Test
    void QueryTheContractPackage() throws IOException, URISyntaxException {

        final String contractHash = getContractHash();

        assert contractHash != null;

    }

    @Test
    void TransferCEP18Tokens()
        throws IOException, ValueSerializationException, TimeoutException, URISyntaxException, NoSuchAlgorithmException {

            final PublicKey receiver = PublicKey.fromTaggedHexString("02025d359802a8826fef41efd8a53fbc8226af6d9e98a658a7cce6b5aa0788322095");

            //Get the senders private key
            //Add your own private key here
            final String secretKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/secret_key-testnet.pem")).toURI()).toString();
            final Ed25519PrivateKey senderPrivateKey = new Ed25519PrivateKey();
            senderPrivateKey.readPrivateKey(secretKeyPath);

            final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("recipient", new CLValuePublicKey(PublicKey.fromAbstractPublicKey(receiver.getPubKey()))),
                new NamedArg<>("amount", new CLValueU256(BigInteger.valueOf(10)))
            );
            final TransactionV1Payload payload = TransactionV1Payload.builder()
                .chainName(casperService.getStatus().getChainSpecName())
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new PaymentLimited(1, new BigInteger("2500000000"), true))
                .initiatorAddr(new InitiatorPublicKey(PublicKey.fromAbstractPublicKey(senderPrivateKey.derivePublicKey())))
                .fields(Fields.builder()
                    .args(new NamedArgs(args))
                    .scheduling(new Standard())
                    .target(new Stored(new ByPackageHash(new Digest(getContractHash()), null), new VmCasperV1()))
                    .entryPoint(new CustomEntryPoint("Transfer"))
                    .build()
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

    private String getContractHash() throws IOException, URISyntaxException {

        //Identifier here is the installed CEP18 contract from HowToInstallASmartContract
        final StateEntityResult stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(
            "01e5df7f79ac345b279d526616b70d14964aae41c3e58370d001d59f87a32182b0"), null);

        final NamedKey namedKey = ((AccountEntity) stateEntity.getEntity()).getNamedKeys().get(0);

        final Optional<NamedKey> contractPackageHash = ((AccountEntity) stateEntity.getEntity()).getNamedKeys()
            .stream().filter(f -> f.getName().contains("contract_package")).findFirst();

        assert contractPackageHash.isPresent();

        //Need a helper method in AbstractSerializedKeyTaggedHex
        return ByteUtils.encodeHexString(contractPackageHash.get().getKey().getKey());

    }


}
