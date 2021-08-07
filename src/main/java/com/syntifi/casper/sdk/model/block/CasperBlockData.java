package com.syntifi.casper.sdk.model.block;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@Data
public class CasperBlockData {
    @JsonProperty("api_version")
    private String apiVersion;

    private CasperBlock block;
}
