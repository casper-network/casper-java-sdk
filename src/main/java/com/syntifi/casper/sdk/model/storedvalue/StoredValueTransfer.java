package com.syntifi.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.transfer.Transfer;

import lombok.Data;

/**
 * Stored Value for {@link Transfer}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Data
@JsonTypeName("Transfer")
public class StoredValueTransfer implements StoredValue<Transfer> {
    @JsonProperty("Transfer")
    public Transfer value;
}
