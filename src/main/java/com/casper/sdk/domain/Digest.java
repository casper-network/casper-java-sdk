package com.casper.sdk.domain;

import com.casper.sdk.json.DigestJsonJSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Objects;

/**
 * Domain type: output of a hashing function.
 */
@JsonSerialize(using = DigestJsonJSerializer.class)
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
