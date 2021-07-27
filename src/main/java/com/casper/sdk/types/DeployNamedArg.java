package com.casper.sdk.types;

import com.casper.sdk.service.json.deserialize.DeployNamedArgJsonDeserializer;
import com.casper.sdk.service.json.serialize.DeployNamedArgJsonSerializer;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Type: a named argument to be mapped to a contract function parameter.
 */
@JsonDeserialize(using = DeployNamedArgJsonDeserializer.class)
@JsonSerialize(using = DeployNamedArgJsonSerializer.class)
public class DeployNamedArg {

    /** Argument name mapped to an entry point parameter. */
    private final String name;
    /** Argument value. */
    private final CLValue value;

    @JsonCreator
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
