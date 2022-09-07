package com.casper.sdk.model.deploy.executabledeploy;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.deploy.NamedArg;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

import java.util.List;

/**
 * An AbstractExecutableDeployItem of Type Transfer containing the runtime args
 * of the contract.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName("Transfer")
public class Transfer implements ExecutableDeployItem {

    /**
     * List of {@link NamedArg}
     */
    private List<NamedArg<?>> args;

    /**
     * {@link ExecutableDeployItem} order 5
     */
    @Override
    public byte getOrder() {
        return 0x5;
    }

    /**
     * Implements the Transfer encoder
     */
    @Override
    public void serialize(SerializerBuffer ser, boolean encodeType) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getOrder());
        ser.writeI32(args.size());
        for (NamedArg<?> namedArg : args) {
            namedArg.serialize(ser, true);
        }
    }
}