package com.syntifi.casper.sdk.model.account;

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
public class AccountData {

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * @see Account
     */
    private Account account;

    /**
     * The merkle proof 
     */
    @JsonProperty("merkle_proof")
    private String merkelProof;
}

