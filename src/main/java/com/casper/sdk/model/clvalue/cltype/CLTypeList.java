package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * CLType for {@link AbstractCLType#LIST}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@EqualsAndHashCode(callSuper = true, of = {"typeName"})
public class CLTypeList extends AbstractCLTypeWithChildren {
    private final String typeName = AbstractCLType.LIST;

    @JsonGetter(AbstractCLType.LIST)
    @ExcludeFromJacocoGeneratedReport
    protected Object getJsonValue() {
        if (getListType() instanceof AbstractCLTypeBasic) {
            return getListType().getTypeName();
        } else {
            return getListType();
        }
    }

    @JsonSetter(AbstractCLType.LIST)
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonValue(final AbstractCLType clType) {
        getChildTypes().add(clType);
    }

    @JsonIgnore
    public AbstractCLType getListType() {
        if (!getChildTypes().isEmpty()) {
            return getChildTypes().get(0);
        }

        return null;
    }

    @JsonIgnore
    public void setListType(final AbstractCLType listType) {
        getChildTypes().clear();
        getChildTypes().add(listType);
    }

    @Override
    public boolean isDeserializable() {

        return getChildTypes().stream().allMatch(childType -> {
            if (childType instanceof CLTypeAny) {
                return false;
            } else {
                return childType.isDeserializable();
            }
        });
    }

    @Override
    public void deserializeChildTypes(DeserializerBuffer deser) throws ValueDeserializationException, NoSuchTypeException, DynamicInstanceException {
        // FIXME: 2021/10/20
    }
}
