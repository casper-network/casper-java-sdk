package com.casper.sdk.types;

import com.casper.sdk.exceptions.ConversionException;
import com.casper.sdk.service.hash.HashService;
import com.casper.sdk.service.json.JsonConversionService;
import com.casper.sdk.service.serialization.cltypes.TypesFactory;
import com.casper.sdk.service.serialization.cltypes.TypesSerializer;
import com.casper.sdk.service.serialization.types.ByteSerializerFactory;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.service.serialization.util.NumberUtils;
import com.casper.sdk.service.signing.SigningService;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Util methods for making Deploy message
 */
public class DeployService {

    private final ByteSerializerFactory serializerFactory;
    private final HashService hashService;
    private final JsonConversionService jsonService;
    private final SigningService signingService;
    private final TypesSerializer u64Serializer;
    private final TypesSerializer u512Serializer;

    public DeployService(final ByteSerializerFactory serializerFactory,
                         final HashService hashService,
                         final JsonConversionService jsonService,
                         final SigningService signingService,
                         final TypesFactory typesFactory) {
        this.serializerFactory = serializerFactory;
        this.hashService = hashService;
        this.jsonService = jsonService;
        this.signingService = signingService;
        u64Serializer = typesFactory.getInstance(CLType.U64);
        u512Serializer = typesFactory.getInstance(CLType.U512);
    }

    String toTtlStr(long ttl) {
        return Duration.ofMillis(ttl)
                .toString()
                .substring(2)
                .replaceAll("(\\d[HMS])(?!$)", "$1 ")
                .toLowerCase();
    }

    public byte[] toBytes(final Deploy deploy) {
        return serializerFactory.getByteSerializer(deploy).toBytes(deploy);
    }

    byte[] serializedHeader(final DeployHeader header) {
        return serializerFactory.getByteSerializerByType(DeployHeader.class).toBytes(header);
    }

    byte[] serializeBody(final DeployExecutable payment, final DeployExecutable session) {
        return ByteUtils.concat(toBytes(payment), toBytes(session));
    }

    byte[] serializeApprovals(final Set<DeployApproval> approvals) {
        return serializerFactory.getByteSerializerByType(Set.class).toBytes(approvals);
    }

    /**
     * Creates a new unsigned Deploy message
     *
     * @param deployParams the deploy parameters
     * @param session      the session
     * @param payment      the payment
     * @return a new deploy
     */
    public Deploy makeDeploy(final DeployParams deployParams,
                             final DeployExecutable session,
                             final DeployExecutable payment) {

        final Digest bodyHash = makeBodyHash(session, payment);

        final DeployHeader header = new DeployHeader(
                signingService.toClPublicKey(deployParams.getAccountKey()),
                deployParams.getTimestamp(),
                toTtlStr(deployParams.getTtl()),
                deployParams.getGasPrice(),
                bodyHash,
                deployParams.getDependencies(),
                deployParams.getChainName()
        );

        final byte[] serializedHeader = serializedHeader(header);
        final Digest deployHash = new Digest(hashService.getHash(serializedHeader));
        return new Deploy(deployHash, header, payment, session, new LinkedHashSet<>());
    }

    Digest makeBodyHash(final DeployExecutable session, final DeployExecutable payment) {
        final byte[] serializedBody = serializeBody(payment, session);
        final byte[] hash = hashService.getHash(serializedBody);
        return new Digest(hash);
    }

    public Transfer newTransfer(final Number amount, final CLPublicKey target, final Number id) {

        final byte[] amountBytes = u512Serializer.serialize(amount);

        // Prefix the option bytes with OPTION_NONE or OPTION_SOME
        final byte[] idBytes = CLOptionValue.prefixOption(u64Serializer.serialize(id));

        final String accountKey = target.toAccountHex();
        final String accountHash = hashService.getAccountHash(accountKey);

        final DeployNamedArg amountArg = new DeployNamedArg("amount", new CLValue(amountBytes, CLType.U512, amount.toString()));
        final DeployNamedArg targetArg = new DeployNamedArg("target", new CLValue(accountHash, new CLByteArrayInfo(32), target.toAccountHex()));
        final DeployNamedArg idArg = new DeployNamedArg("id", new CLOptionValue(idBytes, new CLOptionTypeInfo(new CLTypeInfo(CLType.U64)), id.toString()));

        return new Transfer(List.of(amountArg, targetArg, idArg));
    }

    /**
     * Creates a new standard payment
     *
     * @param paymentAmount the number of notes paying to execution engine
     * @return a new standard payment
     */
    public ModuleBytes standardPayment(final Number paymentAmount) {

        final BigInteger biAmount = NumberUtils.toBigInteger(paymentAmount);
        byte[] amountBytes = u512Serializer.serialize(biAmount);
        final DeployNamedArg paymentArg = new DeployNamedArg(
                "amount",
                new CLValue(amountBytes, CLType.U512, paymentAmount)
        );

        return new ModuleBytes(new byte[0], List.of(paymentArg));
    }

    public Deploy fromJson(final String json) {
        try {
            return jsonService.fromJson(json, Deploy.class);
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    public Deploy fromJson(final InputStream in) {
        try {
            return jsonService.fromJson(in, Deploy.class);
        } catch (IOException e) {
            throw new ConversionException(e);
        }
    }

    /**
     * Obtains the size of the deploy in bytes
     *
     * @param deploy the deploy whose size is to be obtained
     * @return the deploy byte size
     */
    public int deploySizeInBytes(Deploy deploy) {
        return toBytes(deploy).length;
    }

    byte[] toBytes(final DeployExecutable deployExecutable) {
        return serializerFactory.getByteSerializer(deployExecutable).toBytes(deployExecutable);
    }

    public Deploy signDeploy(final Deploy deploy, final KeyPair keyPair) {

        final byte[] signed = signingService.signWithPrivateKey(keyPair.getPrivate(), deploy.getHash().getHash());

        final CLPublicKey publicKey = signingService.toClPublicKey(keyPair.getPublic());

        // Update the deploy  approvals with signed
        deploy.getApprovals().add(
                new DeployApproval(
                        publicKey,
                        new Signature(signed, publicKey.getAlgorithm())
                )
        );

        return deploy;
    }
}
