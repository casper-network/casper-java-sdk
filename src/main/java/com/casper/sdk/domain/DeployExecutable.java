package com.casper.sdk.domain;

import com.casper.sdk.json.DeployExecutableJsonDeserializer;
import com.casper.sdk.json.PublicKeyJsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

/**
 * Encapsulates vm executable information.
 */
@JsonDeserialize(using = DeployExecutableJsonDeserializer.class)
public class DeployExecutable {

    /** Set of arguments mapped to endpoint parameters. */
    private final List<DeployNamedArg> args;

    public DeployExecutable(List<DeployNamedArg> args) {
        this.args = args;
    }

    public List<DeployNamedArg> getArgs() {
        return args;
    }
}
