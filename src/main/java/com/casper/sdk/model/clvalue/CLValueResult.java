package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeResult;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
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

    public CLValueResult(AbstractCLValue<?, ?> ok, AbstractCLValue<?, ?> err) {
        this.setValue(new Result(ok, err));
        setChildTypes();
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        if (this.getValue() == null) return;

        if (target.equals(Target.BYTE)) {
            super.serializePrefixWithLength(ser);
        }

        setChildTypes();

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
    public void deserialize(DeserializerBuffer deser) throws ValueDeserializationException {
        try {
            Result result = new Result();
            CLValueBool bool = new CLValueBool();
            bool.setValue(deser.readBool());
            CLTypeData typeOk = clType.getOkErrTypes().getOkClType().getClTypeData();
            AbstractCLValue<?, ?> clValueOk = CLTypeData.createCLValueFromCLTypeData(typeOk);
            if (clValueOk.getClType() instanceof AbstractCLTypeWithChildren) {
                ((AbstractCLTypeWithChildren) clValueOk.getClType()).getChildTypes()
                        .addAll(((AbstractCLTypeWithChildren) clType.getOkErrTypes().getOkClType()).getChildTypes());
            }
            clValueOk.deserialize(deser);
            result.setOk(clValueOk);

            bool = new CLValueBool();
            bool.deserialize(deser);
            CLTypeData typeErr = clType.getOkErrTypes().getErrClType().getClTypeData();
            AbstractCLValue<?, ?> clValueErr = CLTypeData.createCLValueFromCLTypeData(typeErr);
            if (clValueErr.getClType() instanceof AbstractCLTypeWithChildren) {
                ((AbstractCLTypeWithChildren) clValueErr.getClType()).getChildTypes()
                        .addAll(((AbstractCLTypeWithChildren) clType.getOkErrTypes().getErrClType()).getChildTypes());
            }
            clValueErr.deserialize(deser);
            result.setErr(clValueErr);

            setValue(result);
        } catch (NoSuchTypeException | DynamicInstanceException e) {
            throw new ValueDeserializationException(String.format("Error deserializing %s", this.getClass().getSimpleName()), e);
        }
    }

    protected void setChildTypes() {
        clType.setOkErrTypes(
                new CLTypeResult.CLTypeResultOkErrTypes(getValue().getOk().getClType(), getValue().getErr().getClType()));
    }
}