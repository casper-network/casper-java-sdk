package com.casper.sdk.service.result;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AccountInfo extends AbstractResult {

    private StoredValue storedValue;
    private String merkleProof;

    @JsonCreator
    public AccountInfo(@JsonProperty("api_version") final String apiVersion,
                       @JsonProperty("stored_value") final StoredValue storedValue,
                       @JsonProperty("merkle_proof") final String merkleProof) {
        super(apiVersion);
        this.storedValue = storedValue;
        this.merkleProof = merkleProof;
    }

    public StoredValue getStoredValue() {
        return storedValue;
    }

    public String getMerkleProof() {
        return merkleProof;
    }
}
