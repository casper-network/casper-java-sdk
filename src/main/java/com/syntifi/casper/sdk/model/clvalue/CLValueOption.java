package com.syntifi.casper.sdk.model.clvalue;

import java.io.IOException;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeOption;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Casper Option CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class CLValueOption extends AbstractCLValue<Optional<AbstractCLValue<?, ?>>, CLTypeOption> {
    @JsonProperty("cl_type")
    private CLTypeOption clType = new CLTypeOption();

    public CLValueOption() {
        this(Optional.of(new CLValueAny(null)));
    }

    public CLValueOption(Optional<AbstractCLValue<?, ?>> value) {
        this.setValue(value);
        this.clType.setChildType(value.isPresent() ? value.get().getClType() : null);
    }

    @Override
    public void encode(CLValueEncoder clve)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        Optional<AbstractCLValue<?, ?>> value = getValue();
        
        CLValueBool isPresent = new CLValueBool(value.isPresent() && value.get().getValue() != null);
        isPresent.encode(clve);
        setBytes(isPresent.getBytes());

        Optional<AbstractCLValue<?, ?>> child = getValue();

        if (child.isPresent() && child.get().getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child.get().getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildType()).getChildTypes());
        }
        if (child.isPresent() && isPresent.getValue().equals(Boolean.TRUE)) {
            child.get().encode(clve);
            setBytes(getBytes() + child.get().getBytes());
        }
    }

    @Override
    public void decode(CLValueDecoder clvd)
            throws IOException, CLValueDecodeException, DynamicInstanceException, NoSuchTypeException {
        CLValueBool isPresent = new CLValueBool();
        isPresent.decode(clvd);
        setBytes(isPresent.getBytes());

        CLTypeData childTypeData = clType.getChildType().getClTypeData();

        AbstractCLValue<?, ?> child = CLTypeData.createCLValueFromCLTypeData(childTypeData);

        if (child.getClType() instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) child.getClType())
                    .setChildTypes(((AbstractCLTypeWithChildren) clType.getChildType()).getChildTypes());
        }

        setValue(Optional.of(child));

        if (isPresent.getValue().equals(Boolean.TRUE)) {
            child.decode(clvd);

            setBytes(getBytes() + child.getBytes());
        }
    }
}
