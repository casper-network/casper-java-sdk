package com.casper.sdk.model.transaction;

import com.casper.sdk.model.common.RpcResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The result of an info_get_transaction RPC call
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetTransactionResult extends RpcResult {

    @JsonProperty("transaction")
    private Transaction transaction;

    @JsonProperty("execution_info")
    private ExecutionInfo executionInfo;
}
