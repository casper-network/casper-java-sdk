package com.casper.sdk.model.clvalue;

import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeResult;
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

    public CLValueResult(final AbstractCLValue<?, ?> ok, final AbstractCLValue<?, ?> err) throws ValueSerializationException {
        setChildTypes(ok, err);
        this.setValue(new Result(ok, err));
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {
        setChildTypes(this.getValue().getOk(), this.getValue().getErr());

        SerializerBuffer serVal = new SerializerBuffer();

        CLValueBool clValueTrue = new CLValueBool(true);
        clValueTrue.serialize(serVal);

        getValue().getOk().serialize(serVal);

        CLValueBool clValueFalse = new CLValueBool(false);
        clValueFalse.serialize(serVal);

        getValue().getErr().serialize(serVal);

        final byte[] bytes = serVal.toByteArray();
        ser.writeByteArray(bytes);

        this.setBytes(Hex.toHexString(bytes));
    }

    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        final Result result = new Result();
        CLValueBool bool = new CLValueBool();
        bool.setValue(deser.readBool());
        final CLTypeData typeOk = clType.getOkErrTypes().getOkClType().getClTypeData();
        final AbstractCLValue<?, ?> clValueOk = CLTypeData.createCLValueFromCLTypeData(typeOk);
        if (clValueOk.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) clValueOk.getClType()).getChildTypes()
                    .addAll(((AbstractCLTypeWithChildren) clType.getOkErrTypes().getOkClType()).getChildTypes());
        }
        clValueOk.deserializeCustom(deser);
        result.setOk(clValueOk);

        bool = new CLValueBool();
        bool.deserializeCustom(deser);
        final CLTypeData typeErr = clType.getOkErrTypes().getErrClType().getClTypeData();
        final AbstractCLValue<?, ?> clValueErr = CLTypeData.createCLValueFromCLTypeData(typeErr);
        if (clValueErr.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) clValueErr.getClType()).getChildTypes()
                    .addAll(((AbstractCLTypeWithChildren) clType.getOkErrTypes().getErrClType()).getChildTypes());
        }
        clValueErr.deserializeCustom(deser);
        result.setErr(clValueErr);

        setValue(result);
    }

    protected void setChildTypes(final AbstractCLValue<?, ?> ok, AbstractCLValue<?, ?> err) {
        clType.setOkErrTypes(
                new CLTypeResult.CLTypeResultOkErrTypes(ok.getClType(), err.getClType()));
    }

    @Override
    public String toString() {
        return getValue() != null ? "Ok: " + getValue().getOk().getValue().toString() + ", " + "Err: " + getValue().getErr().getValue().toString() : null;
    }
}
