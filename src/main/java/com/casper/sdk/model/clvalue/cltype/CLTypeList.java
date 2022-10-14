package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * CLType for {@link AbstractCLType#LIST}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@EqualsAndHashCode(callSuper = false, of = {"typeName", "listType"})
public class CLTypeList extends AbstractCLType {
    private final String typeName = AbstractCLType.LIST;

    @Setter
    @JsonIgnore
    private AbstractCLType listType;

    @JsonSetter(AbstractCLType.LIST)
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonValue(AbstractCLType clType) {
        this.listType = clType;
    }

    @JsonGetter(AbstractCLType.LIST)
    @ExcludeFromJacocoGeneratedReport
    protected Object getJsonValue() {
        if (this.listType instanceof AbstractCLTypeBasic) {
            return this.listType.getTypeName();
        } else {
            return this.listType;
        }
    }
}
