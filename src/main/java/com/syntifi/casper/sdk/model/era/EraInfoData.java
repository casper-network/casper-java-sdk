package com.syntifi.casper.sdk.model.era;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * An EraInfo from the network
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EraInfoData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * The auction state
     */
    @JsonProperty("era_summary")
    @JsonInclude(value = Include.NON_NULL)
    private EraSummary eraSummary;
}
