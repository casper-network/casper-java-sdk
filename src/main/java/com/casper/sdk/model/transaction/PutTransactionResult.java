package com.casper.sdk.model.transaction;

import com.casper.sdk.model.common.RpcResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Result for "account_put_transaction" RPC response.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PutTransactionResult extends RpcResult {

    @JsonProperty("transaction_hash")
    private TransactionHash transactionHash;
}
