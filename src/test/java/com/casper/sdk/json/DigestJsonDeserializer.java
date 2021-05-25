package com.casper.sdk.json;

import com.casper.sdk.domain.Digest;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Converts a JSON string to a Digest
 */
public class DigestJsonDeserializer extends JsonDeserializer<Digest> {

    @Override
    public Digest deserialize(final JsonParser p, final DeserializationContext context) throws IOException {
        return new Digest(p.getText());
    }
}
