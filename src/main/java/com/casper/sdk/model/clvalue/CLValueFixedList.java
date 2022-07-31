package com.casper.sdk.model.clvalue;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeWithChildren;
import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeFixedList;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.exception.BufferEndCLValueDecodeException;
import com.casper.sdk.exception.CLValueDecodeException;
import com.casper.sdk.exception.CLValueEncodeException;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.model.clvalue.encdec.CLValueDecoder;
import com.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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
    public void encode(CLValueEncoder clve, boolean encodeType) throws IOException, NoSuchTypeException, CLValueEncodeException {
        setListType();

        setBytes("");
        for (AbstractCLValue<?, ?> child : getValue()) {
            child.encode(clve, false);
            setBytes(getBytes() + child.getBytes());
        }
        if (encodeType) {
            this.encodeType(clve);
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
