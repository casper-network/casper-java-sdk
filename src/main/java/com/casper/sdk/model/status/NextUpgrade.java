package com.casper.sdk.model.status;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Information about the next scheduled upgrade
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
public class NextUpgrade {

    /**
     * @see ActivationPoint
     */
    @JsonProperty("activation_point")
    //private ActivationPoint activationPoint;
    // TODO: the response from the JSON-RPC is different from that specified in the RPC-JSON documentation.
    // Here it is adapted to comply with the API response
    private Integer activationPoint;

    /**
     * Protocol version
     */
    @JsonProperty("protocol_version")
    private String protocolVersion;
}