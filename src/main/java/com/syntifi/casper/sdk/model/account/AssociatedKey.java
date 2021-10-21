package com.syntifi.casper.sdk.model.account;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Associated Key
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class AssociatedKey {
    
    /**
     * account_hash(String) Hex-encoded account hash.
     */
    @JsonProperty("account_hash")
    private String accountHash;

    /**
     * weight(Integer)
     */
    @JsonProperty("weight")
    private int weight;
}
