package com.syntifi.casper.sdk.model.contract;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLTypeBasic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * No description available
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class EntryPoint {

    public interface EntryPointAccess {
        public Object getValue();
    }

    public enum EntryPointAccessEnum implements EntryPointAccess {
        @JsonProperty("Public")
        PUBLIC;

        @Override
        public EntryPointAccessEnum getValue() {
            return this;
        }
    }

    @AllArgsConstructor
    public class EntryPointAccessList implements EntryPointAccess {

        @Getter
        private List<String> groups = new ArrayList<>();

        @Override
        public List<String> getValue() {
            return this.groups;
        }
    }

    public enum EntryPointType {
        @JsonProperty("Session")
        SESSION, @JsonProperty("Contract")
        CONTRACT;
    }

    /**
     * access(enum/List<String>) -
     */
    @JsonIgnore
    private EntryPointAccess access;

    /**
     * args(Array/Object) - Parameter to a method
     */
    @JsonProperty("args")
    private List<Parameter> args;

    /**
     * entry_point_type(enum/String) - Context of method execution
     */
    @JsonProperty("entry_point_type")
    private EntryPointType type;

    /**
     * name(String)
     */
    @JsonProperty("name")
    private String name;

    /**
     * ret({@link AbstractCLType})
     */
    @JsonIgnore
    private AbstractCLType ret;

    /**
     * Accessor for jackson serialization
     * 
     * @return String if access is enum, List<String> if list.
     */
    @JsonGetter("access")
    private Object getJsonAccess() {
        return this.access.getValue();
    }

    /**
     * Accessor for jackson serialization
     * 
     * @param access
     */
    @JsonSetter("access")
    private void setJsonAccess(Object access) {
        if (access instanceof String) {
            this.access = EntryPointAccessEnum.PUBLIC;
        } else if (access instanceof List) {
            this.access = new EntryPointAccessList((ArrayList<String>) access);
        }
    }

    /**
     * Accessor for jackson serialization
     * 
     * @param clType
     */
    @JsonSetter("ret")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonRet(AbstractCLType clType) {
        this.ret = clType;
    }

    /**
     * Accessor for jackson serialization
     * 
     * @return String if cl_type is basic type, CLType object if not.
     */
    @JsonGetter("ret")
    @ExcludeFromJacocoGeneratedReport
    protected Object getJsonRet() {
        if (this.ret instanceof AbstractCLTypeBasic) {
            return this.ret.getTypeName();
        } else {
            return this.ret;
        }
    }
}
