package com.casper.sdk.model.transaction;

import com.casper.sdk.model.deploy.NamedArg;
import com.casper.sdk.model.transaction.scheduling.TransactionScheduling;
import com.casper.sdk.model.transaction.target.TransactionTarget;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The body of a transaction.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionV1Body {
    private List<NamedArg<?>> args;
    private TransactionTarget target;
    @JsonProperty("entry_point")
    private String entryPoint;
    @JsonProperty("transaction_category")
    private TransactionCategory transactionCategory;
    private TransactionScheduling scheduling;
}
