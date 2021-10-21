package com.syntifi.casper.sdk.model.deploy;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Auction metadata.  Intended to be recorded at each era.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class EraInfo {
    
    /**
     * @see SeigniorageAllocation
     */
    @JsonProperty("seigniorage_allocations")
    private List<SeigniorageAllocation> seigniorageAllocations;
}
