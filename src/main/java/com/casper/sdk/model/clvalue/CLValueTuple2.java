package com.casper.sdk.model.clvalue;

import com.casper.sdk.model.clvalue.cltype.CLTypeData;
import com.casper.sdk.model.clvalue.cltype.CLTypeTuple2;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.util.encoders.Hex;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Casper Tuple2 CLValue implementation
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
public class CLValueTuple2
        extends AbstractCLValueWithChildren<Pair<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>>, CLTypeTuple2> {

    @JsonProperty("cl_type")
    private CLTypeTuple2 clType = new CLTypeTuple2();

    @JsonCreator
    public CLValueTuple2(@JsonProperty("cl_type") final CLTypeTuple2 clType,
                         @JsonProperty("bytes") final String bytes,
                         @JsonProperty("parsed") final Object parsed) {
        setBytes(bytes);
        setClType(clType);
        setParsed(parsed);
    }

    public CLValueTuple2(Pair<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) throws ValueSerializationException {
        setChildTypes(value);
        this.setValue(value);
    }

    public void setClType(CLTypeTuple2 clType) {
        this.clType = clType;
        childTypesSet();
    }

    @Override
    protected void serializeValue(final SerializerBuffer ser) throws ValueSerializationException {
        final SerializerBuffer serVal = new SerializerBuffer();
        setChildTypes(this.getValue());
        getValue().getValue0().serialize(serVal);
        getValue().getValue1().serialize(serVal);
        final byte[] bytes = serVal.toByteArray();
        ser.writeByteArray(bytes);
        this.setBytes(Hex.toHexString(bytes));

    }

    @Override
    public void deserializeCustom(final DeserializerBuffer deser) throws Exception {
        final CLTypeData childTypeData1 = clType.getChildClTypeData(0);
        final CLTypeData childTypeData2 = clType.getChildClTypeData(1);

        final AbstractCLValue<?, ?> child1 = CLTypeData.createCLValueFromCLTypeData(childTypeData1);
        populateChildTypesFromParent(child1, clType.getChildTypes().get(0));
        child1.deserializeCustom(deser);

        final AbstractCLValue<?, ?> child2 = CLTypeData.createCLValueFromCLTypeData(childTypeData2);
        populateChildTypesFromParent(child2, clType.getChildTypes().get(1));
        child2.deserializeCustom(deser);

        setValue(new Pair<>(child1, child2));
    }

    @Override
    protected void setChildTypes(final Pair<? extends AbstractCLValue<?, ?>, ? extends AbstractCLValue<?, ?>> value) {
        if (value.getValue0() != null && value.getValue1() != null) {
            clType.setChildTypes(Arrays.asList(value.getValue0().getClType(), value.getValue1().getClType()));
        } else {
            clType.setChildTypes(new ArrayList<>());
        }
    }

    @Override
    public String toString() {
        return getValue() != null ? getValue().toString() : null;
    }
}
