package com.casper.sdk.model.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author ian@meywood.com
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class RpcResult {

    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("protocol_version")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String protocolVersion;
}
