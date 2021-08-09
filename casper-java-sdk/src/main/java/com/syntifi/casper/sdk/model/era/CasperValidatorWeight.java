package com.syntifi.casper.sdk.model.era;

import lombok.Data;

/**
 * Casper block validator weight
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CasperEra
 * @since 0.0.1
 */
@Data
public class CasperValidatorWeight {

    private String validator;
    
    private long weight;
}
