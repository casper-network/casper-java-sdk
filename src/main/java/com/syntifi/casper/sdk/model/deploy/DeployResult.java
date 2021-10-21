package com.syntifi.casper.sdk.model.deploy;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Result for the account_put_deploy RPC response
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class DeployResult {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * The deploy hash
     */
    @JsonProperty("deploy_hash")
    private String deployHash;

}


