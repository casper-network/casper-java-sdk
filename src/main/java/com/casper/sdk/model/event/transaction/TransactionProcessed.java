package com.casper.sdk.model.event.transaction;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.transaction.InitiatorAddr;
import com.casper.sdk.model.transaction.TransactionHash;
import com.casper.sdk.model.transaction.execution.ExecutionResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * The given transaction has been executed, committed and forms part of the given block.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("TransactionProcessed")
public class TransactionProcessed implements EventData {
    @JsonProperty("transaction_hash")
    private TransactionHash transactionHash;
    @JsonProperty("initiator_addr")
    private InitiatorAddr<?> initiatorAddr;
    private Date timestamp;
    private Ttl ttl;
    @JsonProperty("block_hash")
    private Digest blockHash;
    @JsonProperty("execution_result")
    private ExecutionResult executionResult;
    private List<Message> messages;

    public <T extends InitiatorAddr<?>> T getInitiatorAddr() {
        //noinspection unchecked
        return (T) initiatorAddr;
    }
}
