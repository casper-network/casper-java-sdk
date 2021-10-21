package com.syntifi.casper.sdk.model.clvalue;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeResult;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Casper Result CLValue implementation
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
public class CLValueResult extends AbstractCLValue<CLValueResult.Result, CLTypeResult> {
    /**
     * `Result` with `Ok` and `Err` variants of `CLType`s.
     * 
     * @author Alexandre Carvalho
     * @author Andre Bertolace
     * @see CLTypeData
     * @since 0.0.1
     */
    @Data
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    protected class Result {
        private AbstractCLValue<?, ?> ok;

        private AbstractCLValue<?, ?> err;
    }

    @JsonProperty("cl_type")
    private CLTypeResult clType = new CLTypeResult();

    public CLValueResult(AbstractCLValue<?,?> ok, AbstractCLValue<?,?> err) {
        this.setValue(this.new Result(ok, err));
        setChildTypes();
    }

    @Override
    public void encode(CLValueEncoder clve)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        setChildTypes();

        CLValueBool clValueTrue = new CLValueBool(true);
        clValueTrue.encode(clve);

        getValue().getOk().encode(clve);

        CLValueBool clValueFalse = new CLValueBool(false);
        clValueFalse.encode(clve);

        getValue().getErr().encode(clve);

        setBytes(clValueTrue.getBytes() + getValue().getOk().getBytes() + clValueFalse.getBytes()
                + getValue().getErr().getBytes());
    }

    @Override
    public void decode(CLValueDecoder clvd)
            throws IOException, CLValueDecodeException, DynamicInstanceException, NoSuchTypeException {
        Result result = new Result();
        CLValueBool bool = new CLValueBool();
        bool.decode(clvd);
        CLTypeData typeOk = clType.getOkErrTypes().getOkClType().getClTypeData();
        AbstractCLValue<?, ?> clValueOk = CLTypeData.createCLValueFromCLTypeData(typeOk);
        if (clValueOk.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) clValueOk.getClType()).getChildTypes()
                    .addAll(((AbstractCLTypeWithChildren) clType.getOkErrTypes().getOkClType()).getChildTypes());
        }
        clValueOk.decode(clvd);
        result.setOk(clValueOk);

        bool = new CLValueBool();
        bool.decode(clvd);
        CLTypeData typeErr = clType.getOkErrTypes().getErrClType().getClTypeData();
        AbstractCLValue<?, ?> clValueErr = CLTypeData.createCLValueFromCLTypeData(typeErr);
        if (clValueErr.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) clValueErr.getClType()).getChildTypes()
                    .addAll(((AbstractCLTypeWithChildren) clType.getOkErrTypes().getErrClType()).getChildTypes());
        }
        clValueErr.decode(clvd);
        result.setErr(clValueErr);

        setValue(result);
    }

    protected void setChildTypes() {
        clType.setOkErrTypes(
                clType.new CLTypeResultOkErrTypes(getValue().getOk().getClType(), getValue().getErr().getClType()));
    }
}