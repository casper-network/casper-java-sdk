package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.jackson.resolver.CLValueResolver;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.serde.CasperDeserializableObject;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.syntifi.crypto.key.encdec.Hex;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.util.ByteUtils;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;

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
}
