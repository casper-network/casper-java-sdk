package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.jackson.resolver.CLValueResolver;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.serde.CasperDeserializableObject;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.util.ByteUtils;
import lombok.*;

/**
 * Base class for CLValues
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CLTypeData
 * @since 0.0.1
 */
@Getter
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(CLValueResolver.class)
@EqualsAndHashCode(of = {"bytes", "value"})
public abstract class AbstractCLValue<T, P extends AbstractCLType>
        implements CasperSerializableObject, CasperDeserializableObject {

    @Setter(AccessLevel.PROTECTED)
    private String bytes = "";

    @Setter
    @JsonProperty("parsed")
    @JsonInclude(Include.NON_NULL)
    private Object parsed;

    @JsonIgnore
    private T value;

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) throws ValueSerializationException {
        this.value = value;
        this.serialize(new SerializerBuffer());
    }

    public static AbstractCLValue<?, ?> createInstanceFromBytes(DeserializerBuffer deser) throws ValueDeserializationException {
        int length = deser.readI32();
        byte[] bytes = deser.readByteArray(length);
        byte clType = deser.readU8();
        try {
            AbstractCLValue<?, ?> clValue = CLTypeData.getTypeBySerializationTag(clType).getClazz().getDeclaredConstructor().newInstance();
            clValue.deserializeCustom(new DeserializerBuffer(Hex.encode(bytes)));
            return clValue;
        } catch (Exception e) {
            throw new ValueDeserializationException("Error while instantiating CLValue", e);
        }
    }

    @SneakyThrows({ValueSerializationException.class, NoSuchTypeException.class})
    @JsonGetter(value = "bytes")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonBytes() {
        SerializerBuffer ser = new SerializerBuffer();
        this.serialize(ser, Target.JSON);

        this.bytes = ByteUtils.encodeHexString(ser.toByteArray());

        return this.bytes;
    }

    @SneakyThrows({ValueDeserializationException.class})
    @JsonSetter(value = "bytes")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonBytes(String bytes) {
        this.bytes = bytes;

        DeserializerBuffer deser = new DeserializerBuffer(this.bytes);

        this.deserialize(deser);
    }

    @JsonIgnore
    public abstract P getClType();

    public abstract void setClType(P value);


    protected void serializePrefixWithLength(SerializerBuffer ser) throws ValueSerializationException {
        SerializerBuffer localSer = new SerializerBuffer();
        serialize(localSer);
        int size = localSer.toByteArray().length;
        ser.writeI32(size);
    }

    @Override
    public AbstractCLValue<?, ?> deserialize(DeserializerBuffer deser, Target target) throws ValueDeserializationException {
        if (target.equals(Target.BYTE)) {
            return AbstractCLValue.createInstanceFromBytes(deser);
        } else {
            deserialize(deser);
            return this;
        }
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        if (this.getValue() == null) return;

        if (target.equals(Target.BYTE)) {
            serializePrefixWithLength(ser);
        }

        serializeValue(ser);

        if (target.equals(Target.BYTE)) {
            this.encodeType(ser);
        }
    }


    protected abstract void serializeValue(final SerializerBuffer ser) throws ValueSerializationException;

    public abstract void deserializeCustom(DeserializerBuffer deserializerBuffer) throws Exception;

    @Override
    public void deserialize(DeserializerBuffer deserializerBuffer) throws ValueDeserializationException {
        try {
            this.deserializeCustom(deserializerBuffer);
        } catch (Exception e) {
            throw new ValueDeserializationException("Error deserializing value", e);
        }
    }

    protected void encodeType(SerializerBuffer ser) throws NoSuchTypeException {
        byte typeTag = (getClType().getClTypeData().getSerializationTag());
        ser.writeU8(typeTag);
    }
}
