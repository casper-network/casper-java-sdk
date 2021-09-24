package com.casper.sdk.types;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class StoredContractByHash extends DeployExecutable {

    /** # Unique identifier. */
    private final ContractHash hash;
    /** the entry point */
    @JsonProperty("entry_point")
    private final String entryPoint;

    public StoredContractByHash(final ContractHash hash, final String entryPoint, final List<DeployNamedArg> args) {
        super(new byte[0], args);
        this.hash = hash;
        this.entryPoint = entryPoint;
    }

    public Digest getHash() {
        return hash;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    @Override
    public int getTag() {
        return 1;
    }
}
