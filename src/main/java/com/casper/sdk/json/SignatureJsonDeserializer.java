package com.casper.sdk.json;

import com.casper.sdk.domain.Signature;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Converts a JSON string value to a Signature
 */
public class SignatureJsonDeserializer extends JsonDeserializer<Signature> {

    @Override
    public Signature deserialize(final JsonParser p, final DeserializationContext context) throws IOException {
        return new Signature(p.getText());
    }
}
