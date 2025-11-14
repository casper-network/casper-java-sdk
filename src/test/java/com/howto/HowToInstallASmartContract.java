package com.howto;

import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.*;
import com.casper.sdk.model.transaction.entrypoint.CallEntryPoint;
import com.casper.sdk.model.transaction.execution.ExecutionResultV2;
import com.casper.sdk.model.transaction.field.Fields;
import com.casper.sdk.model.transaction.pricing.PaymentLimited;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Session;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.model.transaction.target.VmCasperV1;
import com.casper.sdk.service.CasperService;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.apache.cxf.helpers.IOUtils;
import org.junit.jupiter.api.BeforeEach;
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

public class HowToInstallASmartContract extends HowToMethods {

    private CasperService casperService;

    @BeforeEach
    public void init() throws IOException, URISyntaxException {
        casperService = connect();
    }

    @Test
    void installSmartContract()
            throws IOException, ValueSerializationException, TimeoutException, URISyntaxException {

        //Get the senders private key
        //Add your own private key here
        final String secretKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/secret_key-testnet.pem")).toURI()).toString();
        final Ed25519PrivateKey senderPrivateKey = new Ed25519PrivateKey();
        senderPrivateKey.readPrivateKey(secretKeyPath);

        //Get the WASM contract
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
}
