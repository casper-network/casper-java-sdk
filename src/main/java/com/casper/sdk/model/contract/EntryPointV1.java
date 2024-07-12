package com.casper.sdk.model.contract;

import com.fasterxml.jackson.annotation.*;
import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.model.clvalue.cltype.AbstractCLType;
import com.casper.sdk.model.clvalue.cltype.AbstractCLTypeBasic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Entry points to be executed against the V1 Casper VM.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntryPointV1 implements EntryPoint {

    public interface EntryPointAccess {
        Object getValue();
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
    public static class EntryPointAccessList implements EntryPointAccess {

        @Getter
        private List<String> groups;

        @Override
        public List<String> getValue() {
            return this.groups;
        }
    }

    /** Context of method execution */
    public enum EntryPointType {
        @JsonProperty("Session")
        SESSION,
        @JsonProperty("Contract")
        CONTRACT,
        // Runs using the called entity's context.
        @JsonProperty("Called")
        CALLED,
        // Runs using the calling entity's context.
        @JsonProperty("Caller")
        CALLER,
        @JsonProperty("Factory")
        FACTORY
    }

    /** An enum specifying who pays for the invocation and execution of the entrypoint. */
    public enum EntryPointPayment {
        // Will cover cost to execute self but not cost of any subsequent invoked contracts
        @JsonProperty("SelfOnly")
        SELF_ONLY,
        // The caller must cover cost
        @JsonProperty("Caller")
        CALLER,
        // Will cover cost to execute self and the cost of any subsequent invoked contracts
        @JsonProperty("SelfOnward")
        SELF_ONWARD
    }

    /** the {@link EntryPointAccess} */
    @JsonIgnore
    private EntryPointAccess access;

    /** a list of {@link Parameter} */
    private List<Parameter> args;

    /** the {@link EntryPointType} Context of method execution */
    @JsonProperty("entry_point_type")
    private EntryPointType type;

    @JsonProperty("entry_point_payment")
    private EntryPointPayment payment;

    /** the name */
    private String name;

    /** the return as {@link AbstractCLType} */
    @JsonIgnore
    private AbstractCLType ret;

    /**
     * Accessor for jackson serialization
     *
     * @return String if access is enum, List<String> if is list.
     */
    @JsonGetter("access")
    private Object getJsonAccess() {
        return this.access.getValue();
    }

    /**
     * Accessor for jackson serialization
     *
     * @param access the access type of entry point
     */
    @JsonSetter("access")
    @SuppressWarnings("unchecked")
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
     * @param clType the cltype for ret
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
