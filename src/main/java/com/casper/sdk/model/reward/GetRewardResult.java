package com.casper.sdk.model.reward;

import com.casper.sdk.model.common.RpcResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * Result for "info_get_reward" RPC response.
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GetRewardResult extends RpcResult {
    @JsonProperty("reward_amount")
    private BigInteger rewardAmount;
    @JsonProperty("era_id")
    private long eraId;
}

