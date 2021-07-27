package com.casper.sdk.types;

import java.util.List;

/**
 * Specialisation of DeployExecutable for a transfer used in Session
 */
public class Transfer extends DeployExecutable {

    public Transfer(final byte[] moduleBytes, final List<DeployNamedArg> args) {
        super(moduleBytes, args);
    }

    public Transfer(final List<DeployNamedArg> amountArg) {
        this(new byte[0], amountArg);
    }

    @Override
    public int getTag() {
        return 5;
    }
}
