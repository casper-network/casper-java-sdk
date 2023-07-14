package com.casper.sdk.model.deploy.executabledeploy;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.deploy.NamedArg;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;
import org.bouncycastle.util.encoders.Hex;

import java.util.List;

/**
 * Abstract Executable Deploy Item containing the StoredContractByHash.
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
@JsonTypeName("StoredContractByHash")
public class StoredContractByHash implements ExecutableDeployItemWithEntryPoint {

    /**
     * Hex-encoded Hash
     */
    private String hash;

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
     * {@link ExecutableDeployItem} order 1
     */
    @Override
    public byte getOrder() {
        return 0x1;
    }

    /**
     * Implements the StoredContractByHAsh encoder
     */
    @Override
    public void serialize(SerializerBuffer ser, Target target) throws NoSuchTypeException, ValueSerializationException {
        ser.writeU8(getOrder());
        ser.writeByteArray(Hex.decode(getHash()));
        ser.writeString(getEntryPoint());
        ser.writeI32(args.size());
        for (NamedArg<?> namedArg : args) {
            namedArg.serialize(ser, Target.BYTE);
        }
    }

}