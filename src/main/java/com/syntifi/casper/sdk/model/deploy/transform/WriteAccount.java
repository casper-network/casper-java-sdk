package com.syntifi.casper.sdk.model.deploy.transform;
    
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import lombok.Data;

/**
 * An implmentation of Transform that Writes the given Account to global state.
 * @see Transform
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@JsonTypeName("WriteAccount")
public class WriteAccount implements Transform {
   
    /**
     * Hex-encoded account hash 
     */
    @JsonProperty("WriteAccount")
    private String account;
}


