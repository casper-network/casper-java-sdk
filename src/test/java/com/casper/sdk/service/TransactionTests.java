package com.casper.sdk.service;

import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.key.PublicKey;
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
import com.casper.sdk.model.transaction.target.TransactionRuntime;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.AbstractPrivateKey;
import com.syntifi.crypto.key.AbstractPublicKey;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.apache.cxf.helpers.IOUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
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
//@Disabled
public class TransactionTests {


    @Test
    void chainPutTransactionNativeTransfer() throws IOException, ValueSerializationException, TimeoutException {

        final CasperService casperService = CasperService.usingPeer(new URL("http://localhost:21101/rpc"), null);

        final AbstractPrivateKey faucetPrivateKey = new Ed25519PrivateKey();
        final URL faucetUrl = Objects.requireNonNull(TransactionTests.class.getResource("/net-1/faucet/secret_key.pem"), "missing resource ");
        faucetPrivateKey.readPrivateKey(faucetUrl.getFile());

        final AbstractPublicKey faucetDerivedPublicKey = faucetPrivateKey.derivePublicKey();
        assertThat(faucetDerivedPublicKey, is(notNullValue()));
        final PublicKey faucetPublicKey = PublicKey.fromAbstractPublicKey(faucetDerivedPublicKey);
        // StateEntityResult stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(faucetPublicKey), null);
        // assertThat(stateEntity, is(notNullValue()));
        //  final URef faucetPurse = ((AddressableEntity) stateEntity.getEntity()).getEntity().getMainPurse();

        final AbstractPrivateKey userOnePrivateKey = new Ed25519PrivateKey();
        final URL user1Url = Objects.requireNonNull(TransactionTests.class.getResource("/net-1/user-1/secret_key.pem"), "missing resource ");
        userOnePrivateKey.readPrivateKey(user1Url.getFile());

        final PublicKey userOnePublicKey = PublicKey.fromAbstractPublicKey(userOnePrivateKey.derivePublicKey());
        assertThat(userOnePublicKey, is(notNullValue()));
        //  stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(PublicKey.fromAbstractPublicKey(userOnePublicKey)), null);
        //  assertThat(stateEntity, is(notNullValue()));
        //   final URef userOnePurse = ((AddressableEntity) stateEntity.getEntity()).getEntity().getMainPurse();

        final List<NamedArg<?>> args = Arrays.asList(
                //  new NamedArg<>("source", new CLValueOption(Optional.of(new CLValueURef(faucetPurse)))),
                new NamedArg<>("target", new CLValuePublicKey(userOnePublicKey)),
                new NamedArg<>("amount", new CLValueU512(new BigInteger("2500000000"))),
                new NamedArg<>("id", new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(System.currentTimeMillis())))))
        );

        final TransactionV1Payload payload = TransactionV1Payload.builder()
                .chainName("cspr-dev-cctl")
                .timestamp(new Date())
                .ttl(Ttl.builder().ttl("30m").build())
                .pricingMode(new PaymentLimited(1, new BigInteger("3000000000"), true))
                .initiatorAddr(new InitiatorPublicKey(faucetPublicKey))
                .fields(Fields.builder()
                        .args(new NamedArgs(args))
                        .scheduling(new Standard())
                        .target(new Native())
                        .entryPoint(new TransferEntryPoint()).build()
                )
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

        final InputStream in = Objects.requireNonNull(getClass().getClassLoader().getResource("wasm/cep18-rc3.wasm")).openStream();
        final byte[] wasmBytes = IOUtils.readBytesFromStream(in);
        //final byte[] wasmBytes = Hex.decode("01020304");

      /*  args.AddArgument("name", * clvalue.NewCLString("Test")).
        AddArgument("symbol", * clvalue.NewCLString("test")).
        AddArgument("decimals", * clvalue.NewCLUint8(9)).
        AddArgument("total_supply", * clvalue.NewCLUInt256(big.NewInt(1_000_000_000_000_000))).
        AddArgument("events_mode", * clvalue.NewCLUint8(2)).
        AddArgument("enable_mint_burn", * clvalue.NewCLUint8(0))*/

        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("name", new CLValueString("Test")),
                new NamedArg<>("symbol", new CLValueString("test")),
                new NamedArg<>("decimals", new CLValueU8((byte) 9)),
                new NamedArg<>("total_supply", new CLValueU256(new BigInteger("1000000000000000"))),
                new NamedArg<>("events_mode", new CLValueU8((byte) 2)),
                new NamedArg<>("enable_mint_burn", new CLValueU8((byte) 0))
        );

        final TransactionV1Payload payload = TransactionV1Payload.builder()
                .initiatorAddr(new InitiatorPublicKey(PublicKey.fromAbstractPublicKey(senderPrivKey.derivePublicKey())))
                .chainName("cspr-dev-cctl")
                // .timestamp(new DateTime("2025-06-24T17:31:29.209Z").toDate())
                .timestamp(new Date())
                .ttl(Ttl.builder().ttl("30m").build())
                //.pricingMode(new FixedPricingMode(0, 8))
                .pricingMode(new PaymentLimited(1, new BigInteger("5000000000"), true))
                .fields(Fields.builder().args(new NamedArgs(args))
                        .target(new Session(TransactionRuntime.VM_CASPER_V1, true, wasmBytes))
                        .entryPoint(new CallEntryPoint())
                        .scheduling(new Standard()).build()
                )
                .build();

        final TransactionV1 transactionV1 = TransactionV1.builder()
                .payload(payload)
                .build();

        final Transaction transaction = new Transaction(transactionV1.sign(senderPrivKey));

        String json = new ObjectMapper().writeValueAsString(transaction);
        final PutTransactionResult result = casperService.putTransaction(transaction);

        assert result != null;
        assert result.getTransactionHash() != null;

        final GetTransactionResult transactionResult = waitForTransaction(result.getTransactionHash(), casperService);

        assertThat(transactionResult, is(notNullValue()));
    //    assertThat(((ExecutionResultV2) transactionResult.getExecutionInfo().getExecutionResult()).getErrorMessage(), is(nullValue()));

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
