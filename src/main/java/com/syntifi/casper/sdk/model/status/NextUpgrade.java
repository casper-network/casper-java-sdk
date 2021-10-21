package com.syntifi.casper.sdk.model.status;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Information about the next scheduled upgrade
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class NextUpgrade {

    /**
     * @see ActivationPoint
     */
    @JsonProperty("activation_point")
    private ActivationPoint activationPoint;

    /**
     * Protocol version 
     */
    @JsonProperty("protocol_version")
    private String protocolVersion;
}