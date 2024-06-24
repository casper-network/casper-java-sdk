package com.casper.sdk.model.transaction;

import com.casper.sdk.model.transfer.Transfer;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

/**
 * The result of executing a single transaction V2.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExecutionResultV2 extends ExecutionResult {

    @SuppressWarnings("rawtypes")
    @JsonProperty("initiator")
    private InitiatorAddr initiator;
    /** If error_message is null, the execution was successful */
    @JsonProperty("error_message")
    private String errorMessage;
    @JsonProperty("limit")
    private BigInteger limit;
    @JsonProperty("consumed")
    private BigInteger consumed;
    @JsonProperty("cost")
    private BigInteger cost;
    @JsonProperty("size_estimate")
    private long sizeEstimate;
    @JsonProperty("payment")
    private List<PaymentInfo> payment;
    @JsonProperty("transfers")
    private List<Transfer> transfers;
    @JsonProperty("effects")
    private List<Effect> effects;

    public <T> InitiatorAddr<T> getInitiator() {
        //noinspection unchecked
        return initiator;
    }
}
