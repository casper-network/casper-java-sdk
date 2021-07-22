package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.PublicKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Converts a JSON hex string public key value value to a {@link PublicKey} type object.
 */
public class PublicKeyJsonDeserializer extends JsonDeserializer<PublicKey> {

    @Override
    public PublicKey deserialize(final JsonParser p, final DeserializationContext context) throws IOException {//
        return new PublicKey(p.getText());
    }
}
