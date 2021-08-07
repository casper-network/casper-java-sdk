package com.syntifi.casper.sdk.model.block;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@Data
public class CasperBlockBody {
    private String proposer;

    @JsonProperty("deploy_hashes")
    private List<String> deployHashes;

    @JsonProperty("transfer_hashes")
    private List<String> transferHashes;

}
