package com.casper.sdk.types;

import com.casper.sdk.service.json.deserialize.PublicKeyJsonDeserializer;
import com.casper.sdk.service.json.serialize.PublicKeyJsonSerializer;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;

/**
 * Representing a public key derived from an ECC key pair.
 */
@JsonDeserialize(using = PublicKeyJsonDeserializer.class)
@JsonSerialize(using = PublicKeyJsonSerializer.class)
public class CLPublicKey extends AbstractCLType implements HasTag {

    protected final Algorithm algorithm;
    /** Either 32 or 64 bytes (compressed) depending upon ECC type does not include the key algorithm byte */
    private final byte[] bytes;

    public CLPublicKey(final byte[] bytes, final Algorithm algorithm) {
        super(new CLTypeInfo(CLType.PUBLIC_KEY));

        Objects.requireNonNull(bytes, "bytes cannot be null");
        Objects.requireNonNull(algorithm, "keyAlgorithm cannot be null");

        this.algorithm = algorithm;
        this.bytes = bytes;

    }

    public CLPublicKey(final String key, final Algorithm algorithm) {
        this(ByteUtils.decodeHex(key), algorithm);
    }

    public CLPublicKey(final String key) {
        this(ByteUtils.decodeHex(key));
    }

    public CLPublicKey(final byte[] key) {
        this(removeAlgorithmBytes(key), Algorithm.fromId((char) key[0]));
    }

    private static byte[] removeAlgorithmBytes(final byte[] key) {
        byte[] bytes = new byte[key.length - 1];
        System.arraycopy(key, 1, bytes, 0, key.length - 1);
        return bytes;
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }

    public Algorithm getAlgorithm() {
        return algorithm;
    }

    public String toAccountHex() {
        return ByteUtils.encodeHexString(toAccount());
    }

    public byte[] toAccount() {
        return ByteUtils.concat(
                new byte[]{(byte) algorithm.getValue()},
                bytes
        );
    }

    @Override
    public int getTag() {
        return 1;
    }
}
