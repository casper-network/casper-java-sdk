package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeResult;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;
import org.bouncycastle.util.encoders.Hex;

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
    @Getter
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    protected static class Result {
        private AbstractCLValue<?, ?> ok;

        private AbstractCLValue<?, ?> err;
    }

    @JsonProperty("cl_type")
    private CLTypeResult clType = new CLTypeResult();

    public CLValueResult(AbstractCLValue<?, ?> ok, AbstractCLValue<?, ?> err) throws ValueSerializationException {
        setChildTypes(ok, err);
        this.setValue(new Result(ok, err));
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        if (this.getValue() == null) return;

        if (target.equals(Target.BYTE)) {
            super.serializePrefixWithLength(ser);
        }

        setChildTypes(this.getValue().getOk(), this.getValue().getErr());

        CLValueBool clValueTrue = new CLValueBool(true);
        clValueTrue.serialize(ser);

        getValue().getOk().serialize(ser);

        CLValueBool clValueFalse = new CLValueBool(false);
        clValueFalse.serialize(ser);

        getValue().getErr().serialize(ser);

        if (target.equals(Target.BYTE)) {
            this.encodeType(ser);
        }

        this.setBytes(Hex.toHexString(ser.toByteArray()));
    }

    @Override
    public void deserializeCustom(DeserializerBuffer deser) throws Exception {
        Result result = new Result();
        CLValueBool bool = new CLValueBool();
        bool.setValue(deser.readBool());
        CLTypeData typeOk = clType.getOkErrTypes().getOkClType().getClTypeData();
        AbstractCLValue<?, ?> clValueOk = CLTypeData.createCLValueFromCLTypeData(typeOk);
        if (clValueOk.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) clValueOk.getClType()).getChildTypes()
                    .addAll(((AbstractCLTypeWithChildren) clType.getOkErrTypes().getOkClType()).getChildTypes());
        }
        clValueOk.deserializeCustom(deser);
        result.setOk(clValueOk);

        bool = new CLValueBool();
        bool.deserializeCustom(deser);
        CLTypeData typeErr = clType.getOkErrTypes().getErrClType().getClTypeData();
        AbstractCLValue<?, ?> clValueErr = CLTypeData.createCLValueFromCLTypeData(typeErr);
        if (clValueErr.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) clValueErr.getClType()).getChildTypes()
                    .addAll(((AbstractCLTypeWithChildren) clType.getOkErrTypes().getErrClType()).getChildTypes());
        }
        clValueErr.deserializeCustom(deser);
        result.setErr(clValueErr);

        setValue(result);
    }

    protected void setChildTypes(AbstractCLValue<?, ?> ok, AbstractCLValue<?, ?> err) {
        clType.setOkErrTypes(
                new CLTypeResult.CLTypeResultOkErrTypes(ok.getClType(), err.getClType()));
    }

    @Override
    public String toString() {
        return getValue() != null ? "Ok: " + getValue().getOk().getValue().toString() + ", " + "Err: " + getValue().getErr().getValue().toString() : null;
    }
}