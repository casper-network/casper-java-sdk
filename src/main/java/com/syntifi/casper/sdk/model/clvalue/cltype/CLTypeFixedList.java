package com.syntifi.casper.sdk.model.clvalue.cltype;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * CLType for {@link AbstractCLType.FIXED_LIST}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@EqualsAndHashCode(callSuper = false, of = { "typeName", "listType" })
public class CLTypeFixedList extends AbstractCLType {
    private final String typeName = AbstractCLType.FIXED_LIST;

    @Setter
    @JsonIgnore
    private AbstractCLType listType;

    @JsonSetter(AbstractCLType.FIXED_LIST)
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonValue(AbstractCLType clType) {
        this.listType = clType;
    }

    @JsonGetter(AbstractCLType.FIXED_LIST)
    @ExcludeFromJacocoGeneratedReport
    protected Object getJsonValue() {
        if (this.listType instanceof AbstractCLTypeBasic) {
            return this.listType.getTypeName();
        } else {
            return this.listType;
        }
    }
}
