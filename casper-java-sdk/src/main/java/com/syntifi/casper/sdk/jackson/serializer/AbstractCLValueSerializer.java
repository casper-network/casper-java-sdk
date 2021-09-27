package com.syntifi.casper.sdk.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.SerializationException;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.AbstractCLValue;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLTypeData;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLType;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Customize the serializing mapping of Casper's CLValue properties
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public abstract class AbstractCLValueSerializer<T extends AbstractCLValue<?>> extends JsonSerializer<T> {

    /**
     * Custom implementation of JsonSerializer method
     */
    @Override
    public void serialize(T value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // We must encode the value before serialization
        try (CLValueEncoder clve = new CLValueEncoder()) {
            value.encode(clve);
        } catch (CLValueEncodeException | DynamicInstanceException e) {
            throw new SerializationException("Error serializing CLValues", e);
        }

        gen.writeStartObject();

        writeCLType(gen, value.getClType(), false);

        gen.writeStringField("bytes", value.getBytes());

        // TODO: How to work with parsed value? Set as null or do we have to parse?
        if (value.getParsed() != null) {
            gen.writeObjectField("parsed", value.getParsed());
        }

        gen.writeEndObject();
    }

    /**
     * Serializes the cl_type correctly from the SDK data
     * 
     * @param gen     the json generator
     * @param clType  clType
     * @param isChild tells if clType is a child, not the root object - used for
     *                recursion purposes
     * @throws IOException
     */
    protected void writeCLType(JsonGenerator gen, CLType clType, Boolean isChild) throws IOException {
        if (clType.getChildTypes() == null) {
            if (Boolean.FALSE.equals(isChild)) {
                gen.writeStringField("cl_type", clType.getClTypeData().getClTypeName());
            } else {
                gen.writeString(clType.getClTypeData().getClTypeName());
            }
        } else {
            if (Boolean.FALSE.equals(isChild)) {
                gen.writeObjectFieldStart("cl_type");
            } else {
                gen.writeStartObject();
            }
            gen.writeFieldName(clType.getClTypeData().getClTypeName());
            if (clType.getClTypeData().equals(CLTypeData.LIST)) {
                gen.writeString(clType.getChildTypes().get(0).getClTypeData().getClTypeName());
            } else if (clType.getClTypeData().equals(CLTypeData.MAP)) {
                gen.writeStartObject();
                gen.writeStringField("key", clType.getChildTypes().get(0).getClTypeData().getClTypeName());
                gen.writeStringField("value", clType.getChildTypes().get(1).getClTypeData().getClTypeName());
                gen.writeEndObject();
            } else if (clType.getClTypeData().equals(CLTypeData.RESULT)) {
                gen.writeStartObject();
                gen.writeStringField("ok", clType.getChildTypes().get(0).getClTypeData().getClTypeName());
                gen.writeStringField("err", clType.getChildTypes().get(1).getClTypeData().getClTypeName());
                gen.writeEndObject();
            } else {
                gen.writeStartArray();
                for (CLType child : clType.getChildTypes()) {
                    writeCLType(gen, child, true);
                }
                gen.writeEndArray();
            }
            gen.writeEndObject();
        }
    }

}
