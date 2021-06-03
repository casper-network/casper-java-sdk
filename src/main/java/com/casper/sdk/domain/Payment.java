package com.casper.sdk.domain;

import java.util.List;

/**
 * Specialization DeployExecutable for Payments
 */
public class Payment extends DeployExecutable {

    /** Raw WASM payload.*/
    @CLName("module_bytes")
    private final byte[] moduleBytes;

    public Payment(final byte[] moduleBytes, final List<DeployNamedArg> args) {
        super(args);
        this.moduleBytes = moduleBytes;
    }

    public byte[] getModuleBytes() {
        return moduleBytes;
    }

    @Override
    public int getTag() {
        return 0;
    }
}

