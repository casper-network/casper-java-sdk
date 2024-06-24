package com.casper.sdk.model.storedvalue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.casper.sdk.model.transfer.TransferV1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Stored Value for {@link TransferV1}
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredValue
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonTypeName("Transfer")
public class StoredValueTransfer implements StoredValue<TransferV1> {
    @JsonProperty("Transfer")
    public TransferV1 value;
}
