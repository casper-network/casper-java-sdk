package com.syntifi.casper.sdk.model.era;

import lombok.Data;

/**
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@Data
public class CasperReward {
    private String validator;
    
    private long amount;
}
