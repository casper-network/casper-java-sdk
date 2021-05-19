package com.casper.sdk.domain;

import com.casper.sdk.json.DeployNamedArgJsonDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Domain type: a named argument to be mapped to a contract function parameter.
 */
@JsonDeserialize(using = DeployNamedArgJsonDeserializer.class)
public class DeployNamedArg {

    /** Argument name mapped to an entry point parameter. */
    private final String name;
    /** Argument value. */
    private final CLValue value;

    public DeployNamedArg(final String name, final CLValue value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public CLValue getValue() {
        return value;
    }
}
