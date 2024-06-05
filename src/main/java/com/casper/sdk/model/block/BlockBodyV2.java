package com.casper.sdk.model.block;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.key.PublicKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

/**
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BlockBodyV2 extends BlockBody {

    /*    transactions

     *//**
     * @see PublicKey
     *//*
    @JsonProperty("proposer")
    private PublicKey proposer;



    mint: TransactionHash[];
    auction: TransactionHash[];
    install_upgrade: TransactionHash;
    standard: TransactionHash[];
    rewarded_signatures: number[][];
    hash: string;

    Map<transactions*/
    /**
     * @see PublicKey
     */
    @JsonProperty("proposer")
    private PublicKey proposer;

    /**
     * List of Hex-encoded hash digest
     */
    @JsonProperty("deploy_hashes")
    private List<String> deployHashes;

    /**
     * List of Hex-encoded hash digest
     */
    @JsonProperty("transfer_hashes")
    private List<String> transferHashes;

    /** The body's hash. */
    private Digest hash;

    @JsonProperty("rewarded_signatures")
    private List<List<Long>> rewardedSignatures;



    //private List<Transaction> transactions;
}
