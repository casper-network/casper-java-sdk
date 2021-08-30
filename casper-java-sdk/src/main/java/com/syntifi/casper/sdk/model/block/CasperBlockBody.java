package com.syntifi.casper.sdk.model.block;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * Holds the body data of a Casper block
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CasperBlock
 * @since 0.0.1
 */
@Data
public class CasperBlockBody {

    @JsonProperty("proposer")
    private String proposer;

    @JsonProperty("deploy_hashes")
    private List<String> deployHashes;

    @JsonProperty("transfer_hashes")
    private List<String> transferHashes;

}
