package com.casper.sdk.types;

import java.util.List;
import java.util.Optional;

/**
 * Encapsulates information required to execute a versioned on-chain smart contract referenced by name.
 */
public class StoredVersionedContractByName extends StoredContractByName {

    /** Smart contract version identifier. */
    private final Number version;

    public StoredVersionedContractByName(final String name,
                                         final Number version,
                                         final String entryPoint,
                                         final byte[] moduleBytes,
                                         final List<DeployNamedArg> args) {
        super(name, entryPoint, moduleBytes, args);
        this.version = version;
    }

    public StoredVersionedContractByName(final String name,
                                         final long version,
                                         final String entryPoint,
                                         final List<DeployNamedArg> args) {
        this(name, version, entryPoint, null, args);
    }

    public Optional<Number> getVersion() {
        return Optional.ofNullable(version != null ? version.longValue() : null);
    }

    @Override
    public int getTag() {
        return 4;
    }
}
