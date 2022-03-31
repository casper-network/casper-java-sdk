package com.syntifi.casper.sdk.model.clvalue;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.jackson.resolver.CLValueResolver;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.interfaces.DecodableValue;
import com.syntifi.casper.sdk.model.clvalue.encdec.interfaces.EncodableValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

/**
 * Base class for CLValues
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CLTypeData
 * @since 0.0.1
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"bytes", "value"})
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(CLValueResolver.class)
public abstract class AbstractCLValue<T, P extends AbstractCLType> implements EncodableValue, DecodableValue {

    private String bytes = "";

    @JsonProperty("parsed")
    @JsonInclude(Include.NON_NULL)
    private String parsed;

    @JsonIgnore
    private T value;

    @JsonGetter(value = "bytes")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonBytes()
            throws IOException, CLValueEncodeException, NoSuchTypeException {
        try (CLValueEncoder clve = new CLValueEncoder()) {
            this.encode(clve, false);
        }

        return this.bytes;
    }

    @JsonSetter(value = "bytes")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonBytes(String bytes)
            throws IOException, CLValueDecodeException, DynamicInstanceException, NoSuchTypeException {
        this.bytes = bytes;

        try (CLValueDecoder clvd = new CLValueDecoder(this.bytes)) {
            this.decode(clvd);
        }
    }

    @JsonIgnore
    public abstract P getClType();

    public abstract void setClType(P value);

    public abstract void encode(CLValueEncoder clve, boolean encodeType) throws IOException, NoSuchTypeException, CLValueEncodeException;

    public void encodeType(CLValueEncoder clve) throws NoSuchTypeException {
        byte val = (getClType().getClTypeData().getSerializationTag());
        clve.write(val);
    }
}
