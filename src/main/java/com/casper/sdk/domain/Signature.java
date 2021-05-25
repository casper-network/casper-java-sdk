package com.casper.sdk.domain;

import com.casper.sdk.json.PublicKeyJsonSerializer;
import com.casper.sdk.json.SignatureJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Signature domain type used in deployment approvals.
 */
@JsonDeserialize(using = SignatureJsonDeserializer.class)
@JsonSerialize(using = PublicKeyJsonSerializer.class)
public class Signature extends PublicKey {

    public Signature(final byte[] bytes) {
        super(bytes);
    }

    public Signature(final String hex) {
        this(fromString(hex));
    }
}
