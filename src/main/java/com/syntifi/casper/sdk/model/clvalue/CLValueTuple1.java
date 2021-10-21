package com.syntifi.casper.sdk.model.clvalue;

import java.io.IOException;
import java.util.Arrays;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeTuple1;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;

import org.javatuples.Unit;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Casper Tuple1 CLValue implementation
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
public class CLValueTuple1 extends AbstractCLValueWithChildren<Unit<? extends AbstractCLValue<?, ?>>, CLTypeTuple1> {
    @JsonProperty("cl_type")
    private CLTypeTuple1 clType = new CLTypeTuple1();

    public CLValueTuple1(Unit<? extends AbstractCLValue<?, ?>> value) {
        this.setValue(value);
        setChildTypes();
    }

    @Override
    public void encode(CLValueEncoder clve)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        setChildTypes();

        getValue().getValue0().encode(clve);

        setBytes(getValue().getValue0().getBytes());
    }

    @Override
    public void decode(CLValueDecoder clvd)
            throws IOException, CLValueDecodeException, DynamicInstanceException, NoSuchTypeException {
        CLTypeData childTypeData1 = clType.getChildClTypeData(0);

        AbstractCLValue<?, ?> child1 = CLTypeData.createCLValueFromCLTypeData(childTypeData1);
        if (child1.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child1.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildTypes().get(0)).getChildTypes());
        }
        child1.decode(clvd);

        setValue(new Unit<>(child1));
        setBytes(getValue().getValue0().getBytes());
    }

    @Override
    protected void setChildTypes() {
        clType.setChildTypes(Arrays.asList(getValue().getValue0().getClType()));
    }
}
