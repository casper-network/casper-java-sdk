package com.syntifi.casper.sdk.jackson.serializer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.SerializationException;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.AbstractCLValue;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLType;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLTypeData;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.CLValueByteArray;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

import org.apache.commons.codec.EncoderException;

/**
 * Customize the serializing mapping of Casper's CLValue properties
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
public abstract class AbstractCLValueSerializer<T extends AbstractCLValue<?>> extends JsonSerializer<T> {
    private static final String CL_TYPE_JSON_PROP_NAME = "cl_type";

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

        writeCLTypeData(gen, value, false);

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
     * @throws EncoderException
     */
    protected void writeCLTypeData(JsonGenerator gen, AbstractCLValue<?> clValue, Boolean isChild) throws IOException {
        CLType clType = clValue.getClType();

        if (clType.getChildTypes() == null) {
            if (clType.getClTypeData().equals(CLTypeData.BYTE_ARRAY)) {
                writeCLTypeDataByteArray(gen, clValue, clType);
            } else if (Boolean.FALSE.equals(isChild)) {
                gen.writeStringField(CL_TYPE_JSON_PROP_NAME, clType.getClTypeData().getClTypeName());
            } else {
                gen.writeString(clType.getClTypeData().getClTypeName());
            }
        } else {
            if (Boolean.FALSE.equals(isChild)) {
                gen.writeObjectFieldStart(CL_TYPE_JSON_PROP_NAME);
            } else {
                gen.writeStartObject();
            }
            gen.writeFieldName(clType.getClTypeData().getClTypeName());
            if (clType.getClTypeData().equals(CLTypeData.LIST)) {
                gen.writeString(clType.getChildTypes().get(0).getClTypeData().getClTypeName());
            } else if (clType.getClTypeData().equals(CLTypeData.MAP)) {
                writeCLTypeDataMap(gen, clType);
            } else if (clType.getClTypeData().equals(CLTypeData.RESULT)) {
                writeCLTypeDataResult(gen, clType);
            } else if (clType.getClTypeData().equals(CLTypeData.OPTION)) {
                writeCLTypeDataOption(gen, clType);
            } else if (clValue.getValue() instanceof Iterable) {
                writeCLTypeDataIterable(gen, clValue);
            } else {
                throw new SerializationException(String.format("Can not serialize CLType %s",
                        clValue.getClType().getClTypeData().getClTypeName()));
            }
            gen.writeEndObject();
        }
    }

    private void writeCLTypeDataIterable(JsonGenerator gen, AbstractCLValue<?> clValue) throws IOException {
        gen.writeStartArray();
        for (AbstractCLValue<?> child : (Iterable<AbstractCLValue<?>>) clValue.getValue()) {
            writeCLTypeData(gen, child, true);
        }
        gen.writeEndArray();
    }

    private void writeCLTypeDataOption(JsonGenerator gen, CLType clType) throws IOException {
        gen.writeStartArray();
        if (!clType.getChildTypes().isEmpty()) {
            gen.writeString(clType.getChildTypes().get(0).getClTypeData().getClTypeName());
        }
        gen.writeEndArray();
    }

    private void writeCLTypeDataResult(JsonGenerator gen, CLType clType) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("ok", clType.getChildTypes().get(0).getClTypeData().getClTypeName());
        gen.writeStringField("err", clType.getChildTypes().get(1).getClTypeData().getClTypeName());
        gen.writeEndObject();
    }

    private void writeCLTypeDataMap(JsonGenerator gen, CLType clType) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("key", clType.getChildTypes().get(0).getClTypeData().getClTypeName());
        gen.writeStringField("value", clType.getChildTypes().get(1).getClTypeData().getClTypeName());
        gen.writeEndObject();
    }

    private void writeCLTypeDataByteArray(JsonGenerator gen, AbstractCLValue<?> clValue, CLType clType) throws IOException {
        gen.writeObjectFieldStart(CL_TYPE_JSON_PROP_NAME);
        gen.writeNumberField(clType.getClTypeData().getClTypeName(),
                ((CLValueByteArray) clValue).getValue().length);
        gen.writeEndObject();
    }

}
