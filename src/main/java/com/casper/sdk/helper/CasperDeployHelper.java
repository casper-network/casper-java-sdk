package com.casper.sdk.helper;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.CLValueU512;
import com.casper.sdk.model.clvalue.cltype.CLTypeU512;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.deploy.Approval;
import com.casper.sdk.model.deploy.Deploy;
import com.casper.sdk.model.deploy.DeployHeader;
import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.deploy.executabledeploy.ExecutableDeployItem;
import com.casper.sdk.model.deploy.executabledeploy.ModuleBytes;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.key.Signature;
import com.syntifi.crypto.key.AbstractPrivateKey;
import com.syntifi.crypto.key.hash.Blake2b;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Deploy Service class implementing the process to generate deploys
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.2.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CasperDeployHelper {

    public static DeployHeader buildDeployHeader(PublicKey fromPublicKey, String chainName,
                                                 Long gasPrice, Ttl ttl, Date date,
                                                 List<Digest> dependencies, byte[] bodyHash) {
        return DeployHeader
                .builder()
                .account(fromPublicKey)
                .ttl(ttl)
                .timeStamp(date)
                .gasPrice(gasPrice)
                .bodyHash(Digest.digestFromBytes(bodyHash))
                .chainName(chainName)
                .dependencies(dependencies)
                .build();
    }

    public static HashAndSignature signDeployHeader(AbstractPrivateKey privateKey, DeployHeader deployHeader)
            throws GeneralSecurityException, NoSuchTypeException, ValueSerializationException {
        SerializerBuffer serializerBuffer = new SerializerBuffer();

        deployHeader.serialize(serializerBuffer, Target.BYTE);
        byte[] headerHash = Blake2b.digest(serializerBuffer.toByteArray(), 32);
        Signature signature = Signature.sign(privateKey, headerHash);
        return new HashAndSignature(headerHash, signature);
    }

    public static byte[] getDeployItemAndModuleBytesHash(ExecutableDeployItem deployItem, ModuleBytes moduleBytes)
            throws NoSuchTypeException, ValueSerializationException {
        SerializerBuffer ser = new SerializerBuffer();
        moduleBytes.serialize(ser, Target.BYTE);
        deployItem.serialize(ser, Target.BYTE);
        return Blake2b.digest(ser.toByteArray(), 32);
    }

    public static ModuleBytes getPaymentModuleBytes(BigInteger paymentAmount) throws ValueSerializationException {
        List<NamedArg<?>> paymentArgs = new LinkedList<>();
        NamedArg<CLTypeU512> paymentArg = new NamedArg<>("amount",
                new CLValueU512(paymentAmount));
        paymentArgs.add(paymentArg);
        return ModuleBytes
                .builder()
                .args(paymentArgs)
                .bytes(new byte[]{})
                .build();
    }

    /**
     * Core method to fully build a deploy
     *
     * @param fromPrivateKey private key of the sender
     * @param chainName      name of chain
     * @param session        item to deploy ExecutableDeployItems
     * @param payment        Module bytes as another ExecuteDeployItems
     * @param gasPrice       gasPrice for native transfers can be set to 1
     * @param ttl            time to live in milliseconds (default value is 1800000
     *                       ms (30 minutes))
     * @param date           deploy date
     * @param dependencies   list of digest dependencies
     * @return
     * @throws NoSuchTypeException
     * @throws GeneralSecurityException
     * @throws ValueSerializationException
     */
    public static Deploy buildDeploy(AbstractPrivateKey fromPrivateKey, String chainName,
                                     ExecutableDeployItem session, ModuleBytes payment,
                                     Long gasPrice, Ttl ttl, Date date, List<Digest> dependencies)
            throws NoSuchTypeException, GeneralSecurityException, ValueSerializationException {

        byte[] sessionAnPaymentHash = getDeployItemAndModuleBytesHash(session, payment);

        PublicKey fromPublicKey = PublicKey.fromAbstractPublicKey(fromPrivateKey.derivePublicKey());

        DeployHeader deployHeader = buildDeployHeader(fromPublicKey, chainName, gasPrice, ttl,
                date, dependencies, sessionAnPaymentHash);

        HashAndSignature hashAndSignature = signDeployHeader(fromPrivateKey, deployHeader);

        List<Approval> approvals = new LinkedList<>();
        approvals.add(Approval.builder()
                .signer(PublicKey.fromAbstractPublicKey(fromPrivateKey.derivePublicKey()))
                .signature(hashAndSignature.getSignature())
                .build());

        return Deploy.builder()
                .hash(Digest.digestFromBytes(hashAndSignature.getHash()))
                .header(deployHeader)
                .payment(payment)
                .session(session)
                .approvals(approvals)
                .build();
    }

    @Getter
    @AllArgsConstructor
    private static class HashAndSignature {
        byte[] hash;
        Signature signature;
    }
}
