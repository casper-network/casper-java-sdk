package com.casper.sdk.domain;

import com.casper.sdk.exceptions.ConversionException;
import com.casper.sdk.json.PublicKeyJsonSerializer;
import com.casper.sdk.json.SignatureJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

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
        this(getBytes(hex));
    }

    private static byte[] getBytes(String hex) {
        try{
            return fromString(hex);
        } catch (ConversionException e) {
            return hex.getBytes(StandardCharsets.UTF_8);
        }
    }
}
