package com.casper.sdk.types;

import java.util.List;
import java.util.Optional;

public class StoredVersionedContractByHash extends StoredContractByHash {

    /** Smart contract version identifier. */
    private final Number version;

    public StoredVersionedContractByHash(final ContractHash hash,
                                         final Number contractVersion,
                                         final String entryPoint,
                                         final List<DeployNamedArg> args) {
        super(hash, entryPoint, args);
        this.version = contractVersion;
    }

    public Optional<Number> getVersion() {
        return Optional.ofNullable(version != null ? version.longValue() : null);
    }

    @Override
    public int getTag() {
        return 3;
    }
}
