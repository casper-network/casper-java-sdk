package com.syntifi.casper.sdk.model.storedvalue;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.transfer.Withdraw;

import lombok.Data;

/**
 * Stored Value for {@link Withdraw}
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Data
@JsonTypeName("Withdraw")
public class StoredValueWithdraw implements StoredValue<List<Withdraw>> {
    @JsonProperty("Withdraw")
    public List<Withdraw> value;
}
