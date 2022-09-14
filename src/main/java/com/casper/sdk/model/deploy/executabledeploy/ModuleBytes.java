package com.casper.sdk.model.deploy.executabledeploy;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.deploy.NamedArg;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

import java.util.List;

/**
 * Abstract Executable Deploy Item containing the ModuleBytes of the contract.
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
@JsonTypeName("ModuleBytes")
public class ModuleBytes implements ExecutableDeployItem {

    /**
     * Module bytes
     */
    @JsonProperty("module_bytes")
    private byte[] bytes;

    /**
     * @see NamedArg
     */
    private List<NamedArg<?>> args;


    /**
     * {@link ExecutableDeployItem} order 0
     */
    @Override
    public byte getOrder() {
        return 0x0;
    }

    /**
     * Implements the ModuleBytes encoder
     */
    @Override
    public void serialize(SerializerBuffer ser, boolean encodeType) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getOrder());
        ser.writeI32(getBytes().length);
        ser.writeByteArray(getBytes());
        ser.writeI32(args.size());
        for (NamedArg<?> namedArg : args) {
            namedArg.serialize(ser, encodeType);
        }
    }
}
