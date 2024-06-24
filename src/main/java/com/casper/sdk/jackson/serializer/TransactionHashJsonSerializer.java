package com.casper.sdk.jackson.serializer;

import com.casper.sdk.model.transaction.TransactionHash;
import com.casper.sdk.model.transaction.TransactionHashDeploy;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.WritableTypeId;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import java.io.IOException;

/**
 * JSON serializer for {@link TransactionHash} needed as JSON PRC serialization does not support embedded object types
 * using the correct serializeWithType method.
 *
 * @author ian@meywood.com
 */
public class TransactionHashJsonSerializer extends JsonSerializer<TransactionHash> {

    @Override
    public void serialize(final TransactionHash transactionHash,
                          final JsonGenerator gen,
                          final SerializerProvider serializerProvider) throws IOException {

        gen.writeStartObject();
        gen.writeStringField(getFieldName(transactionHash), transactionHash.toString());
        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(final TransactionHash transactionHash,
                                  final JsonGenerator gen,
                                  final SerializerProvider serializers,
                                  final TypeSerializer typeSer) throws IOException {
        final WritableTypeId writableTypeId = typeSer.typeId(transactionHash, JsonToken.VALUE_EMBEDDED_OBJECT);
        typeSer.writeTypePrefix(gen, writableTypeId);
        gen.writeString(transactionHash.toString());
        typeSer.writeTypeSuffix(gen, writableTypeId);
    }

    private String getFieldName(final TransactionHash transactionHash) {
        return transactionHash instanceof TransactionHashDeploy ? "Deploy" : "Version1";
    }
}
