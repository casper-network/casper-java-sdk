package com.casper.sdk.model.event.transaction;

import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.transaction.Transaction;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The given transaction has been newly-accepted by this node.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("TransactionAccepted")
public class TransactionAccepted implements EventData {
    private Transaction transaction;
}
