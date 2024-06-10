package com.casper.sdk.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The response for a V2 chain_get_block RPC request.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChainGetBlockResponse {

    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("block_with_signatures")
    private BlockWithSignatures blockWithSignatures;
}
