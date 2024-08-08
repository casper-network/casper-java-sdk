package com.casper.sdk.service;

import com.casper.sdk.model.account.PublicKeyIdentifier;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.contract.Contract;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.casper.sdk.model.entity.AddressableEntity;
import com.casper.sdk.model.entity.StateEntityResult;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.*;
import com.casper.sdk.model.transaction.entrypoint.CallEntryPoint;
import com.casper.sdk.model.transaction.entrypoint.TransferEntryPoint;
import com.casper.sdk.model.transaction.pricing.ClassicPricingMode;
import com.casper.sdk.model.transaction.pricing.FixedPricingMode;
import com.casper.sdk.model.transaction.pricing.ReservedPricingMode;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.model.transaction.target.TransactionRuntime;
import com.casper.sdk.model.uref.URef;
import com.syntifi.crypto.key.AbstractPrivateKey;
import com.syntifi.crypto.key.AbstractPublicKey;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.apache.cxf.helpers.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static com.casper.sdk.helper.CasperDeployHelper.getPaymentModuleBytes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Integration tests for the Transactions against a cctl node
 *
 * @author ian@meywood.com
 */
//@Disabled
public class TransactionTests {

    @Test
    void chainPutTransactionNativeTransfer() throws IOException, ValueSerializationException {

        final CasperService casperService = CasperService.usingPeer(new URL("http://localhost:21101/rpc"), null);

        final String faucetSecretKeyPath = "/Users/carl/Documents/Workspace/Casper/casper-network/casper-java-sdk/assets/net-1/faucet/secret_key.pem";
        final Ed25519PrivateKey faucetPrivateKey = new Ed25519PrivateKey();
        faucetPrivateKey.readPrivateKey(faucetSecretKeyPath);

        AbstractPublicKey faucetDerivedPublicKey = faucetPrivateKey.derivePublicKey();
        assertThat(faucetDerivedPublicKey, is(notNullValue()));
        final PublicKey faucetPublicKey = PublicKey.fromAbstractPublicKey(faucetDerivedPublicKey);
        StateEntityResult stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(faucetPublicKey), null);
        assertThat(stateEntity, is(notNullValue()));
        final URef faucetPurse = ((AddressableEntity) stateEntity.getEntity()).getEntity().getMainPurse();

        final String userOneSecretKeyPath = "/Users/carl/Documents/Workspace/Casper/casper-network/casper-java-sdk/assets/net-1/user-1/secret_key.pem";
        final Ed25519PrivateKey userOnePrivateKey = new Ed25519PrivateKey();
        userOnePrivateKey.readPrivateKey(userOneSecretKeyPath);
        final AbstractPublicKey userOnePublicKey = userOnePrivateKey.derivePublicKey();
        assertThat(userOnePublicKey, is(notNullValue()));
        stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(PublicKey.fromAbstractPublicKey(userOnePublicKey)), null);
        assertThat(stateEntity, is(notNullValue()));
        final URef userOnePurse = ((AddressableEntity) stateEntity.getEntity()).getEntity().getMainPurse();

        final TransactionV1Header header = TransactionV1Header.builder()
                .chainName("cspr-dev-cctl")
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new FixedPricingMode(1))
                .initiatorAddr(new InitiatorPublicKey(faucetPublicKey))
                .build();


        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("source", new CLValueOption(Optional.of(new CLValueURef(faucetPurse)))),
                new NamedArg<>("target", new CLValueURef(userOnePurse)),
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



        final Transaction transaction = new Transaction(transactionV1.sign(faucetPrivateKey));

        final PutTransactionResult result = casperService.putTransaction(transaction);

        assertThat(result, is(notNullValue()));
        assertThat(result.getTransactionHash(), is(transaction.get().getHash()));
    }

    @Test
    void chainPutContractCep18() throws IOException, ValueSerializationException, URISyntaxException {

        final CasperService casperService = CasperService.usingPeer(new URL("http://localhost:21101/rpc"), null);

        final AbstractPrivateKey senderPrivKey = new Ed25519PrivateKey();
        final URL url = Objects.requireNonNull(TransactionTests.class.getResource("/net-1/faucet/secret_key.pem"), "missing resource ");
        senderPrivKey.readPrivateKey(url.getFile());

        final String wasmPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/contracts/cep18.wasm")).toURI()).toString();
        final URL wasmUrl = new URL("file://" + wasmPath);
        final byte[] wasmBytes = IOUtils.readBytesFromStream(wasmUrl.openStream());

        final TransactionV1Header header = TransactionV1Header.builder()
                .chainName("cspr-dev-cctl")
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new FixedPricingMode(8))
                .initiatorAddr(new InitiatorPublicKey(PublicKey.fromAbstractPublicKey(senderPrivKey.derivePublicKey())))
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


        final Transaction transaction = new Transaction(transactionV1.sign(senderPrivKey));

        final PutTransactionResult result = casperService.putTransaction(transaction);

        assert result != null;
        assert result.getTransactionHash() != null;

    }


}