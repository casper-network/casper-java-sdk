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
public class PublicKey extends AbstractCLType implements HasTag {

    protected final SignatureAlgorithm keyAlgorithm;
    /** Either 32 or 64 bytes (compressed) depending upon ECC type does not include the key algorithm byte */
    private final byte[] bytes;

    public PublicKey(final byte[] bytes, final SignatureAlgorithm keyAlgorithm) {
        super(new CLTypeInfo(CLType.PUBLIC_KEY));

        Objects.requireNonNull(bytes, "bytes cannot be null");
        Objects.requireNonNull(keyAlgorithm, "keyAlgorithm cannot be null");

        this.keyAlgorithm = keyAlgorithm;
        this.bytes = bytes;

    }

    public PublicKey(final String key, final SignatureAlgorithm keyAlgorithm) {
        this(ByteUtils.decodeHex(key), keyAlgorithm);
    }

    public PublicKey(final String key) {
        this(ByteUtils.decodeHex(key));
    }

    public PublicKey(final byte[] key) {
        this(removeAlgorithmBytes(key), SignatureAlgorithm.fromId((char) key[0]));
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

    public SignatureAlgorithm getKeyAlgorithm() {
        return keyAlgorithm;
    }

    public String toAccountHex() {
        return ByteUtils.encodeHexString(toAccount());
    }

    public byte[] toAccount() {
        return ByteUtils.concat(
                new byte[]{(byte) keyAlgorithm.getValue()},
                bytes
        );
    }

    @Override
    public int getTag() {
        return 1;
    }
}
