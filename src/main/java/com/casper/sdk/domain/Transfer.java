package com.casper.sdk.domain;

import java.util.List;

/**
 * Specialisation of DeployExecutable for a transfer used in Session
 */
public class Transfer extends DeployExecutable {

    public Transfer(final List<DeployNamedArg> args) {
        super(args);
    }
}
