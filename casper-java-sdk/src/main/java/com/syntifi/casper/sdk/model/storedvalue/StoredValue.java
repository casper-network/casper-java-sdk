package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.storedvalue.clvalue.AbstractCLValue;

import lombok.Data;

/**
 * A Casper value, i.e. a value which can be stored and manipulated by 
 * smart contracts.\n\nIt holds the underlying data as a type-erased, 
 * serialized `Vec<u8>` and also holds the CLType of the underlying 
 * data as a separate member.\n\nThe `parsed` field, representing the 
 * original value, is a convenience only available when a CLValue is 
 * encoded to JSON, and can always be set to null if preferred."
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValueData
 * @since 0.0.1
 */
@Data
public class StoredValue {

    @JsonProperty("CLValue")
    private AbstractCLValue<?> clValue;
}
