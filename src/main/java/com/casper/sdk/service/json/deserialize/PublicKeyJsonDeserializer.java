package com.casper.sdk.service.json.deserialize;

import com.casper.sdk.types.CLPublicKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * Converts a JSON hex string public key value to a {@link CLPublicKey} type object.
 */
public class PublicKeyJsonDeserializer extends JsonDeserializer<CLPublicKey> {

    @Override
    public CLPublicKey deserialize(final JsonParser p, final DeserializationContext context) throws IOException {//
        return new CLPublicKey(p.getText());
    }
}
