package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.clvalue.AbstractCLValue;
import com.syntifi.casper.sdk.model.clvalue.cltype.AbstractCLType;

import lombok.Data;

/**
 * Stored Value for {@link AbstractCLType}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Data
@JsonTypeName("CLValue")
public class StoredValueCLValue implements StoredValue<AbstractCLValue<?, ?>> {
    @JsonProperty("CLValue")
    private AbstractCLValue<?, ?> value;
}
