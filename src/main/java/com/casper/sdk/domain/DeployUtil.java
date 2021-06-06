package com.casper.sdk.domain;

import com.casper.sdk.service.HashService;
import com.casper.sdk.service.serialization.domain.ByteSerializerFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.service.serialization.util.NumberUtils;

import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Util methods for making Deploy message
 */
public class DeployUtil {

    private static final HashService hashService = HashService.getInstance();
    private static final ByteSerializerFactory serializerFactory = new ByteSerializerFactory();


    public static Deploy makeDeploy(final DeployParams deployParams,
                                    final DeployExecutable session,
                                    final DeployExecutable payment) {

        final byte[] serializedBody = serializeBody(payment, session);
        final Digest bodyHash = new Digest(hashService.get32ByteHash(serializedBody));

        final DeployHeader header = new DeployHeader(
                deployParams.getAccountPublicKey(),
                deployParams.getTimestamp(),
                toTtlStr(deployParams.getTtl()),
                deployParams.getGasPrice(),
                bodyHash,
                deployParams.getDependencies(),
                deployParams.getChainName()
        );

        final byte[] serializedHeader = serializedHeader(header);
        final Digest deployHash = new Digest(hashService.get32ByteHash(serializedHeader));
        return new Deploy(deployHash, header, payment, session, new LinkedHashSet<>());
    }

    public static Transfer makeTransfer(final Number amount, final PublicKey target, final Number id) {

        final byte[] amountBytes = ByteUtils.toU512(amount);
        final byte[] idBytes = ByteUtils.toU64(id);

        final DeployNamedArg amountArg = new DeployNamedArg("amount", new CLValue(amountBytes, CLType.U512, amount.toString()));
        final DeployNamedArg targetArg = new DeployNamedArg("target", new CLValue(target.getBytes(), new CLByteArrayInfo(32), target.toString()));
        final DeployNamedArg idArg = new DeployNamedArg("id", new CLValue(idBytes, CLType.U64, id.toString()));

        return new Transfer(List.of(amountArg, targetArg, idArg));
    }

    /**
     * Creates a new standard payment
     *
     * @param paymentAmount the number of notes paying to execution engine
     */
    public static StoredContractByName standardPayment(final Number paymentAmount) {

        final BigInteger biAmount = NumberUtils.toBigInteger(paymentAmount);

        final DeployNamedArg paymentArg = new DeployNamedArg(
                "amount",
                new CLValue(biAmount.toByteArray(), CLType.U512, paymentAmount)
        );

        return new StoredContractByName("payment", null, List.of(paymentArg));
    }

    private static String toTtlStr(long ttl) {
        return (ttl / 60000) + "m";
    }

    private static byte[] serializedHeader(final DeployHeader header) {
        return serializerFactory.getByteSerializerByType(DeployHeader.class).toBytes(header);
    }

    private static byte[] serializeBody(final DeployExecutable payment, final DeployExecutable session) {
        return ByteUtils.concat(toBytes(payment), toBytes(session));
    }

    static byte[] toBytes(final DeployExecutable deployExecutable) {
        return serializerFactory.getByteSerializer(deployExecutable).toBytes(deployExecutable);
    }
}
