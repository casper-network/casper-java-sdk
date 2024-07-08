package com.casper.sdk.model.event.transaction;

import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.transaction.TransactionHash;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The given transaction has expired.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("TransactionExpired")
public class TransactionExpired implements EventData {
    @JsonProperty("transaction_hash")
    private TransactionHash transactionHash;
}

