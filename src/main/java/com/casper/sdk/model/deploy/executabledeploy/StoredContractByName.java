package com.casper.sdk.model.deploy.executabledeploy;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.deploy.NamedArg;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

import java.util.List;

/**
 * Abstract Executable Deploy Item containing the StoredContractByName.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see ExecutableDeployItem
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("StoredContractByName")
public class StoredContractByName extends ExecutableDeployItemWithEntryPoint {

    /**
     * Contract name
     */
    private String name;

    /**
     * Entry Point
     */
    @JsonProperty("entry_point")
    private String entryPoint;

    /**
     * @see NamedArg
     */
    private List<NamedArg<?>> args;

    /**
     * {@link ExecutableDeployItem} order 2
     */
    @Override
    public byte getOrder() {
        return 0x2;
    }

    /**
     * Implements the StoredContractByHash encoder
     */
    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws NoSuchTypeException, ValueSerializationException {
        ser.writeU8(getOrder());
        ser.writeString(getName());
        ser.writeString(getEntryPoint());
        serializeNamedArgs(ser, target);
    }
}
