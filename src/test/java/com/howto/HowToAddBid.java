package com.howto;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.CLValueU8;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.GetTransactionResult;
import com.casper.sdk.model.transaction.InitiatorPublicKey;
import com.casper.sdk.model.transaction.NamedArgs;
import com.casper.sdk.model.transaction.PutTransactionResult;
import com.casper.sdk.model.transaction.TransactionV1;
import com.casper.sdk.model.transaction.TransactionV1Payload;
import com.casper.sdk.model.transaction.entrypoint.AddBidEntryPoint;
import com.casper.sdk.model.transaction.execution.ExecutionResultV2;
import com.casper.sdk.model.transaction.field.Fields;
import com.casper.sdk.model.transaction.pricing.PaymentLimited;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.service.CasperService;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class HowToAddBid extends HowToMethods {

    private CasperService casperService;

    @BeforeEach
    public void init() throws IOException, URISyntaxException {
        casperService = connect();
    }

    @Test
    void addBid()
        throws ValueSerializationException, TimeoutException, NoSuchAlgorithmException, IOException, URISyntaxException {

        final PublicKey delegator = PublicKey.fromTaggedHexString("013f69f01439509c0fd113176bcaec9170d18b70fb2bbbdcc9b28803f8bbcc429c");

        final String secretKeyPath = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("howto/keys/delegator.pem")).toURI()).toString();
        final Ed25519PrivateKey delegatorPrivateKey = new Ed25519PrivateKey();
        delegatorPrivateKey.readPrivateKey(secretKeyPath);

        final List<NamedArg<?>> args = Arrays.asList(
            new NamedArg<>("public_key", new CLValuePublicKey(PublicKey.fromAbstractPublicKey(delegator.getPubKey()))),
            new NamedArg<>("delegation_rate", new CLValueU8(Integer.valueOf(20).byteValue())),
            new NamedArg<>("amount", new CLValueU512(new BigInteger("10000000000000")))
        );
        final TransactionV1Payload payload = TransactionV1Payload.builder()
            .chainName(casperService.getStatus().getChainSpecName())
            .ttl(Ttl.builder().ttl("30m").build())
            .pricingMode(new PaymentLimited(1, new BigInteger("2500000000"), true))
            .initiatorAddr(new InitiatorPublicKey(PublicKey.fromAbstractPublicKey(delegator.getPubKey())))
            .fields(Fields.builder()
                .args(new NamedArgs(args))
                .scheduling(new Standard())
                .target(new Native())
                .entryPoint(new AddBidEntryPoint()).build()
            )
            .build();

        final TransactionV1 transactionV1 = TransactionV1.builder()
            .payload(payload)
            .build();

        final Transaction transaction = new Transaction(transactionV1.sign(delegatorPrivateKey));

        final PutTransactionResult result = casperService.putTransaction(transaction);

        assert result != null;
        assert result.getTransactionHash() != null;

        final GetTransactionResult transactionResult = waitForTransaction(result.getTransactionHash());

        assertThat(transactionResult, is(notNullValue()));
        assertThat(((ExecutionResultV2) transactionResult.getExecutionInfo().getExecutionResult()).getErrorMessage(), is(nullValue()));

    }

}
