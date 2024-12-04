package com.casper.sdk.service;

import com.casper.sdk.model.account.PublicKeyIdentifier;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.entity.AddressableEntity;
import com.casper.sdk.model.entity.StateEntityResult;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.*;
import com.casper.sdk.model.transaction.entrypoint.CallEntryPoint;
import com.casper.sdk.model.transaction.entrypoint.TransferEntryPoint;
import com.casper.sdk.model.transaction.execution.ExecutionResultV2;
import com.casper.sdk.model.transaction.pricing.FixedPricingMode;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.model.transaction.target.TransactionRuntime;
import com.casper.sdk.model.uref.URef;
import com.syntifi.crypto.key.AbstractPrivateKey;
import com.syntifi.crypto.key.AbstractPublicKey;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.apache.cxf.helpers.IOUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Integration tests for the Transactions against a cctl node
 *
 * @author ian@meywood.com
 */
@Disabled
public class TransactionTests {

    @Test
    void chainPutTransactionNativeTransfer() throws IOException, ValueSerializationException, TimeoutException {

        final CasperService casperService = CasperService.usingPeer(new URL("http://localhost:21101/rpc"), null);

        final AbstractPrivateKey faucetPrivateKey = new Ed25519PrivateKey();
        final URL faucetUrl = Objects.requireNonNull(TransactionTests.class.getResource("/net-1/faucet/secret_key.pem"), "missing resource ");
        faucetPrivateKey.readPrivateKey(faucetUrl.getFile());

        AbstractPublicKey faucetDerivedPublicKey = faucetPrivateKey.derivePublicKey();
        assertThat(faucetDerivedPublicKey, is(notNullValue()));
        final PublicKey faucetPublicKey = PublicKey.fromAbstractPublicKey(faucetDerivedPublicKey);
        StateEntityResult stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(faucetPublicKey), null);
        assertThat(stateEntity, is(notNullValue()));
        final URef faucetPurse = ((AddressableEntity) stateEntity.getEntity()).getEntity().getMainPurse();

        final AbstractPrivateKey userOnePrivateKey = new Ed25519PrivateKey();
        final URL user1Url = Objects.requireNonNull(TransactionTests.class.getResource("/net-1/user-1/secret_key.pem"), "missing resource ");
        userOnePrivateKey.readPrivateKey(user1Url.getFile());

        final AbstractPublicKey userOnePublicKey = userOnePrivateKey.derivePublicKey();
        assertThat(userOnePublicKey, is(notNullValue()));
        stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(PublicKey.fromAbstractPublicKey(userOnePublicKey)), null);
        assertThat(stateEntity, is(notNullValue()));
        final URef userOnePurse = ((AddressableEntity) stateEntity.getEntity()).getEntity().getMainPurse();

        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("source", new CLValueOption(Optional.of(new CLValueURef(faucetPurse)))),
                new NamedArg<>("target", new CLValueURef(userOnePurse)),
                new NamedArg<>("amount", new CLValueU512(new BigInteger("2500000000"))),
                new NamedArg<>("id", new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(System.currentTimeMillis())))))
        );

        final TransactionV1Payload payload = TransactionV1Payload.builder()
                .chainName("cspr-dev-cctl")
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new FixedPricingMode(1))
                .initiatorAddr(new InitiatorPublicKey(faucetPublicKey))
                .args(args)
                .target(new Native())
                .entryPoint(new TransferEntryPoint())
                .scheduling(new Standard())
                //  .transactionCategory(TransactionCategory.MINT)
                .build();

        final TransactionV1 transactionV1 = TransactionV1.builder()
                .payload(payload)
                .build();

        final Transaction transaction = new Transaction(transactionV1.sign(faucetPrivateKey));

        final PutTransactionResult result = casperService.putTransaction(transaction);

        assertThat(result, is(notNullValue()));
        assertThat(result.getTransactionHash(), is(transaction.get().getHash()));

        final GetTransactionResult transactionResult = waitForTransaction(result.getTransactionHash(), casperService);

        assertThat(transactionResult, is(notNullValue()));
        assertThat(((ExecutionResultV2) transactionResult.getExecutionInfo().getExecutionResult()).getErrorMessage(), is(nullValue()));

    }

    @Test
    void chainPutContractCep18() throws IOException, ValueSerializationException, URISyntaxException, TimeoutException {

        final CasperService casperService = CasperService.usingPeer(new URL("http://localhost:21101/rpc"), null);

        final AbstractPrivateKey senderPrivKey = new Ed25519PrivateKey();
        final URL url = Objects.requireNonNull(TransactionTests.class.getResource("/net-1/faucet/secret_key.pem"), "missing resource ");
        senderPrivKey.readPrivateKey(url.getFile());

        final String wasmPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/contracts/cep18.wasm")).toURI()).toString();
        final URL wasmUrl = new URL("file://" + wasmPath);
        final byte[] wasmBytes = IOUtils.readBytesFromStream(wasmUrl.openStream());

        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("decimals", new CLValueU8((byte) 11)),
                new NamedArg<>("name", new CLValueString("Acme Token")),
                new NamedArg<>("symbol", new CLValueString("ACME")),
                new NamedArg<>("total_supply", new CLValueU256(BigInteger.valueOf(500000))),
                new NamedArg<>("events_mode", new CLValueU8((byte) 0)),
                new NamedArg<>("id", new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(System.currentTimeMillis())))))
        );

        final TransactionV1Payload payload = TransactionV1Payload.builder()
                .chainName("cspr-dev-cctl")
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new FixedPricingMode(8))
                .initiatorAddr(new InitiatorPublicKey(PublicKey.fromAbstractPublicKey(senderPrivKey.derivePublicKey())))
                .args(args)
                .target(new Session(wasmBytes, TransactionRuntime.VM_CASPER_V2))
                .entryPoint(new CallEntryPoint())
                .scheduling(new Standard())
                //    .transactionCategory(TransactionCategory.INSTALL_UPGRADE)
                .build();

        final TransactionV1 transactionV1 = TransactionV1.builder()
                .payload(payload)
                .build();


        final Transaction transaction = new Transaction(transactionV1.sign(senderPrivKey));

        final PutTransactionResult result = casperService.putTransaction(transaction);

        assert result != null;
        assert result.getTransactionHash() != null;

        final GetTransactionResult transactionResult = waitForTransaction(result.getTransactionHash(), casperService);

        assertThat(transactionResult, is(notNullValue()));
        assertThat(((ExecutionResultV2) transactionResult.getExecutionInfo().getExecutionResult()).getErrorMessage(), is(nullValue()));

        //Tests for the returned getTransaction Entities/Kinds/Entries are in EffectsTest and CasperServiceTests
    }

    private GetTransactionResult waitForTransaction(final TransactionHash hash, final CasperService casperService) throws TimeoutException {

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
