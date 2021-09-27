package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.syntifi.casper.sdk.exception.CLValueDecodeException;
import com.syntifi.casper.sdk.exception.CLValueEncodeException;
import com.syntifi.casper.sdk.exception.DynamicInstanceException;
import com.syntifi.casper.sdk.jackson.deserializer.CLValueOptionDeserializer;
import com.syntifi.casper.sdk.jackson.serializer.CLValueOptionSerializer;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueDecoder;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.encdec.CLValueEncoder;

/**
 * Casper Option CLValue implementation
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLValue
 * @since 0.0.1
 */
@JsonSerialize(using = CLValueOptionSerializer.class)
@JsonDeserialize(using = CLValueOptionDeserializer.class)
public class CLValueOption extends AbstractCLValue<Optional<? extends AbstractCLValue<?>>> {

    public CLValueOption() {
        this(Optional.ofNullable(null));
    }

    public CLValueOption(Optional<? extends AbstractCLValue<?>> value) {
        super(value, new CLType(CLTypeData.OPTION,
                value.isPresent() && value.get() == null ? null : getCLTypeDataOfChildren(value)));
    }

    static List<CLType> getCLTypeDataOfChildren(Optional<? extends AbstractCLValue<?>> value) {
        if (value.isPresent()) {
            return Arrays.asList(value.get().getClType());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public void encode(CLValueEncoder clve) throws IOException, CLValueEncodeException, DynamicInstanceException {
        CLValueBool isPresent = new CLValueBool(getValue().isPresent());
        clve.writeBool(isPresent);
        setBytes(isPresent.getBytes());

        Optional<? extends AbstractCLValue<?>> child = getValue();
        if (child.isPresent()) {
            child.get().encode(clve);
            setBytes(getBytes() + child.get().getBytes());
        }
    }

    @Override
    public void decode(CLValueDecoder clvd) throws IOException, CLValueDecodeException, DynamicInstanceException {
        CLValueBool isPresent = new CLValueBool();
        isPresent.decode(clvd);
        setBytes(isPresent.getBytes());

        if (Boolean.TRUE.equals(isPresent.getValue())) {
            CLType childTypeData = getClType().getChildTypes().get(0);

            AbstractCLValue<?> child = CLTypeData.createCLValueFromCLTypeData(childTypeData.getClTypeData());
            child.setClType(childTypeData);
            child.decode(clvd);

            setValue(Optional.of(child));
            setBytes(getBytes() + child.getBytes());
        } else {
            setValue(Optional.ofNullable(null));
        }
    }
}
