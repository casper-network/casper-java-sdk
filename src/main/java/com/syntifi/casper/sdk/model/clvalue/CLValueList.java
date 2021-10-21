package com.syntifi.casper.sdk.model.clvalue;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.exception.NoSuchTypeException;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.syntifi.casper.sdk.model.clvalue.cltype.CLTypeList;
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
public class CLValueList extends AbstractCLValue<List<? extends AbstractCLValue<?, ?>>, CLTypeList> {
    @JsonProperty("cl_type")
    private CLTypeList clType = new CLTypeList();

    public CLValueList(List<? extends AbstractCLValue<?, ?>> value) {
        this.setValue(value);
        setListType();
    }

    @Override
    public void encode(CLValueEncoder clve)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        setListType();

        // List length is written first
        CLValueI32 length = new CLValueI32(getValue().size());
        length.encode(clve);
        setBytes(length.getBytes());

        for (AbstractCLValue<?, ?> child : getValue()) {
            child.encode(clve);
            setBytes(getBytes() + child.getBytes());
        }
    }

    @Override
    public void decode(CLValueDecoder clvd)
            throws IOException, CLValueDecodeException, DynamicInstanceException, NoSuchTypeException {
        CLTypeData childrenType = getClType().getListType().getClTypeData();

        // List length is sent first
        CLValueI32 length = new CLValueI32();
        length.decode(clvd);
        setBytes(length.getBytes());

        List<AbstractCLValue<?, ?>> list = new LinkedList<>();
        for (int i = 0; i < length.getValue(); i++) {
            AbstractCLValue<?, ?> child = CLTypeData.createCLValueFromCLTypeData(childrenType);
            if (child.getClType() instanceof AbstractCLTypeWithChildren) {
                ((AbstractCLTypeWithChildren) child.getClType())
                        .setChildTypes(((AbstractCLTypeWithChildren) clType.getListType()).getChildTypes());
            }
            child.decode(clvd);
            setBytes(getBytes() + child.getBytes());
            list.add(child);
        }

        setValue(list);
    }

    protected void setListType() {
        clType.setListType(getValue().get(0).getClType());
    }
}
