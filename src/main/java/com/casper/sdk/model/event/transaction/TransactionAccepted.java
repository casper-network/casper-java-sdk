package com.casper.sdk.model.event.transaction;

import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.transaction.target.Transaction;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
@NoArgsConstructor
@JsonTypeName("TransactionAccepted")
public class TransactionAccepted implements EventData {

    private Transaction transaction;

    @JsonCreator
    public TransactionAccepted(final Transaction transaction) {
        this.transaction = transaction;
    }
}
