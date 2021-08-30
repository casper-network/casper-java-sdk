package com.syntifi.casper.sdk.filter.block;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Filter class passed to service
 * {@link com.syntifi.casper.sdk.service.CasperService#getBlock(CasperBlockByHashFilter)}
 * to filter a block by its hash
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
@AllArgsConstructor
public class CasperBlockByHashFilter {

    @JsonProperty("Hash")
    private String hash;
}
