package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.CLTypeU256;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.CLValueDecodeException;
import com.casper.sdk.exception.CLValueEncodeException;
import com.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Casper U256 CLValue implementation
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CLValueU256 extends AbstractCLValue<BigInteger, CLTypeU256> {
    private CLTypeU256 clType = new CLTypeU256();

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonClType(CLTypeU256 clType) {
        this.clType = clType;
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    public CLValueU256(BigInteger value) {
        this.setValue(value);
    }

    @Override
    public void encode(CLValueEncoder clve, boolean encodeType) throws IOException, CLValueEncodeException, NoSuchTypeException {
        clve.writeU256(this);
        if (encodeType) {
            this.encodeType(clve);
        }
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException {
        clvd.readU256(this);
    }
}
