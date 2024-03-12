package com.casper.sdk.model.clvalue.cltype;

import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import dev.oak3.sbs4j.DeserializerBuffer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueDeserializationException;
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
    public void setKeyValueTypes(final CLTypeMapEntryType keyValueTypes) {
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
        protected void setJsonKey(final AbstractCLType clType) {
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
        protected void setJsonValue(final AbstractCLType clType) {
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

    @Override
    public void serializeChildTypes(final SerializerBuffer ser) throws NoSuchTypeException {
        getKeyValueTypes().getKeyType().serialize(ser);
        getKeyValueTypes().getValueType().serialize(ser);
    }

    @Override
    public void deserializeChildTypes(final DeserializerBuffer deser) throws ValueDeserializationException, NoSuchTypeException, DynamicInstanceException {

        // read child types
        final int keyTypeTag = deser.readU8();
        final int valueTypeTag = deser.readU8();

        final CLTypeData keyType = CLTypeData.getTypeBySerializationTag((byte) keyTypeTag);
        final CLTypeData valueType = CLTypeData.getTypeBySerializationTag((byte) valueTypeTag);
        final AbstractCLType clTypeKey = CLTypeData.createCLTypeFromCLTypeName(keyType.getClTypeName());
        final AbstractCLType clTypeValue = CLTypeData.createCLTypeFromCLTypeName(valueType.getClTypeName());

        if (clTypeValue instanceof AbstractCLTypeWithChildren) {
            ((AbstractCLTypeWithChildren) clTypeValue).deserializeChildTypes(deser);
        }

        setKeyValueTypes(new CLTypeMap.CLTypeMapEntryType(clTypeKey, clTypeValue));
    }
}
