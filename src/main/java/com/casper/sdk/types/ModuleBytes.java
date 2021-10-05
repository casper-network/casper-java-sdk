package com.casper.sdk.types;

import java.util.List;

public class ModuleBytes extends DeployExecutable {

    public ModuleBytes(final byte[] moduleBytes, final List<DeployNamedArg> args) {
        super(moduleBytes, args);
    }

    public ModuleBytes(final List<DeployNamedArg> args) {
        this(new byte[0], args);
    }

    @Override
    public int getTag() {
        return 0;
    }
}
