package com.syntifi.casper.sdk.model.deploy;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Auction metadata. Intended to be recorded at each era.
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EraInfo {

    /**
     * @see SeigniorageAllocation
     */
    @JsonProperty("seigniorage_allocations")
    private List<SeigniorageAllocation> seigniorageAllocations;
}
