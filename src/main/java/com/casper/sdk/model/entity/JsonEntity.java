package com.casper.sdk.model.entity;

import com.casper.sdk.model.common.RpcResult;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JsonEntity extends RpcResult {

    @JsonProperty("entity")
    private StateEntity entity;

    @JsonProperty("merkle_proof")
    private String merkleProof;

}
