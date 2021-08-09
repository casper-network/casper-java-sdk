package com.syntifi.casper.sdk.model.era;

import lombok.Data;

/**
 * Casper block era reward data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CasperEraReport
 * @since 0.0.1
 */
@Data
public class CasperReward {

    private String validator;
    
    private long amount;
}
