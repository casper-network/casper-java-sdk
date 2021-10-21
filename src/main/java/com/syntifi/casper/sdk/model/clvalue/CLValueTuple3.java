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
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeTuple3;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;

import org.javatuples.Triplet;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Casper Tuple3 CLValue implementation
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
public class CLValueTuple3 extends
        AbstractCLValueWithChildren<Triplet<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>>, CLTypeTuple3> {
    @JsonProperty("cl_type")
    private CLTypeTuple3 clType = new CLTypeTuple3();

    public CLValueTuple3(Triplet<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) {
        this.setValue(value);
        setChildTypes();
    }

    @Override
    public void encode(CLValueEncoder clve)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        setChildTypes();

        getValue().getValue0().encode(clve);
        getValue().getValue1().encode(clve);
        getValue().getValue2().encode(clve);

        setBytes(getValue().getValue0().getBytes() + getValue().getValue1().getBytes()
                + getValue().getValue2().getBytes());
    }

    @Override
    public void decode(CLValueDecoder clvd)
            throws IOException, CLValueDecodeException, DynamicInstanceException, NoSuchTypeException {
        CLTypeData childTypeData1 = clType.getChildClTypeData(0);
        CLTypeData childTypeData2 = clType.getChildClTypeData(1);
        CLTypeData childTypeData3 = clType.getChildClTypeData(2);

        AbstractCLValue<?, ?> child1 = CLTypeData.createCLValueFromCLTypeData(childTypeData1);
        if (child1.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child1.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildTypes().get(0)).getChildTypes());
        }
        child1.decode(clvd);

        AbstractCLValue<?, ?> child2 = CLTypeData.createCLValueFromCLTypeData(childTypeData2);
        if (child2.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child2.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildTypes().get(1)).getChildTypes());
        }
        child2.decode(clvd);

        AbstractCLValue<?, ?> child3 = CLTypeData.createCLValueFromCLTypeData(childTypeData3);
        if (child3.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child3.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildTypes().get(2)).getChildTypes());
        }
        child3.decode(clvd);

        setValue(new Triplet<>(child1, child2, child3));
        setBytes(getValue().getValue0().getBytes() + getValue().getValue1().getBytes()
                + getValue().getValue2().getBytes());
    }

    @Override
    protected void setChildTypes() {
        clType.setChildTypes(Arrays.asList(getValue().getValue0().getClType(), getValue().getValue1().getClType(),
                getValue().getValue2().getClType()));

    }
}
