package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
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
    protected void setJsonValue(AbstractCLType clType) {
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
    public void setListType(AbstractCLType listType) {
        getChildTypes().clear();
        getChildTypes().add(listType);
    }

    @Override
    public boolean isUndeserializable() {

        return getChildTypes().stream().anyMatch(childType -> {
            if (childType instanceof CLTypeAny) {
                return true;
            } else {
                return childType.isUndeserializable();
            }
        });
    }
}
