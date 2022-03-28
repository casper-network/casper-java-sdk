package com.syntifi.casper.sdk.service;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.InvalidByteStringException;
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
import com.syntifi.casper.sdk.model.key.AlgorithmTag;
import com.syntifi.casper.sdk.model.key.PublicKey;
import com.syntifi.casper.sdk.model.key.Signature;
import com.syntifi.crypto.key.AbstractPrivateKey;
import com.syntifi.crypto.key.Secp256k1PrivateKey;

import org.bouncycastle.crypto.digests.Blake2bDigest;

public class CasperTransferDeployService {

    public static Deploy buildTransferDeploy(AbstractPrivateKey fromPrivateKey,
            PublicKey toPublicKey, BigInteger amount, String chainName)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException,
            GeneralSecurityException, InvalidByteStringException {
        long id = new Random().nextInt();
        BigInteger paymentAmount = BigInteger.valueOf(10000000000L);
        long gasPrice = 1L;
        Ttl ttl = Ttl
                .builder()
                .ttl("30m")
                .build();
        return CasperTransferDeployService.buildTransferDeploy(chainName, fromPrivateKey, toPublicKey, amount,
                id,
                paymentAmount, gasPrice, ttl, null);
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
     * @throws NoSuchTypeException
     * @throws DynamicInstanceException
     * @throws CLValueEncodeException
     * @throws IOException
     * @throws GeneralSecurityException
     * @throws InvalidByteStringException
     */
    public static Deploy buildTransferDeploy(String chainName, AbstractPrivateKey fromPrivateKey,
            PublicKey toPublicKey, BigInteger amount, Long id, BigInteger paymentAmount,
            Long gasPrice, Ttl ttl, List<Digest> dependencies)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException,
            GeneralSecurityException, InvalidByteStringException {

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
        CLValueEncoder clve = new CLValueEncoder();
        session.encode(clve);
        payment.encode(clve);
        byte[] sessionAnPaymentBytes = clve.toByteArray();
        clve.flush();

        // TODO: use com.syntifi.crypto.key.hash.Blake2b digest method
        byte[] bodyHash = CasperTransferDeployService.digest(sessionAnPaymentBytes);

        DeployHeader deployHeader = DeployHeader
                .builder()
                .account(toPublicKey)
                .ttl(ttl)
                .timeStamp(new Date())
                .gasPrice(gasPrice)
                .bodyHash(Digest.digestFromBytes(bodyHash))
                .chainName(chainName)
                .dependencies(dependencies)
                .build();
        deployHeader.encode(clve);

        byte[] signatureBytes = fromPrivateKey.sign(bodyHash);
        Signature signature = new Signature();
        signature.setKey(signatureBytes);
        signature.setTag((fromPrivateKey instanceof Secp256k1PrivateKey)
                ? AlgorithmTag.SECP256K1
                : AlgorithmTag.ED25519);

        List<Approval> approvals = new LinkedList<>();
        approvals.add(Approval.builder()
                .signer(PublicKey.fromBytes(fromPrivateKey.derivePublicKey().getKey()))
                .signature(signature)
                .build());

        return Deploy.builder()
                .hash(Digest.digestFromBytes(bodyHash))
                .header(deployHeader)
                .payment(payment)
                .session(session)
                .approvals(approvals)
                .build();

    }

    // TODO: use com.syntifi.crypto.key.hash.Blake2b digest method and delete this
    static byte[] digest(byte[] input) {
        Blake2bDigest d = new Blake2bDigest();
        d.update(input, 0, input.length);
        byte[] result = new byte[d.getDigestSize()];
        d.doFinal(result, 0);
        return result;
    }
}
