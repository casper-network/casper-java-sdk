package com.casper.sdk.domain;

import java.util.Objects;

/**
 * Domain type: output of a hashing function.
 */
public class Digest {

    /** 32 byte array emitted by a hashing algorithm */
    private final String hash;

    public Digest(final String hash) {
        Objects.requireNonNull(hash, "hash must not be null");
        this.hash = hash;
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
