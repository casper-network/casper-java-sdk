package com.syntifi.casper.sdk.model.era;

import com.syntifi.casper.sdk.model.key.PublicKey;

import lombok.Data;

/**
 * Casper block era reward data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonEraReport
 * @since 0.0.1
 */
@Data
public class Reward {
    
    /**
     * @see PublicKey
     */
    private PublicKey validator;
   
    /**
     * amount
     */
    private long amount;
}
