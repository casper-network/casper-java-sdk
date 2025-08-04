package com.casper.sdk.identifier.entity;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.key.Key;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode
public class ContractHash implements EntityIdentifier {

    private static final String PREFIX = "contract-";

    /** Contract key hash */
    @JsonProperty("ContractHash")
    private String hash;


    @JsonCreator
    public ContractHash(final String hash) {
        String[] split = hash.split("-");
        this.hash = split[split.length - 1];
    }

    public ContractHash(final Digest hash) {
        this.hash = hash.toString();
    }

    public ContractHash(final Key contractKey) {
        this(contractKey.toString());
    }


    @JsonProperty
    @Override
    @JsonGetter("ContractHash")
    public String toString() {
        return PREFIX + hash;
    }

    public Digest asDigest() {
        return hash != null ? new Digest(hash) : null;
    }
}
