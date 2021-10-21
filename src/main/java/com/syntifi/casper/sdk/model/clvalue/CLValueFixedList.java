package com.syntifi.casper.sdk.model.clvalue;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.exception.BufferEndCLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeFixedList;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.clvalue.encdec.CLValueEncoder;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Casper List CLValue implementation
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
public class CLValueFixedList extends AbstractCLValue<List<? extends AbstractCLValue<?, ?>>, CLTypeFixedList> {
    @JsonProperty("cl_type")
    private CLTypeFixedList clType = new CLTypeFixedList();

    public CLValueFixedList(List<? extends AbstractCLValue<?, ?>> value) {
        this.setValue(value);
        setListType();
    }

    @Override
    public void encode(CLValueEncoder clve)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        setListType();

        setBytes("");
        for (AbstractCLValue<?, ?> child : getValue()) {
            child.encode(clve);
            setBytes(getBytes() + child.getBytes());
        }
    }

    @Override
    public void decode(CLValueDecoder clvd)
            throws IOException, CLValueDecodeException, DynamicInstanceException, NoSuchTypeException {
        CLTypeData childrenType = getClType().getListType().getClTypeData();

        List<AbstractCLValue<?, ?>> list = new LinkedList<>();

        boolean hasMoreItems = true;
        do {
            AbstractCLValue<?, ?> child = CLTypeData.createCLValueFromCLTypeData(childrenType);
            if (child.getClType() instanceof AbstractCLTypeWithChildren) {
                ((AbstractCLTypeWithChildren) child.getClType())
                        .setChildTypes(((AbstractCLTypeWithChildren) clType.getListType()).getChildTypes());
            }
            try {
                child.decode(clvd);
                list.add(child);
            } catch (BufferEndCLValueDecodeException bede) {
                hasMoreItems = false;
            }
        } while (hasMoreItems);

        setValue(list);
    }

    protected void setListType() {
        clType.setListType(getValue().get(0).getClType());
    }
}
