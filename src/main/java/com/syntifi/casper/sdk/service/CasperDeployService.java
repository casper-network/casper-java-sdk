package com.syntifi.casper.sdk.service;

import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.CLValueOption;
import com.syntifi.casper.sdk.model.clvalue.CLValuePublicKey;
import com.syntifi.casper.sdk.model.clvalue.CLValueU512;
import com.syntifi.casper.sdk.model.clvalue.CLValueU64;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeOption;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypePublicKey;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeU512;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.common.Digest;
import com.syntifi.casper.sdk.model.common.Ttl;
import com.syntifi.casper.sdk.model.deploy.Approval;
import com.syntifi.casper.sdk.model.deploy.Deploy;
import com.syntifi.casper.sdk.model.deploy.DeployHeader;
import com.syntifi.casper.sdk.model.deploy.NamedArg;
import com.syntifi.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.syntifi.casper.sdk.model.deploy.executabledeploy.Transfer;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.key.Signature;
import com.syntifi.crypto.key.AbstractPrivateKey;
import com.syntifi.crypto.key.hash.Blake2b;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Deploy Service class implementing the process to generate deploys
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
public class CasperDeployService {

    /**
     * Method to generate a Transfer deploy
     *
     * @param fromPrivateKey sender private Key
     * @param toPublicKey    receiver public key
     * @param amount         amount to transfer
     * @param chainName      network name
     * @return Deploy
     * @throws NoSuchTypeException      thrown if type not found
     * @throws DynamicInstanceException thrown if it could not instantiate a type
     * @throws CLValueEncodeException   thrown if failed to encode a cl value
     * @throws IOException              thrown if an IO error occurs
     * @throws GeneralSecurityException thrown when an error occurs with cryptographic keys
     */
    public static Deploy buildTransferDeploy(AbstractPrivateKey fromPrivateKey,
                                             PublicKey toPublicKey, BigInteger amount, String chainName)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException,
            GeneralSecurityException {
        long id = Math.abs(new Random().nextInt());
        BigInteger paymentAmount = BigInteger.valueOf(25000000000L);
        long gasPrice = 1L;
        Ttl ttl = Ttl
                .builder()
                .ttl("30m")
                .build();
        return CasperDeployService.buildTransferDeploy(fromPrivateKey,
                toPublicKey, amount, chainName, id, paymentAmount,
                gasPrice, ttl, new Date(), new ArrayList<>());
    }


    /**
     * @param fromPrivateKey private key of the sender
     * @param toPublicKey    public key of the receiver
     * @param amount         amount to transfer
     * @param id             id field in the request to tag the transaction
     * @param paymentAmount, the number of motes paying to the execution engine
     * @param gasPrice       gasPrice for native transfers can be set to 1
     * @param ttl            time to live in milliseconds (default value is 1800000
     *                       ms (30 minutes))
     * @return Deploy
     * @throws NoSuchTypeException      thrown if type not found
     * @throws DynamicInstanceException thrown if it could not instantiate a type
     * @throws CLValueEncodeException   thrown if failed to encode a cl value
     * @throws IOException              thrown if an IO error occurs
     * @throws GeneralSecurityException thrown when an error occurs with cryptographic keys
     */
    public static Deploy buildTransferDeploy(AbstractPrivateKey fromPrivateKey, PublicKey toPublicKey,
                                             BigInteger amount, String chainName, Long id, BigInteger paymentAmount,
                                             Long gasPrice, Ttl ttl, Date date, List<Digest> dependencies)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException,
            GeneralSecurityException {

        List<NamedArg<?>> transferArgs = new LinkedList<>();
        NamedArg<CLTypeU512> amountNamedArg = new NamedArg<>("amount",
                new CLValueU512(amount));
        transferArgs.add(amountNamedArg);
        NamedArg<CLTypePublicKey> publicKeyNamedArg = new NamedArg<>("target",
                new CLValuePublicKey(toPublicKey));
        transferArgs.add(publicKeyNamedArg);
        CLValueOption idArg = new CLValueOption(Optional.of(
                new CLValueU64(BigInteger.valueOf(682008))));
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
        CLValueEncoder clve = new CLValueEncoder();
        payment.encode(clve, true);
        session.encode(clve, true);
        byte[] sessionAnPaymentHash = Blake2b.digest(clve.toByteArray(), 32);
        clve.flush();
        clve.reset();

        PublicKey fromPublicKey = PublicKey.fromAbstractPublicKey(fromPrivateKey.derivePublicKey());

        DeployHeader deployHeader = DeployHeader
                .builder()
                .account(fromPublicKey)
                .ttl(ttl)
                .timeStamp(date)
                .gasPrice(gasPrice)
                .bodyHash(Digest.digestFromBytes(sessionAnPaymentHash))
                .chainName(chainName)
                .dependencies(dependencies)
                .build();
        deployHeader.encode(clve, true);
        byte[] headerHash = Blake2b.digest(clve.toByteArray(), 32);

        Signature signature = Signature.sign(fromPrivateKey, headerHash);

        List<Approval> approvals = new LinkedList<>();
        approvals.add(Approval.builder()
                .signer(PublicKey.fromAbstractPublicKey(fromPrivateKey.derivePublicKey()))
                .signature(signature)
                .build());

        return Deploy.builder()
                .hash(Digest.digestFromBytes(headerHash))
                .header(deployHeader)
                .payment(payment)
                .session(session)
                .approvals(approvals)
                .build();

    }
}
