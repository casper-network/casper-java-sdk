package com.syntifi.casper.sdk.model.era;

import java.math.BigInteger;

import com.syntifi.casper.sdk.model.key.PublicKey;

import lombok.Data;

/**
 * Casper block validator weight
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see JsonEraEnd
 * @since 0.0.1
 */
@Data
public class JsonValidatorWeight {

    /**
     * @see PublicKey
     */
    private PublicKey validator;

    /**
     * Decimal representation of a 512-bit integer 
     */ 
    private BigInteger weight;
}
