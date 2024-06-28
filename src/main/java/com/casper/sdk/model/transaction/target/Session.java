package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

/**
 * The execution target is the included module bytes, i.e. compiled Wasm.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonTypeName("Session")
public class Session implements TransactionTarget {

    /** The compiled Wasm. */
    @JsonProperty("module_bytes")
    private byte[] moduleBytes;
    /** The execution runtime to use. */
    @JsonProperty("runtime")
    private TransactionRuntime runtime;

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
        if (moduleBytes != null) {
            ser.writeI32(moduleBytes.length);
            ser.writeByteArray(moduleBytes);
        } else {
            ser.writeI32(0);
        }
        runtime.serialize(ser, target);
    }

    @Override
    public byte getByteTag() {
        return 2;
    }
}
