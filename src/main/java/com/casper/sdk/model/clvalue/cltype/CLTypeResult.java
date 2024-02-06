package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

/**
 * CLType for {@link AbstractCLType#RESULT}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@EqualsAndHashCode(callSuper = false, of = {"typeName", "okErrTypes"})
public class CLTypeResult extends AbstractCLType {

    private final String typeName = RESULT;

    @Setter
    @JsonProperty(RESULT)
    private CLTypeResultOkErrTypes okErrTypes;

    /**
     * Support class for {@link AbstractCLType#RESULT} ok/err types
     *
     * @author Alexandre Carvalho
     * @author Andre Bertolace
     * @see AbstractCLType
     * @since 0.0.1
     */
    @Getter
    @Setter
    @EqualsAndHashCode
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CLTypeResultOkErrTypes {
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

    @Override
    public boolean isDeserializable() {
        return true;
    }
}
