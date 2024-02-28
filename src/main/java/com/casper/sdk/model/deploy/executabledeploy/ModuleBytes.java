package com.casper.sdk.model.deploy.executabledeploy;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.deploy.NamedArg;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import dev.oak3.sbs4j.util.ByteUtils;
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
public class ModuleBytes extends ExecutableDeployItem {

    /**
     * Module bytes
     */
    @JsonIgnore
    private byte[] bytes;

    /**
     * @see NamedArg
     */
    private List<NamedArg<?>> args;

    @JsonProperty("module_bytes")
    public String getJsonBytes() {
        return ByteUtils.encodeHexString(bytes);
    }

    @JsonProperty("module_bytes")
    public void setJsonBytes(String byteContent) {
        this.bytes = ByteUtils.parseHexString(byteContent);
    }

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
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getOrder());
        ser.writeI32(getBytes().length);
        ser.writeByteArray(getBytes());
        serializeNamedArgs(ser, target);
    }
}
