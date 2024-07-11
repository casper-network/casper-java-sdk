package com.casper.sdk.model.entity;

import com.casper.sdk.model.common.RpcResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * An addressable entity or a legacy account.
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StateEntityResult extends RpcResult {

    @JsonProperty("entity")
    private StateEntity entity;

    @JsonProperty("merkle_proof")
    private String merkleProof;

}
