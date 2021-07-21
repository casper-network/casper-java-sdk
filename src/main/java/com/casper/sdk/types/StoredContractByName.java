package com.casper.sdk.types;

import java.util.List;

/**
 * Encapsulates information required to execute an on-chain smart contract referenced by name.
 */
public class StoredContractByName extends ModuleBytes implements StoredContractNames {

    /** On-chain smart contract name - only in scope when dispatch account = contract owner account. */
    private final String name;
    /** TODO find out what is this */
    private final String entryPoint;

    public StoredContractByName(final String name,
                                final String entryPoint,
                                final byte[] moduleBytes,
                                final List<DeployNamedArg> args) {
        super(moduleBytes, args);
        this.entryPoint = entryPoint;
        this.name = name;
    }

    public StoredContractByName(final String name, final String entryPoint, final List<DeployNamedArg> args) {
        this(name, entryPoint, new byte[0], args);
    }

    public String getName() {
        return name;
    }

    public String getEntryPoint() {
        return entryPoint;
    }

    @Override
    public int getTag() {
        return 2;
    }
}
