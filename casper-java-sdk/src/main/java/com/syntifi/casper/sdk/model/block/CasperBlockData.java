package com.syntifi.casper.sdk.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Root class for a Casper block request
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class CasperBlockData {

    @JsonProperty("api_version")
    private String apiVersion;

    @JsonProperty("block")
    private CasperBlock block;
}
