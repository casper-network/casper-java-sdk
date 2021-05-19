package com.casper.sdk.domain;

import com.casper.sdk.json.SignatureJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Objects;

@JsonDeserialize(using = SignatureJsonDeserializer.class)
public class Signature extends AbstractCLType {
    /** Either 32 or 33 bytes (compressed) depending upon ECC type */
    private final byte[] bytes;

    public Signature(final byte[] bytes) {
        super(CLType.BYTE_ARRAY);
        Objects.requireNonNull(bytes, "bytes cannot be null");
        this.bytes = bytes;
    }

    public Signature(final String hex) {
        this(fromString(hex));
    }

    @Override
    public byte[] getBytes() {
        return bytes;
    }
}
