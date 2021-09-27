package com.syntifi.casper.sdk.model.storedvalue.clvalue;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeResolver;
import com.syntifi.casper.sdk.jackson.CLTypeResolver;
import com.syntifi.casper.sdk.model.interfaces.DecodableValue;
import com.syntifi.casper.sdk.model.interfaces.EncodableValue;
import com.syntifi.casper.sdk.model.storedvalue.StoredValue;

import lombok.Data;

/**
 * Casper CLValue A Casper value, i.e. a value which can be stored and
 * manipulated by smart contracts. It holds the underlying data as a
 * type-erased, serialized `Vec<u8>` and also holds the CLType of the underlying
 * data as a separate member. The `parsed` field, representing the original
 * value, is a convenience only available when a CLValue is encoded to JSON, and
 * can always be set to null if preferred."
 * 
 * This abstract class resolves the types by the 'cl_type' json property.
 * 
 * All types must implement this class and be listed in the JsonSubTypes
 * annotation.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
@JsonTypeResolver(CLTypeResolver.class)
public abstract class AbstractCLValue<T> implements EncodableValue, DecodableValue {

    protected AbstractCLValue(T value, CLType clType) {
        this.value = value;
        this.clType = clType;
    }

    /**
     * data structure that holds CLType of this value and its children
     */
    private CLType clType;

    /**
     * encoded string from the hex-encoded bytes
     */
    private String bytes;

    // TODO: Is this parsed value always present? What's its use?
    private Object parsed;

    /**
     * the actual object decoded and parsed value
     */
    @JsonIgnore
    private T value;
}
