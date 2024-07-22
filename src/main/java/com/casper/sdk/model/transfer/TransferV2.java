package com.casper.sdk.model.transfer;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.transaction.InitiatorAddr;
import com.casper.sdk.model.transaction.TransactionHash;
import com.casper.sdk.model.uref.URef;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Represents a version 2 transfer from one purse to another.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransferV2 {

    /** Transaction that created the transfer. */
    @JsonProperty("transaction_hash")
    private TransactionHash transactionHash;
    /** Entity from which transfer was executed. */
    @JsonProperty("from")
    private InitiatorAddr from;
    /** Account to which funds are transferred. */
    @JsonProperty("to")
    private Digest to;
    /** Source purse. */
    @JsonProperty("source")
    private URef source;
    /** Target purse. */
    @JsonProperty("target")
    private URef target;
    /** Amount. */
    @JsonProperty("amount")
    private BigInteger amount;
    /** Gas. */
    @JsonProperty("gas")
    private int gas;
    /** User-defined ID. */
    @JsonProperty("id")
    private BigInteger id;

    public <T> InitiatorAddr<T> getFrom() {
        //noinspection unchecked
        return from;
    }
}
