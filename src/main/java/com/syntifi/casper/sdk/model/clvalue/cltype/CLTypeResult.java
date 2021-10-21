package com.syntifi.casper.sdk.model.clvalue.cltype;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CLType for {@link AbstractCLType.RESULT}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@EqualsAndHashCode(callSuper = false, of = { "typeName", "okErrTypes" })
public class CLTypeResult extends AbstractCLType {

    /**
     * Support class for {@link AbstractCLType.RESULT} ok/err types
     * 
     * @author Alexandre Carvalho
     * @author Andre Bertolace
     * @see AbstractCLType
     * @since 0.0.1
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class CLTypeResultOkErrTypes {
        @JsonIgnore
        private AbstractCLType okClType;
        @JsonIgnore
        private AbstractCLType errClType;

        @JsonSetter("ok")
        @ExcludeFromJacocoGeneratedReport
        protected void setJsonKey(AbstractCLType clType) {
            this.okClType = clType;
        }

        @JsonGetter("ok")
        @ExcludeFromJacocoGeneratedReport
        protected Object getJsonKey() {
            if (this.okClType instanceof AbstractCLTypeBasic) {
                return this.okClType.getTypeName();
            } else {
                return this.okClType;
            }
        }

        @JsonSetter("err")
        @ExcludeFromJacocoGeneratedReport
        protected void setJsonValue(AbstractCLType clType) {
            this.errClType = clType;
        }

        @JsonGetter("err")
        @ExcludeFromJacocoGeneratedReport
        protected Object getJsonValue() {
            if (this.errClType instanceof AbstractCLTypeBasic) {
                return this.errClType.getTypeName();
            } else {
                return this.errClType;
            }
        }
    }

    private final String typeName = AbstractCLType.RESULT;

    @Setter
    @JsonProperty(AbstractCLType.RESULT)
    private CLTypeResultOkErrTypes okErrTypes;
}
