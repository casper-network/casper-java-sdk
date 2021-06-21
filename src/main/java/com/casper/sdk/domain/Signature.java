package com.casper.sdk.domain;

import com.casper.sdk.json.PublicKeyJsonSerializer;
import com.casper.sdk.json.SignatureJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.nio.charset.StandardCharsets;

/**
 * Signature domain type used in deployment approvals.
 */
@JsonDeserialize(using = SignatureJsonDeserializer.class)
@JsonSerialize(using = PublicKeyJsonSerializer.class)
public class Signature extends PublicKey {

    public Signature(final byte[] bytes, final KeyAlgorithm keyAlgorithm) {
        super(bytes, keyAlgorithm);
    }

    public Signature(final String hex, final KeyAlgorithm keyAlgorithm) {
        this(hex.getBytes(StandardCharsets.UTF_8), keyAlgorithm);
    }

    public Signature(final String signature) {
        super(signature);
    }


}
