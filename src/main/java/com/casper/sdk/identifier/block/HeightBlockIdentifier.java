package com.casper.sdk.identifier.block;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.service.CasperService;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Identifier class passed to service
 * {@link CasperService#getBlock(BlockIdentifier)} to identify and
 * retrieve the block given its height.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
public class HeightBlockIdentifier implements BlockIdentifier {

    /**
     * Block height
     */
    @JsonProperty("Height")
    private long height;
}
