package com.casper.sdk.types;

import com.casper.sdk.service.json.serialize.DigestJsonJSerializer;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Arrays;
import java.util.Objects;

/**
 * Type: output of a hashing function.
 */
@JsonSerialize(using = DigestJsonJSerializer.class)
public class Digest {

    /** The number of characters needed for a 32 byte hash */
    private static final int BYTES_32_HEX = 32;
    /** 32 byte array emitted by a hashing algorithm */
    private final byte[] hash;

    public Digest(final String hash) {
        this(toBytes(hash));
    }

    public Digest(final byte[] hash) {
        Objects.requireNonNull(hash, "Hash must not be null");
        if (hash.length != BYTES_32_HEX) {
            throw new IllegalArgumentException("Hash must be 32 bytes long: " + Arrays.toString(hash));
        }
        this.hash = hash;
    }

    public byte[] getHash() {
        return hash;
    }

    @Override
    public String toString() {
        return ByteUtils.encodeHexString(getHash());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Digest digest = (Digest) o;
        return Arrays.equals(hash, digest.hash);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(hash);
    }

    private static byte [] toBytes(final String hash) {
        Objects.requireNonNull(hash, "Hash must not be null");
        if (hash.length() != BYTES_32_HEX * 2) {
            throw new IllegalArgumentException("Hash must be 32 bytes long: " + hash);
        }
        return ByteUtils.decodeHex(hash);
    }
}
