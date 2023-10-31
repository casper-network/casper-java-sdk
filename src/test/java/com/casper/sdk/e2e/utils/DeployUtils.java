package com.casper.sdk.e2e.utils;

import com.casper.sdk.e2e.exception.TimeoutException;
import com.casper.sdk.helper.CasperDeployHelper;
import com.casper.sdk.model.clvalue.*;
import com.casper.sdk.model.clvalue.cltype.CLTypeOption;
import com.casper.sdk.model.clvalue.cltype.CLTypePublicKey;
import com.casper.sdk.model.clvalue.cltype.CLTypeU512;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployData;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.casper.sdk.model.deploy.executabledeploy.Transfer;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.service.CasperService;
import com.syntifi.crypto.key.Ed25519PrivateKey;
import com.syntifi.crypto.key.Ed25519PublicKey;

import java.math.BigInteger;
import java.util.*;

/**
 * @author ian@meywood.com
 */
public class DeployUtils {
    public static DeployData waitForDeploy(final String deployHash,
                                           final int timeoutSeconds,
                                           final CasperService casperService) {

        final long timeout = timeoutSeconds * 1000L;
        final long now = System.currentTimeMillis();

        DeployData deploy = null;

        while (deploy == null || deploy.getExecutionResults().isEmpty()) {

            deploy = casperService.getDeploy(deployHash);
            if (deploy.getExecutionResults().isEmpty() && System.currentTimeMillis() > now + timeout) {
                throw new TimeoutException("Timed-out waiting for deploy " + deployHash);
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

    public static Deploy buildStandardTransferDeploy(final List<NamedArg<?>> namedArgs) throws Exception {
        final Ed25519PrivateKey senderKey = new Ed25519PrivateKey();
        final Ed25519PublicKey receiverKey = new Ed25519PublicKey();

        senderKey.readPrivateKey(AssetUtils.getUserKeyAsset(1, 1, "secret_key.pem").getFile());
        receiverKey.readPublicKey(AssetUtils.getUserKeyAsset(1, 2, "public_key.pem").getFile());

        final List<NamedArg<?>> transferArgs = new LinkedList<>();
        final NamedArg<CLTypeU512> amountNamedArg = new NamedArg<>("amount", new CLValueU512(new BigInteger("2500000000")));
        transferArgs.add(amountNamedArg);
        final NamedArg<CLTypePublicKey> publicKeyNamedArg = new NamedArg<>("target", new CLValuePublicKey(PublicKey.fromAbstractPublicKey(receiverKey)));
        transferArgs.add(publicKeyNamedArg);

        final CLValueOption idArg = new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(200))));
        final NamedArg<CLTypeOption> idNamedArg = new NamedArg<>("id", idArg);
        transferArgs.add(idNamedArg);
        transferArgs.addAll(namedArgs);
        final Transfer session = Transfer.builder().args(transferArgs).build();
        final ModuleBytes payment = CasperDeployHelper.getPaymentModuleBytes(new BigInteger("100000000"));

        final Ttl ttl = Ttl.builder().ttl("30m").build();

        final Deploy deploy = CasperDeployHelper.buildDeploy(
                senderKey,
                "casper-net-1",
                session,
                payment,
                1L,
                ttl,
                new Date(),
                new ArrayList<>()
        );

        return deploy;
    }

    public static AbstractCLValue<?, ?> getNamedArgValue(final String type, final List<NamedArg<?>> namedArgs) {
        return namedArgs.stream()
                .filter(namedArg -> type.equals(namedArg.getType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Named arg " + type + " not found"))
                .getClValue();
    }
}
