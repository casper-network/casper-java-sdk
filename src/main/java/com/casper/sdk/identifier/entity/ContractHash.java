package com.casper.sdk.identifier.entity;

import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.key.Key;
import com.fasterxml.jackson.annotation.JsonCreator;
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

    @Override
    public String toString() {
        return hash != null ? PREFIX + hash : null;
    }
}
