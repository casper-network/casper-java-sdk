package com.syntifi.casper.sdk.model.block;

import java.util.List;

import lombok.Data;

/**
 * Casper's root block data
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see CasperBlockData
 * @since 0.0.1
 */
@Data
public class CasperBlock {

    /**
     * The block's hash.
     * 
     * @param hash the block's hash.
     * @return the block's hash.
     */
    private String hash;

    /**
     * The block's header info.
     */
    private CasperBlockHeader header;

    /**
     * The block's body info.
     */
    private CasperBlockBody body;

    /**
     * The block's proof data.
     */
    private List<CasperBlockProof> proofs;
}
