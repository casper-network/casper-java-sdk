package com.syntifi.casper.sdk.service;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.syntifi.casper.sdk.model.clvalue.CLValueOption;
import com.syntifi.casper.sdk.model.clvalue.CLValuePublicKey;
import com.syntifi.casper.sdk.model.clvalue.CLValueU512;
import com.syntifi.casper.sdk.model.clvalue.CLValueU64;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeOption;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypePublicKey;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeU512;
import com.syntifi.casper.sdk.model.deploy.Deploy;
import com.syntifi.casper.sdk.model.deploy.NamedArg;
import com.syntifi.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.syntifi.casper.sdk.model.deploy.executabledeploy.Transfer;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.crypto.key.AbstractPrivateKey;

public class CasperTransferDeployService {

    public static Deploy buildTransferDeploy(AbstractPrivateKey fromPrivateKey,
            PublicKey toPublicKey, BigInteger amount) {
        long id = new Random().nextInt();
        BigInteger paymentAmount = BigInteger.valueOf(10000000000L);
        long gasPrice = 1L;
        long ttl = 1800000L;
        return CasperTransferDeployService.buildTransferDeploy(fromPrivateKey, toPublicKey, amount, id,
                paymentAmount, gasPrice, ttl);
    }

    /**
     * 
     * @param fromPrivateKey
     * @param toPublicKey
     * @param amount
     * @param id             id field in the request to tag the transaction
     * @param paymentAmount, the number of motes paying to the execution engine
     * @param gasPrice       gasPrice for native transfers can be set to 1
     * @param ttl            time to live in milliseconds (default value is 1800000
     *                       ms (30 minutes))
     * @return
     */
    public static Deploy buildTransferDeploy(AbstractPrivateKey fromPrivateKey,
            PublicKey toPublicKey, BigInteger amount, Long id, BigInteger paymentAmount,
            Long gasPrice, Long ttl) {
        List<NamedArg<?>> transferArgs = new LinkedList<>();

        NamedArg<CLTypeU512> amountNamedArg = new NamedArg<>("amount",
                new CLValueU512(amount));
        transferArgs.add(amountNamedArg);

        NamedArg<CLTypePublicKey> publicKeyNamedArg = new NamedArg<>("target",
                new CLValuePublicKey(toPublicKey));
        transferArgs.add(publicKeyNamedArg);

        CLValueOption idArg = new CLValueOption(Optional.of(
                new CLValueU64(BigInteger.valueOf(id))));
        NamedArg<CLTypeOption> idNamedArg = new NamedArg<>("id", idArg);
        transferArgs.add(idNamedArg);
        Transfer session = Transfer
                .builder()
                .args(transferArgs)
                .build();

        List<NamedArg<?>> paymentArgs = new LinkedList<>();
        NamedArg<CLTypeU512> paymentArg = new NamedArg<>("amount",
                new CLValueU512(paymentAmount));
        paymentArgs.add(paymentArg);

        ModuleBytes payment = ModuleBytes
                .builder()
                .args(paymentArgs)
                .bytes("")
                .build();

        
        Deploy deploy = new Deploy();

        return deploy;
    }

}
