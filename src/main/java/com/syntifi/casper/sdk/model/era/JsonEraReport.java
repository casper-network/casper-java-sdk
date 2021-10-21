package com.syntifi.casper.sdk.model.era;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.model.key.PublicKey;

import lombok.Data;

/**
 * Equivocation and reward information to be included in the terminal block.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonEraEnd
 * @since 0.0.1
 */
@Data
public class JsonEraReport {
    
    /**
     * List of @see PublicKey 
     */
    @JsonProperty("inactive_validators")
    private List<PublicKey> inactiveValidators;

    /**
     * List of @see PublicKey 
     */
    private List<PublicKey> equivocators;
   
    /**
     * List of @see Reward 
     */
    private List<Reward> rewards;
}
