package com.casper.sdk.json;

import com.casper.sdk.domain.PublicKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Converts a JSON hex string public key value value to a {@link PublicKey} domain object.
 */
public class PublicKeyJsonDeserializer extends JsonDeserializer<PublicKey> {

    @Override
    public PublicKey deserialize(final JsonParser p, final DeserializationContext context) throws IOException {
        return new PublicKey(p.getText());
    }
}
