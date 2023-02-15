package com.casper.sdk.helper;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.CLValueOption;
import com.casper.sdk.model.clvalue.CLValuePublicKey;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.CLValueU64;
import com.casper.sdk.model.clvalue.cltype.CLTypeOption;
import com.casper.sdk.model.clvalue.cltype.CLTypePublicKey;
import com.casper.sdk.model.clvalue.cltype.CLTypeU512;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.casper.sdk.model.deploy.executabledeploy.Transfer;
import com.casper.sdk.model.key.PublicKey;
import com.syntifi.crypto.key.AbstractPrivateKey;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * Transfer helper provides methods to easily transfer from/to purses
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.5.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CasperTransferHelper {
    /**
     * Helper method to create a Deploy for a Transfer
     *
     * @param from      private key from sender
     * @param to        public key from signer
     * @param amount    amount to transfer
     * @param chainName chain name
     * @return a transfer deploy
     * @throws NoSuchTypeException
     * @throws GeneralSecurityException
     * @throws ValueSerializationException
     */
    public static Deploy buildTransferDeploy(AbstractPrivateKey from, PublicKey to,
                                             BigInteger amount, String chainName)
            throws NoSuchTypeException, GeneralSecurityException, ValueSerializationException {
        long id = Math.abs(new Random().nextInt());
        Ttl ttl = Ttl
                .builder()
                .ttl(CasperConstants.DEFAULT_DEPLOY_TTL.value / 60 / 1000 + "m")
                .build();
        BigInteger paymentAmount = BigInteger.valueOf(CasperConstants.STANDARD_PAYMENT_FOR_NATIVE_TRANSFERS.value);
        return buildTransferDeploy(from, to, amount, chainName, id, paymentAmount,
                CasperConstants.DEFAULT_GAS_PRICE.value, ttl, new Date(), new ArrayList<>());
    }

    /**
     * Helper method to create a Deploy for a Transfer
     *
     * @param signer        private key from sender
     * @param to            public key from signer
     * @param amount        amount to transfer
     * @param chainName     chain name
     * @param id            deploy id
     * @param paymentAmount payment amount for processing transfers
     * @param gasPrice      gas price
     * @param ttl           time to live
     * @param date          execution date
     * @param dependencies  List of digest dependencies
     * @return a transfer deploy
     * @throws NoSuchTypeException
     * @throws GeneralSecurityException
     * @throws ValueSerializationException
     */
    public static Deploy buildTransferDeploy(AbstractPrivateKey signer, PublicKey to, BigInteger amount,
                                             String chainName, Long id, BigInteger paymentAmount,
                                             Long gasPrice, Ttl ttl, Date date, List<Digest> dependencies)
            throws NoSuchTypeException, GeneralSecurityException, ValueSerializationException {
        List<NamedArg<?>> transferArgs = new LinkedList<>();
        NamedArg<CLTypeU512> amountNamedArg = new NamedArg<>("amount",
                new CLValueU512(amount));
        transferArgs.add(amountNamedArg);
        NamedArg<CLTypePublicKey> publicKeyNamedArg = new NamedArg<>("target",
                new CLValuePublicKey(to));
        transferArgs.add(publicKeyNamedArg);

        CLValueOption idArg = (id == null) ?
                new CLValueOption(Optional.of(new CLValueU64())) :
                new CLValueOption(Optional.of(new CLValueU64(BigInteger.valueOf(id))));

        NamedArg<CLTypeOption> idNamedArg = new NamedArg<>("id", idArg);
        transferArgs.add(idNamedArg);

        Transfer session = Transfer
                .builder()
                .args(transferArgs)
                .build();
        ModuleBytes payment = CasperDeployHelper.getPaymentModuleBytes(paymentAmount);

        return CasperDeployHelper.buildDeploy(signer, chainName, session, payment, gasPrice, ttl,
                date, dependencies);
    }


}
