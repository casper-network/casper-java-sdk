package com.casper.sdk.domain;

import com.casper.sdk.json.DigestJsonJSerializer;
import com.casper.sdk.service.serialization.util.ByteUtils;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;

/**
 * Domain type: output of a hashing function.
 */
@JsonSerialize(using = DigestJsonJSerializer.class)
public class Digest {

    /** The number of characters needed for a 32 byte hash */
    private static final int BYTES_32_HEX = 64;
    /** 32 byte array emitted by a hashing algorithm */
    private final String hash;

    public Digest(final String hash) {
        Objects.requireNonNull(hash, "Hash must not be null");
        if (hash.length() != BYTES_32_HEX) {
            throw new IllegalArgumentException("Hash must be 32 bytes long: " + hash);
        }
        this.hash = hash;
    }

    public Digest(final byte[] hash) {
        this(ByteUtils.encodeHexString(hash));
    }

    public String getHash() {
        return hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Digest digest = (Digest) o;
        return hash.equals(digest.hash);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hash);
    }
}
