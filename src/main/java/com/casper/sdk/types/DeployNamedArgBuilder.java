package com.casper.sdk.types;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for building DeployNamedArg lists
 */
public class DeployNamedArgBuilder {

    private final List<DeployNamedArg> argList = new ArrayList<>();

    public DeployNamedArgBuilder add(final String name, final CLValue value) {
        argList.add(new DeployNamedArg(name, value));
        return this;
    }

    public List<DeployNamedArg> build() {
        return argList;
    }
}
