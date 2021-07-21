package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.Digest;
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
