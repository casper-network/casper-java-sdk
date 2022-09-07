package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.jackson.resolver.CLValueResolver;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.interfaces.DeserializableObject;
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
@EqualsAndHashCode(of = {"bytes", "value"})
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(CLValueResolver.class)
public abstract class AbstractCLValue<T, P extends AbstractCLType> implements CasperSerializableObject, DeserializableObject {

    @Setter(AccessLevel.PROTECTED)
    private String bytes = "";

    @Setter
    @JsonProperty("parsed")
    @JsonInclude(Include.NON_NULL)
    private String parsed;

    @Setter
    @JsonIgnore
    private T value;

    @SneakyThrows({ValueSerializationException.class})
    @JsonGetter(value = "bytes")
    @ExcludeFromJacocoGeneratedReport
    public String getBytes() {
        SerializerBuffer ser = new SerializerBuffer();
        this.serialize(ser);

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

    public abstract void serialize(SerializerBuffer ser, boolean encodeType) throws ValueSerializationException, NoSuchTypeException;

    public abstract void deserialize(DeserializerBuffer deserializerBuffer) throws ValueDeserializationException;

    public void encodeType(SerializerBuffer ser) throws NoSuchTypeException {
        byte val = (getClType().getClTypeData().getSerializationTag());
        ser.writeU8(val);
    }
}
