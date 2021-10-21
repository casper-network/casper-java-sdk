package com.syntifi.casper.sdk.model.deploy.transform;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.syntifi.casper.sdk.model.bid.Bid;

import lombok.Data;

/**
 * An implmentation of Transform that Writes the given Bid to global state.
 * @see Transform
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@JsonTypeName("WriteBid")
public class WriteBid implements Transform {
   
    /**
     * @see Bid 
     */
    @JsonProperty("WriteBid")
    private Bid bid;
}



