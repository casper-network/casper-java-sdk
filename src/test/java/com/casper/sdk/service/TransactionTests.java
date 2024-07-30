package com.casper.sdk.service;

import com.casper.sdk.model.account.PublicKeyIdentifier;
import com.casper.sdk.model.clvalue.CLValueOption;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.CLValueU64;
import com.casper.sdk.model.clvalue.CLValueURef;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.entity.AddressableEntity;
import com.casper.sdk.model.entity.StateEntityResult;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.transaction.*;
import com.casper.sdk.model.transaction.entrypoint.TransferEntryPoint;
import com.casper.sdk.model.transaction.pricing.FixedPricingMode;
import com.casper.sdk.model.transaction.scheduling.Standard;
import com.casper.sdk.model.transaction.target.Native;
import com.casper.sdk.model.transaction.target.Transaction;
import com.casper.sdk.model.uref.URef;
import com.syntifi.crypto.key.AbstractPublicKey;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Integration tests for the Transactions against a node
 *
 * @author ian@meywood.com
 */
public class TransactionTests {


    @Test
    void chainPutTransactionNativeTransfer() throws IOException, ValueSerializationException {

        final CasperService casperService = CasperService.usingPeer(new URL("http://localhost:21101/rpc"), null);

        final String faucetSecretKeyPath = "/Users/imills/casper/casper-java-sdk/assets/net-1/faucet/secret_key.pem";
        final Ed25519PrivateKey faucetPrivateKey = new Ed25519PrivateKey();
        faucetPrivateKey.readPrivateKey(faucetSecretKeyPath);
        AbstractPublicKey faucetDerivedPublicKey = faucetPrivateKey.derivePublicKey();
        assertThat(faucetDerivedPublicKey, is(notNullValue()));
        final PublicKey faucetPublicKey = PublicKey.fromAbstractPublicKey(faucetDerivedPublicKey);
        StateEntityResult stateEntity = casperService.getStateEntity(new PublicKeyIdentifier(faucetPublicKey), null);
        assertThat(stateEntity, is(notNullValue()));
        final URef faucetPurse = ((AddressableEntity) stateEntity.getEntity()).getEntity().getMainPurse();

        final String userOneSecretKeyPath = "/Users/imills/casper/casper-java-sdk/assets/net-1/user-1/secret_key.pem";
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
                .pricingMode(new FixedPricingMode(8))
                .initiatorAddr(new InitiatorPublicKey(faucetPublicKey))
                .build();


        final List<NamedArg<?>> args = Arrays.asList(
                new NamedArg<>("source", new CLValueOption(Optional.of(new CLValueURef(faucetPurse)))),
                new NamedArg<>("target", new CLValueURef(userOnePurse)),
                new NamedArg<>("amount", new CLValueU512(new BigInteger("2500000000"))),
                new NamedArg<>("id", new CLValueU64(BigInteger.valueOf(System.currentTimeMillis())))
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
    }
}
