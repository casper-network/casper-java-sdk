package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.jackson.resolver.CLValueResolver;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
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
@JsonPropertyOrder({"cl_type", "bytes", "parsed"})
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

    @SuppressWarnings("LombokGetterMayBeUsed")
    public T getValue() {
        return this.value;
    }

    public void setValue(final T value) throws ValueSerializationException {
        this.value = value;
        this.serialize(new SerializerBuffer());
    }

    public static AbstractCLValue<?, ?> createInstanceFromBytes(final DeserializerBuffer deser) throws ValueDeserializationException {
        final int length = deser.readI32();
        final byte[] bytes = deser.readByteArray(length);
        final byte clType = deser.readU8();

        try {
            CLTypeData clTypeData = CLTypeData.getTypeBySerializationTag(clType);
            final AbstractCLValue<?, ?> clValue = clTypeData.getClazz().getDeclaredConstructor().newInstance();
            if (clValue instanceof AbstractCLValueWithChildren) {
                // We have only obtained the parent type from the buffer now we need to read the child types
                ((AbstractCLTypeWithChildren) clValue.getClType()).deserializeChildTypes(deser);
            }

            clValue.deserializeCustom(new DeserializerBuffer(Hex.encode(bytes)));
            return clValue;
        } catch (Exception e) {
            throw new ValueDeserializationException("Error while instantiating CLValue", e);
        }
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @SneakyThrows({ValueSerializationException.class, NoSuchTypeException.class})
    @JsonGetter(value = "bytes")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonBytes() {
        final SerializerBuffer ser = new SerializerBuffer();
        this.serialize(ser, Target.JSON);

        this.bytes = ByteUtils.encodeHexString(ser.toByteArray());

        return this.bytes;
    }

    @SneakyThrows({ValueDeserializationException.class})
    @JsonSetter(value = "bytes")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonBytes(final String bytes) {
        this.bytes = bytes;
        this.deserialize(new DeserializerBuffer(this.bytes));
    }

    @JsonIgnore
    public abstract P getClType();

    public abstract void setClType(P value);


    protected void serializePrefixWithLength(final SerializerBuffer ser) throws ValueSerializationException {
        final SerializerBuffer localSer = new SerializerBuffer();
        serialize(localSer);
        final int size = localSer.toByteArray().length;
        ser.writeI32(size);
    }

    @Override
    public AbstractCLValue<?, ?> deserialize(final DeserializerBuffer deser, final Target target) throws ValueDeserializationException {
        if (target.equals(Target.BYTE)) {
            return AbstractCLValue.createInstanceFromBytes(deser);
        } else {
            deserialize(deser);
            return this;
        }
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        if (this.getValue() == null) return;

        if (Target.BYTE.equals(target)) {
            serializePrefixWithLength(ser);
        }

        serializeValue(ser);

        if (Target.BYTE.equals(target)) {
            getClType().serialize(ser);
        }
    }

    protected abstract void serializeValue(final SerializerBuffer ser) throws ValueSerializationException;

    public abstract void deserializeCustom(final DeserializerBuffer deserializerBuffer) throws Exception;

    @Override
    public void deserialize(final DeserializerBuffer deserializerBuffer) throws ValueDeserializationException {
        try {
            this.deserializeCustom(deserializerBuffer);
        } catch (Exception e) {
            throw new ValueDeserializationException("Error deserializing value", e);
        }
    }
}
