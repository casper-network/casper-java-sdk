package com.casper.sdk.service.result;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssociatedKey {

    /**
     * account_hash(String) Hex-encoded account hash.
     */
    private final String accountHash;

    /**
     * weight(Integer)
     */
    private final int weight;

    public AssociatedKey(@JsonProperty("account_hash") final String accountHash,
                         @JsonProperty("weight") final int weight) {
        this.accountHash = accountHash;
        this.weight = weight;
    }

    public String getAccountHash() {
        return accountHash;
    }

    public int getWeight() {
        return weight;
    }
}
