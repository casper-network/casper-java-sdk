package com.casper.sdk.types;

import com.casper.sdk.service.json.deserialize.PublicKeyJsonDeserializer;
import com.casper.sdk.service.json.serialize.PublicKeyJsonSerializer;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;
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
        this(bytes, keyAlgorithm, false);
    }

    public PublicKey(final byte[] bytes, final SignatureAlgorithm keyAlgorithm, final boolean notStandardLength) {
        super(new CLTypeInfo(CLType.PUBLIC_KEY));
        Objects.requireNonNull(bytes, "bytes cannot be null");

        int keyLen = bytes.length % 8;

        if (keyLen == 0 || notStandardLength) {
            //Objects.requireNonNull(keyAlgorithm, "keyAlgorithm cannot be null");
            this.bytes = bytes;
            this.keyAlgorithm = keyAlgorithm;
        } else if (keyLen == 1) {
            // byte array
            this.keyAlgorithm = SignatureAlgorithm.fromId((char) bytes[0]);
            this.bytes = new byte[bytes.length - 1];
            System.arraycopy(bytes, 1, this.bytes, 0, bytes.length - 1);
        } else if (keyLen == 2) {
            // Hex string bytes
            this.keyAlgorithm = SignatureAlgorithm.fromId((char) bytes[1]);
            this.bytes = ByteUtils.decodeHex(new String(bytes).substring(2));
        } else {
            throw new IllegalArgumentException("Invalid key " + Arrays.toString(bytes) + " length " + bytes.length);
        }
    }

    public PublicKey(final String key, final SignatureAlgorithm keyAlgorithm) {
        super(new CLTypeInfo(CLType.PUBLIC_KEY));
        Objects.requireNonNull(key, "keys cannot be null");

        int keyLen = key.length() % 8;

        if (keyLen == 0) {
            this.bytes = ByteUtils.decodeHex(key);
            this.keyAlgorithm = keyAlgorithm;
        } else if (keyLen == 1) {
            // byte array
            this.keyAlgorithm = SignatureAlgorithm.fromId(key.charAt(0));
            this.bytes = ByteUtils.decodeHex(key.substring(1));
        } else if (keyLen == 2) {
            // Hex string bytes
            this.keyAlgorithm = SignatureAlgorithm.fromId(key.charAt(1));
            this.bytes = ByteUtils.decodeHex(key.substring(2));
        } else {
            throw new IllegalArgumentException("Invalid key " + key + " length " + key.length());
        }
    }

    public PublicKey(final String key) {
        this(key, null);
    }

    public PublicKey(final byte[] key) {
        this(key, null);
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
