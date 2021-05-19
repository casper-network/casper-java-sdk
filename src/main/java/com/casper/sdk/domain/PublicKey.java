package com.casper.sdk.domain;

import com.casper.sdk.json.PublicKeyJsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Objects;

/**
 * Domain type: representing a public key derived from an ECC key pair.
 */
@JsonDeserialize(using = PublicKeyJsonDeserialize.class)
public class PublicKey extends AbstractCLType {

    /** Either 32 or 33 bytes (compressed) depending upon ECC type */
    private final byte[] bytes;

    public PublicKey(final byte[] bytes) {
        super(CLType.PUBLIC_KEY);
        Objects.requireNonNull(bytes, "bytes cannot be null");
        this.bytes = bytes;
    }

    public PublicKey(final String hex) {
        this(fromString(hex));
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }
}
