package com.casper.sdk.identifier.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * The hash of an on-chain contract .
 *
 * @author carl@stormeye.co.uk
 */
@Getter
@Setter
@Builder
public class ContractHash implements EntityIdentifier{

    /** Contract key hash */
    @JsonProperty("ContractHash")
    private String contractHash;

    @JsonCreator
    public ContractHash(final String hash) {
        this.contractHash = hash.replace("hash-", "contract-");
    }
}
