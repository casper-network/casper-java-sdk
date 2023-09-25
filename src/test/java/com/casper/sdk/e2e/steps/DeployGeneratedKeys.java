package com.casper.sdk.e2e.steps;

import com.casper.sdk.e2e.utils.AssetUtils;
import com.casper.sdk.e2e.utils.CasperClientProvider;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.helper.CasperKeyHelper;
import com.casper.sdk.helper.CasperTransferHelper;
import com.casper.sdk.identifier.block.HashBlockIdentifier;
import com.casper.sdk.model.block.JsonBlockData;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.event.blockadded.BlockAdded;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.service.CasperService;
import com.casper.sdk.e2e.utils.ContextMap;
import com.syntifi.crypto.key.AbstractPrivateKey;
import com.syntifi.crypto.key.AbstractPublicKey;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import com.syntifi.crypto.key.Secp256k1PrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Step Definitions for Deploys with generated keys
 */
public class DeployGeneratedKeys {

    private final ContextMap contextMap = ContextMap.getInstance();
    private final Logger logger = LoggerFactory.getLogger(DeployGeneratedKeys.class);

    @BeforeAll
    public static void setUp() {
        ContextMap.getInstance().clear();
    }


    @Given("that a {string} sender key is generated")
    public void thatSenderKeyIsGenerated(final String algo) throws IOException, GeneralSecurityException {

        logger.info("that a {} sender key is generated", algo);

        final AbstractPrivateKey sk;
        final AbstractPublicKey pk;

        if (algo.equals("Ed25519")) {
            sk = CasperKeyHelper.createRandomEd25519Key();
            pk = CasperKeyHelper.derivePublicKey((Ed25519PrivateKey) sk);
        } else {
            sk = CasperKeyHelper.createRandomSecp256k1Key();
            pk = CasperKeyHelper.derivePublicKey((Secp256k1PrivateKey) sk);
        }

        assertThat(sk, is(notNullValue()));
        assertThat(pk, is(notNullValue()));

        byte[] msg = "this is the sender".getBytes();
        byte[] signature = sk.sign(msg);
        assertTrue(pk.verify(msg, signature));

        assertThat(sk.getKey(), is(notNullValue()));
        assertThat(pk.getKey(), is(notNullValue()));

        contextMap.put(StepConstants.SENDER_KEY_SK, sk);
        contextMap.put(StepConstants.SENDER_KEY_PK, pk);
    }


    @Given("that a {string} receiver key is generated")
    public void thatAReceiverKeyIsGenerated(final String algo) throws IOException, GeneralSecurityException {

        logger.info("that a {} receiver key is generated", algo);

        final AbstractPublicKey pk;
        final AbstractPrivateKey sk;

        if (algo.equals("Ed25519")) {
            sk = CasperKeyHelper.createRandomEd25519Key();
            pk = CasperKeyHelper.derivePublicKey((Ed25519PrivateKey) sk);
        } else {
            sk = CasperKeyHelper.createRandomSecp256k1Key();
            pk = CasperKeyHelper.derivePublicKey((Secp256k1PrivateKey) sk);
        }

        byte[] msg = "this is the receiver".getBytes();
        byte[] signature = sk.sign(msg);
        assertTrue(pk.verify(msg, signature));

        assertThat(sk.getKey(), is(notNullValue()));
        assertThat(pk.getKey(), is(notNullValue()));

        contextMap.put(StepConstants.RECEIVER_KEY, pk);
    }

    @Then("fund the account from the faucet user with a transfer amount of {long} and a payment amount of {long}")
    public void fundTheAccountFromTheFaucetUserWithATransferAmountOfAndAPaymentAmountOf(long transferAmount, long paymentAmount) throws IOException, NoSuchTypeException, GeneralSecurityException, ValueSerializationException {
        logger.info("fund the account from the faucet user with a transfer amount of {} and a payment amount of {}", transferAmount, paymentAmount);

        final URL faucetPrivateKeyUrl = AssetUtils.getFaucetAsset(1, "secret_key.pem");
        assertThat(faucetPrivateKeyUrl, is(notNullValue()));
        final Ed25519PrivateKey privateKey = new Ed25519PrivateKey();
        privateKey.readPrivateKey(faucetPrivateKeyUrl.getFile());

        contextMap.put(StepConstants.TRANSFER_AMOUNT, transferAmount);
        contextMap.put(StepConstants.PAYMENT_AMOUNT, paymentAmount);

        doDeploy(privateKey, contextMap.get(StepConstants.SENDER_KEY_PK));
    }

    @Then("transfer to the receiver account the transfer amount of {long} and the payment amount of {long}")
    public void transferToTheReceiverAccountTheTransferAmountOfAndThePaymentAmountOf(long transferAmount, long paymentAmount) throws NoSuchTypeException, GeneralSecurityException, ValueSerializationException {
        logger.info("transfer to the receiver account the transfer amount of {} and the payment amount of {}", transferAmount, paymentAmount);

        contextMap.put(StepConstants.TRANSFER_AMOUNT, transferAmount);
        contextMap.put(StepConstants.PAYMENT_AMOUNT, paymentAmount);

        doDeploy(contextMap.get(StepConstants.SENDER_KEY_SK), contextMap.get(StepConstants.RECEIVER_KEY));
    }

    @And("the returned block header proposer contains the {string} algo")
    public void theReturnedBlockHeaderProposerContainsTheAlgo(String algo) {
        logger.info("the returned block header proposer contains the {} algo", algo);

        final Digest matchingBlockHash = ((BlockAdded) contextMap.get(StepConstants.LAST_BLOCK_ADDED)).getBlockHash();
        final JsonBlockData block = CasperClientProvider.getInstance().getCasperService().getBlock(new HashBlockIdentifier(matchingBlockHash.toString()));

        assertThat(block.getBlock().getBody().getProposer().getTag().toString().toUpperCase(), is(algo.toUpperCase(Locale.ROOT)));
    }

    private void doDeploy(final AbstractPrivateKey sk, final AbstractPublicKey pk) throws NoSuchTypeException, GeneralSecurityException, ValueSerializationException {

        final Deploy deploy = CasperTransferHelper.buildTransferDeploy(
                sk,
                PublicKey.fromAbstractPublicKey(pk),
                BigInteger.valueOf(contextMap.get(StepConstants.TRANSFER_AMOUNT)),
                "casper-net-1",
                Math.abs(new Random().nextLong()),
                BigInteger.valueOf(contextMap.get(StepConstants.PAYMENT_AMOUNT)),
                1L,
                Ttl.builder().ttl("30m").build(),
                new Date(),
                new ArrayList<>());

        contextMap.put(StepConstants.PUT_DEPLOY, deploy);

        final CasperService casperService = CasperClientProvider.getInstance().getCasperService();

        contextMap.put(StepConstants.DEPLOY_RESULT, casperService.putDeploy(deploy));
    }
}
