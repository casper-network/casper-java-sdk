package com.casper.sdk.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Root class for a Casper block request
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JsonBlockData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * @see JsonBlock
     */
    @JsonProperty("block")
    private JsonBlock block;
}
