package com.casper.sdk.json;

import com.casper.sdk.domain.PublicKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Converts a JSON hex string public key value value to a PublicKey
 */
public class PublicKeyJsonDeserialize extends JsonDeserializer<PublicKey> {
    @Override
    public PublicKey deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        return new PublicKey(p.getText());
    }
}
