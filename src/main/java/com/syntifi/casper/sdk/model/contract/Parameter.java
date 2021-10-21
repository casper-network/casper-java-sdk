package com.syntifi.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLTypeBasic;

import lombok.Data;

/**
 * Parameter to a method
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class Parameter {

    /**
     * cl_type(CLType) The value of the entry: a casper `Key` type.
     */
    @JsonIgnore
    private AbstractCLType clType;

    /**
     * name(String) The name of the entry.
     */
    @JsonProperty("name")
    private String name;

    @JsonSetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonClType(AbstractCLType clType) {
        this.clType = clType;
    }

    /**
     * The accessor for jackson serialization
     * 
     * @return String if cl_type is basic type, CLType object if not.
     */
    @JsonGetter("cl_type")
    @ExcludeFromJacocoGeneratedReport
    protected Object getJsonClType() {
        if (this.clType instanceof AbstractCLTypeBasic) {
            return this.clType.getTypeName();
        } else {
            return this.clType;
        }
    }
}
