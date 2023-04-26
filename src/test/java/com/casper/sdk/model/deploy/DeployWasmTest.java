package com.casper.sdk.model.deploy;

import com.casper.sdk.helper.CasperConstants;
import com.casper.sdk.helper.CasperDeployHelper;
import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.clvalue.CLValueU256;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.CLValueU8;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.casper.sdk.service.CasperService;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.casper.sdk.helper.CasperDeployHelper.getPaymentModuleBytes;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Test that a smart contract can be deployed to a node.
 *
 * @author ian@meywood.com
 */
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
        final BigInteger payment = BigDecimal.valueOf(50e9).toBigInteger();
        final byte tokenDecimals = 11;
        final String tokenName = "Acme Token";
        final BigInteger tokenTotalSupply = BigDecimal.valueOf(1e15).toBigInteger();
        final String tokenSymbol = "ACME";

        // Load faucet private key
        final URL faucetPrivateKeyUrl = getClass().getResource(FAUCET_SECRET_KEY_PATH);
        final Ed25519PrivateKey privateKey = new Ed25519PrivateKey();
        privateKey.readPrivateKey(faucetPrivateKeyUrl.getFile());

        final List<NamedArg<?>> paymentArgs = new LinkedList<>();
        paymentArgs.add(new NamedArg<>("amount", new CLValueU512(payment)));
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
                null
        );

        final CasperService casperService = CasperService.usingPeer("localhost", 11101);

        final DeployResult deployResult = casperService.putDeploy(deploy);
        assertThat(deployResult, is(notNullValue()));
        assertThat(deployResult.getDeployHash(), is(notNullValue()));
    }
}
