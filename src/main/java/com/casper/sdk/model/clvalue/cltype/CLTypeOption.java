package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * CLType for {@link AbstractCLType#OPTION}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, of = {"typeName"})
public class CLTypeOption extends AbstractCLTypeWithChildren {
    private final String typeName = OPTION;

    @JsonGetter(OPTION)
    @ExcludeFromJacocoGeneratedReport
    protected Object getJsonClType() {
        if (getOptionType() instanceof AbstractCLTypeBasic) {
            return getOptionType().getTypeName();
        } else {
            return getOptionType();
        }
    }

    @JsonSetter(OPTION)
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonClType(AbstractCLType clType) {
        getChildTypes().add(clType);
    }

    @JsonIgnore
    public AbstractCLType getOptionType() {
        if (!getChildTypes().isEmpty()) {
            return getChildTypes().get(0);
        }

        return null;
    }

    @JsonIgnore
    public void setOptionType(AbstractCLType listType) {
        getChildTypes().clear();
        getChildTypes().add(listType);
    }

    @Override
    public boolean isUndeserializable() {
        return getOptionType().isUndeserializable() || getChildTypes().stream().anyMatch(AbstractCLType::isUndeserializable);
    }
}
