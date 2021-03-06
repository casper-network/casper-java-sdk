package com.casper.sdk.types;

import com.casper.sdk.service.json.deserialize.DeployExecutableJsonDeserializer;
import com.casper.sdk.service.json.serialize.DeployExecutableJsonSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.List;

/**
 * Encapsulates vm executable information.
 */
@JsonDeserialize(using = DeployExecutableJsonDeserializer.class)
@JsonSerialize(using = DeployExecutableJsonSerializer.class)
public abstract class DeployExecutable implements HasTag {

    /** Raw WASM payload. */
    @CLName("module_bytes")
    private final byte[] moduleBytes;

    /** Set of arguments mapped to endpoint parameters. */
    private final List<DeployNamedArg> args;

    public DeployExecutable(final byte[] moduleBytes, final List<DeployNamedArg> args) {
        this.moduleBytes = moduleBytes;
        this.args = args;
    }

    public byte[] getModuleBytes() {
        return moduleBytes;
    }

    public List<DeployNamedArg> getArgs() {
        return args;
    }

    public DeployNamedArg getNamedArg(final String name) {
        if (args != null) {
            for (DeployNamedArg arg : args) {
                if (arg.getName().equals(name)) {
                    return arg;
                }
            }
        }
        return null;
    }
}
