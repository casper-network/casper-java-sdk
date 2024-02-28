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
 * Abstract Executable Deploy Item containing the StoredVersionedContractByHash.
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
@JsonTypeName("StoredVersionedContractByHash")
public class StoredVersionedContractByHash extends ExecutableDeployItemWithEntryPoint {

    /**
     * Hex-encoded Hash
     */
    private String hash;

    /**
     * contract version
     */
    private long version;

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
     * {@link ExecutableDeployItem} order 3
     */
    @Override
    public byte getOrder() {
        return 0x3;
    }

    /**
     * Implements the StoredVersionedContractByHash encoder
     */
    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws NoSuchTypeException, ValueSerializationException {
        ser.writeU8(getOrder());
        ser.writeString(getHash());
        ser.writeI64(getVersion());
        ser.writeString(getEntryPoint());
        serializeNamedArgs(ser, target);
    }
}
