package com.casper.sdk.domain;

import com.casper.sdk.exceptions.ConversionException;
import com.casper.sdk.service.HashService;
import com.casper.sdk.service.serialization.ByteArrayBuilder;
import com.casper.sdk.service.serialization.ByteUtils;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;
import java.util.LinkedHashSet;
import java.util.List;

import static com.casper.sdk.service.serialization.ByteUtils.toU32;

/**
 * Util methods for making Deploy message
 */
public class DeployUtil {

    private static final HashService hashService = HashService.getInstance();

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

        final byte[] serializedHeader = serializeHeader(header);
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
    public static Payment standardPayment(final BigInteger paymentAmount) {

        final DeployNamedArg paymentArg = new DeployNamedArg(
                "amount",
                new CLValue(paymentAmount.toByteArray(), CLType.U512, paymentAmount)
        );

        return new Payment(new byte[0], List.of(paymentArg));
    }


    private static String toTtlStr(long ttl) {
        return (ttl / 60000) + "m";
    }


    private static byte[] serializeHeader(final DeployHeader header) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        builder.append(header.getAccount().getBytes());
        builder.append(ByteUtils.toU64(header.getTtlLong()));
        builder.append(ByteUtils.toU64(header.getGasPrice()));
        builder.append(toBytesDeployHash(header.getBodyHash()));
        builder.append(toBytesDigests(header.getDependencies()));
        builder.append(ByteUtils.toBytes(header.getChainName()));

        return builder.toByteArray();
    }

    private static byte[] toBytesDigests(final List<Digest> source) {
        if (source.isEmpty()) {
            return new byte[0];
        } else {
            final ByteArrayBuilder builder = new ByteArrayBuilder();
            builder.append(toU32(source.size()));
            for (Digest digest : source) {
                builder.append(digest.getHash());
            }

            return builder.toByteArray();
        }
    }

    private static byte[] toBytesDeployHash(final Digest bodyHash) {
        return toBytesBytesArray(bodyHash.getHash());
    }

    private static byte[] toBytesBytesArray(String hash) {
        try {
            return Hex.decodeHex(hash.toCharArray());
        } catch (DecoderException e) {
            throw new ConversionException(e);
        }
    }

    private static byte[] serializeBody(final DeployExecutable payment, final DeployExecutable session) {
        return ByteUtils.concat(toBytes(payment), toBytes(session));
    }

    private static byte[] toBytes(final DeployExecutable deployExecutable) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        // Append the type of the Deploy Executable in a single byte
        builder.append(new byte[]{(byte) deployExecutable.getTag()});

        if (deployExecutable.isPayment()) {
            builder.append(((Payment) deployExecutable).getModuleBytes());
        }

        builder.append(toBytes(deployExecutable.getArgs()));

        return builder.toByteArray();
    }

    static byte[] toBytes(final List<DeployNamedArg> args) {

        final ByteArrayBuilder builder = new ByteArrayBuilder();

        // append the number of arguments as LE U32 array
        builder.append(toU32(args.size()));

        // Append each argument
        args.forEach(deployNamedArg -> builder.append(toBytes(deployNamedArg)));
        return builder.toByteArray();
    }


    static byte[] toBytes(final DeployNamedArg deployNamedArg) {

        byte[] name = deployNamedArg.getName().getBytes();

        return ByteUtils.concat(
                ByteUtils.concat(toU32(name.length), name),
                toBytes(deployNamedArg.getValue())
        );
    }


    static byte[] toBytes(final CLValue value) {
        return ByteUtils.concat(toBytes(value.getCLTypeInfo()), value.getBytes());
    }

    private static byte[] toBytes(final CLTypeInfo clTypeInfo) {
        return CLTypeHelper.toBytesHelper(clTypeInfo);
    }
}
