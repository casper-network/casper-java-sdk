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

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
public abstract class AbstractCLValue<T, P extends AbstractCLType>
        implements CasperSerializableObject, CasperDeserializableObject {

    @Setter(AccessLevel.PROTECTED)
    private String bytes = "";

    @Setter
    @JsonProperty("parsed")
    @JsonInclude(Include.NON_NULL)
    private String parsed;

    @JsonIgnore
    private T value;

    public void setValue(T value) throws ValueSerializationException {
        this.value = value;
        this.serialize(new SerializerBuffer());
    }

    @SneakyThrows({ValueDeserializationException.class})
    @JsonSetter(value = "bytes")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonBytes(String bytes) {
        this.bytes = bytes;

        DeserializerBuffer deser = new DeserializerBuffer(this.bytes);
        this.deserialize(deser);
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
    public AbstractCLValue<?, ?> deserialize(DeserializerBuffer deser, Target target) throws ValueDeserializationException, NoSuchTypeException {
        if (target.equals(Target.BYTE)) {
            return AbstractCLValue.createInstanceFromBytes(deser);
        } else {
            deserialize(deser);
            return this;
        }
    }

    public static AbstractCLValue<?, ?> createInstanceFromBytes(DeserializerBuffer deser) throws ValueDeserializationException, NoSuchTypeException {
        int length = deser.readI32();
        byte[] bytes = deser.readByteArray(length);
        byte clType = deser.readU8();
        try {
            AbstractCLValue<?, ?> clValue = CLTypeData.getTypeBySerializationTag(clType).getClazz().getDeclaredConstructor().newInstance();
            clValue.deserialize(new DeserializerBuffer(Hex.encode(bytes)));
            return clValue;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new ValueDeserializationException("Error while instantiating CLValue", e);
        }
    }

    @Override
    public abstract void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException;

    @Override
    public abstract void deserialize(DeserializerBuffer deserializerBuffer) throws ValueDeserializationException;

    protected void encodeType(SerializerBuffer ser) throws NoSuchTypeException {
        byte val = (getClType().getClTypeData().getSerializationTag());
        ser.writeU8(val);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AbstractCLValue)) return false;
        final AbstractCLValue<?, ?> other = (AbstractCLValue<?, ?>) o;
        if (!other.canEqual(this)) return false;
        final Object this$bytes = this.getBytes();
        final Object other$bytes = other.getBytes();
        if (!Objects.equals(this$bytes, other$bytes)) return false;
        final Object this$value = this.getValue();
        final Object other$value = other.getValue();
        if (this$value instanceof Map) {
            return other$value.equals(other$value);
        } else {
            return Objects.equals(this$value, other$value);
        }
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AbstractCLValue;
    }
}
