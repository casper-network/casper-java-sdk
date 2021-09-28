package com.casper.sdk.service.json.serialize;

import com.casper.sdk.service.serialization.util.ByteUtils;
import com.casper.sdk.types.CLKeyInfo;
import com.casper.sdk.types.CLKeyValue;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * JSON serializer for the specialized {@link CLKeyValue}
 */
public class CLKeyValueJsonSerializer extends JsonSerializer<CLKeyValue> {

    @Override
    public void serialize(final CLKeyValue value,
                          final JsonGenerator gen,
                          final SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeFieldName("cl_type");
        gen.getCodec().writeValue(gen, value.getCLTypeInfo());
        gen.writeFieldName("bytes");
        gen.writeString(buildJsonBytes(value));
        writeParsed(value, gen);
        gen.writeEndObject();
    }

    @NotNull
    private String buildJsonBytes(final CLKeyValue value) {
        return getValueBytes(value);
    }

    protected String getValueBytes(final CLKeyValue value) {
        return value.toHex();
    }

    private void writeParsed(CLKeyValue value, JsonGenerator gen) throws IOException {

        final String strParsed;

        if (value.getParsed() != null) {
            strParsed = buildParsed(value.getParsed().toString(), value.getKeyType());
        } else if (value.getKeyBytes() != null) {
            strParsed = buildParsed(ByteUtils.encodeHexString(value.getKeyBytes()), value.getKeyType());
        } else {
            strParsed = null;
        }

        if (strParsed != null) {
            gen.writeFieldName("parsed");
            gen.writeStartObject();
            gen.writeFieldName("Hash");
            gen.writeString(strParsed);
            gen.writeEndObject();
        }
    }

    private String buildParsed(final String parsed, final CLKeyInfo.KeyType keyType) {
        if (parsed.contains("-")) {
            return parsed;
        } else {
            return keyType.getParsedName() + "-" + parsed;
        }
    }
}
