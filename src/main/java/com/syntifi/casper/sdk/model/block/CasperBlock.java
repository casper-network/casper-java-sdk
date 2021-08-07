package com.syntifi.casper.sdk.model.block;

import java.util.List;

import lombok.Data;

/**
 * 
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.1.0
 */
@Data
public class CasperBlock {
    private CasperBlockHeader header;

    private String hash;

    private CasperBlockBody body;

    private List<CasperBlockProof> proofs;
}
