package com.casper.sdk.model.deploy;

import com.casper.sdk.helper.CasperConstants;
import com.casper.sdk.helper.CasperDeployHelper;
import com.casper.sdk.identifier.dictionary.StringDictionaryIdentifier;
import com.casper.sdk.model.account.Account;
import com.casper.sdk.model.clvalue.CLValueByteArray;
import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.clvalue.CLValueU256;
import com.casper.sdk.model.clvalue.CLValueU8;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.contract.NamedKey;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.casper.sdk.model.deploy.executabledeploy.StoredContractByHash;
import com.casper.sdk.model.deploy.executionresult.Success;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.stateroothash.StateRootHashData;
import com.casper.sdk.model.storedvalue.StoredValueAccount;
import com.casper.sdk.model.storedvalue.StoredValueData;
import com.casper.sdk.service.CasperService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import com.syntifi.crypto.key.encdec.Hex;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;

import static com.casper.sdk.helper.CasperDeployHelper.getPaymentModuleBytes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Test that a smart contract can be deployed to a node.
 *
 * @author ian@meywood.com
 */
@Disabled
public class DeployWasmTest {

    private static final String WASM_PATH = "/contracts/erc20.wasm";
    // NOTE for real testing this will need to reference file in ~/casper-node/utils/nctl/assets/net-1/faucet/secret_key.pem
    private static final String FAUCET_SECRET_KEY_PATH = "/contracts/secret_key.pem";

    @Test
    public void deployModuleBytesContainingWasm() throws Exception {

        final URL resource = getClass().getResource(WASM_PATH);
        //noinspection ConstantConditions
        final byte[] bytes = IOUtils.toByteArray(resource.openStream());
        assertThat(bytes.length, is(189336));

        final String chainName = "casper-net-1";
        final BigInteger payment = new BigInteger("200000000000");
        final byte tokenDecimals = 11;
        final String tokenName = "Acme Token";
        final BigInteger tokenTotalSupply = new BigInteger("500000000000");
        final String tokenSymbol = "ACME";

        // Load faucet private key
        final URL faucetPrivateKeyUrl = getClass().getResource(FAUCET_SECRET_KEY_PATH);
        Assertions.assertNotNull(faucetPrivateKeyUrl);
        final Ed25519PrivateKey privateKey = new Ed25519PrivateKey();
        privateKey.readPrivateKey(faucetPrivateKeyUrl.getFile());

        final List<NamedArg<?>> paymentArgs = new LinkedList<>();
//        paymentArgs.add(new NamedArg<>("amount", new CLValueU512(payment)));
        paymentArgs.add(new NamedArg<>("token_decimals", new CLValueU8(tokenDecimals)));
        paymentArgs.add(new NamedArg<>("token_name", new CLValueString(tokenName)));
        paymentArgs.add(new NamedArg<>("token_symbol", new CLValueString(tokenSymbol)));
        paymentArgs.add(new NamedArg<>("token_total_supply", new CLValueU256(tokenTotalSupply)));

        final ModuleBytes session = ModuleBytes.builder().bytes(bytes).args(paymentArgs).build();
        final ModuleBytes paymentModuleBytes = getPaymentModuleBytes(payment);

        final Deploy deploy = CasperDeployHelper.buildDeploy(privateKey,
                chainName,
                session,
                paymentModuleBytes,
                CasperConstants.DEFAULT_GAS_PRICE.value,
                Ttl.builder().ttl("30m").build(),
                new Date(),
                new ArrayList<>()
        );


        final CasperService casperService = CasperService.usingPeer("localhost", 11101);

        String json = new ObjectMapper().writeValueAsString(deploy);
        System.out.println(json);

        final DeployResult deployResult = casperService.putDeploy(deploy);
        assertThat(deployResult, is(notNullValue()));
        assertThat(deployResult.getDeployHash(), is(notNullValue()));

        // Wait for a successful deploy
        final DeployData deployData = waitForDeploy(casperService, deployResult.getDeployHash());
        assertThat(deployData.getExecutionResults().get(0).getResult(), is(instanceOf(Success.class)));

        // Obtain the contract hash from the dictionary
        final String contractHash = getContractHash(casperService, privateKey);

        // Invoke the contract
        invokeContractUsingStoredContractByHash(casperService, contractHash.substring(5), privateKey);
    }

    private String getContractHash(final CasperService casperService,
                                   final Ed25519PrivateKey faucetPrivateKey) throws IOException {

        PublicKey publicKey = PublicKey.fromAbstractPublicKey(faucetPrivateKey.derivePublicKey());
        final String accountHash = publicKey.generateAccountHash(true);
        final StringDictionaryIdentifier key = StringDictionaryIdentifier.builder().dictionary(accountHash).build();

        final StateRootHashData stateRootHash = casperService.getStateRootHash();
        final StoredValueData stateItem = casperService.getStateItem(
                stateRootHash.getStateRootHash(),
                key.getDictionary(),
                new ArrayList<>());

        assertThat(stateItem, is(notNullValue()));
        assertThat(stateItem.getStoredValue(), is(instanceOf(StoredValueAccount.class)));


        final Account account = (Account) stateItem.getStoredValue().getValue();
        assertThat(account.getAssociatedKeys(), is(not(empty())));

        for (NamedKey namedKey : account.getNamedKeys()) {
            if (namedKey.getName().startsWith("ERC20") && namedKey.getKey().startsWith("hash")) {
                return namedKey.getKey();
            }
        }
        throw new RuntimeException("Missing contract hash");
    }

    private void invokeContractUsingStoredContractByHash(final CasperService casperService,
                                                         final String contractHash,
                                                         final Ed25519PrivateKey faucetPrivateKey) throws Exception {

        final Ed25519PrivateKey recipientPrivateKey = Ed25519PrivateKey.deriveRandomKey();
        final PublicKey recipient = PublicKey.fromAbstractPublicKey(recipientPrivateKey.derivePublicKey());
        final String accountHash = recipient.generateAccountHash(false);
        final BigInteger amount = new BigInteger("2500000000");

        final List<NamedArg<?>> args = Arrays.asList(
                // new NamedArg<>("recipient", new CLValuePublicKey(recipient)),
                // Python SDK uses CLValueByteArray successfully for this argument
                new NamedArg<>("recipient", new CLValueByteArray(Hex.decode(accountHash))),
                new NamedArg<>("amount", new CLValueU256(amount))
        );

        final StoredContractByHash session = StoredContractByHash.builder()
                .entryPoint("transfer")
                .hash(contractHash)
                .args(args)
                .build();

        final ModuleBytes payment = getPaymentModuleBytes(new BigInteger("2500000000"));

        final String chainName = "casper-net-1";
        final Deploy transferDeploy = CasperDeployHelper.buildDeploy(faucetPrivateKey,
                chainName,
                session,
                payment,
                1L,
                Ttl.builder().ttl("30m").build(),
                new Date(),
                new ArrayList<>()
        );

        // So I can grab it when testing
        final String json = new ObjectMapper().writeValueAsString(transferDeploy);
        System.out.println(json);

        final DeployResult deployResult = casperService.putDeploy(transferDeploy);

        assertThat(deployResult.getDeployHash(), is(notNullValue()));

        DeployData deployData = waitForDeploy(casperService, deployResult.getDeployHash());

        assertThat(deployData.getExecutionResults().get(0).getResult(), is(instanceOf(Success.class)));
    }

    private DeployData waitForDeploy(CasperService casperService, final String deployHash) {

        final long timeout = 300 * 1000L;
        final long now = System.currentTimeMillis();

        DeployData deploy = null;

        while (deploy == null || deploy.getExecutionResults().isEmpty()) {

            deploy = casperService.getDeploy(deployHash);
            if (deploy.getExecutionResults().isEmpty() && System.currentTimeMillis() > now + timeout) {
                throw new RuntimeException("Timed-out waiting for deploy " + deployHash);
            }

            try {
                //noinspection BusyWait
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        return deploy;
    }
}
