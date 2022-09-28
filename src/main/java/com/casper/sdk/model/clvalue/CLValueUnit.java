package com.casper.sdk.model.clvalue;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.CLTypeUnit;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Casper Unit CLValue implementation
 * <p>
 * Unit is singleton value without additional semantics and serializes to an
 * empty byte array.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CLValueUnit extends AbstractCLValue<Object, CLTypeUnit> {
    private static final String UNITY_EMPTY_VALUE = "";

    private CLTypeUnit clType = new CLTypeUnit();

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonClType(CLTypeUnit clType) {
        this.clType = clType;
    }

    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected String getJsonClType() {
        return this.getClType().getTypeName();
    }

    public CLValueUnit() {
        this.setValue(UNITY_EMPTY_VALUE);
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws NoSuchTypeException {
        if (this.getValue() == null) return;

        setBytes(UNITY_EMPTY_VALUE);

        if (target.equals(Target.BYTE)) {
            this.encodeType(ser);
        }
    }

    @Override
    public void deserialize(DeserializerBuffer deser) {
        setBytes(UNITY_EMPTY_VALUE);
    }
}