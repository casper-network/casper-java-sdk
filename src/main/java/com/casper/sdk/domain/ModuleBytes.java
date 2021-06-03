package com.casper.sdk.domain;

import java.util.List;

public class ModuleBytes extends DeployExecutable {
    public ModuleBytes(final List<DeployNamedArg> args) {
        super(args);
    }

    @Override
    public int getTag() {
        return 0;
    }
}
