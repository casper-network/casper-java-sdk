package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import lombok.*;

/**
 * CLType for {@link AbstractCLType#MAP}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see AbstractCLType
 * @since 0.0.1
 */
@Getter
@EqualsAndHashCode(callSuper = false, of = {"typeName", "keyValueTypes"})
public class CLTypeMap extends AbstractCLTypeWithChildren {
    private final String typeName = AbstractCLType.MAP;

    @JsonProperty(MAP)
    public void setKeyValueTypes(CLTypeMapEntryType keyValueTypes) {
        this.keyValueTypes = keyValueTypes;
        getChildTypes().add(this.keyValueTypes.getKeyType());
        getChildTypes().add(this.keyValueTypes.getValueType());
    }

    private CLTypeMapEntryType keyValueTypes;

    @Override
    public boolean isDeserializable() {

        if (getKeyValueTypes().keyType instanceof CLTypeAny || getKeyValueTypes().valueType instanceof CLTypeAny) {
            // The map contains an 'Any' type therefore cannot be deserialized
            return false;
        } else if (getKeyValueTypes().valueType instanceof AbstractCLTypeWithChildren) {
           return getChildTypes().stream().allMatch(childType -> {
                if (childType instanceof CLTypeAny) {
                    return false;
                } else {
                    return childType.isDeserializable();
                }
            });
        } else {
            return getKeyValueTypes().keyType.isDeserializable() || getKeyValueTypes().valueType.isDeserializable();
        }
    }

    /**
     * Support class for {@link AbstractCLType#MAP} entry types
     *
     * @author Alexandre Carvalho
     * @author Andre Bertolace
     * @see AbstractCLType
     * @since 0.0.1
     */
    @Getter
    @Setter
    @EqualsAndHashCode(callSuper = false, of = {"keyType", "valueType"})
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CLTypeMapEntryType {
        @JsonIgnore
        private AbstractCLType keyType;
        @JsonIgnore
        private AbstractCLType valueType;

        @JsonSetter("key")
        @ExcludeFromJacocoGeneratedReport
        protected void setJsonKey(AbstractCLType clType) {
            this.keyType = clType;
        }

        @JsonGetter("key")
        @ExcludeFromJacocoGeneratedReport
        protected Object getJsonKey() {
            if (this.keyType instanceof AbstractCLTypeBasic) {
                return this.keyType.getTypeName();
            } else {
                return this.keyType;
            }
        }

        @JsonSetter("value")
        @ExcludeFromJacocoGeneratedReport
        protected void setJsonValue(AbstractCLType clType) {
            this.valueType = clType;
        }

        @JsonGetter("value")
        @ExcludeFromJacocoGeneratedReport
        protected Object getJsonValue() {
            if (this.valueType instanceof AbstractCLTypeBasic) {
                return this.valueType.getTypeName();
            } else {
                return this.valueType;
            }
        }
    }
}
