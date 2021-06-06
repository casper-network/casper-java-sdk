package com.casper.sdk.domain;

import java.util.List;

public class ModuleBytes extends DeployExecutable {

    public ModuleBytes(final byte[] moduleBytes, final List<DeployNamedArg> args) {
        super(moduleBytes, args);
    }

    public ModuleBytes(List<DeployNamedArg> args) {
        this(new byte[0], args);
    }

    @Override
    public int getTag() {
        return 0;
    }
}
