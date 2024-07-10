package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

/**
 * The execution target is a stored entity or package.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonTypeName("Stored")
public class Stored implements TransactionTarget {

    /** The execution runtime to use. */
    @JsonProperty("runtime")
    private TransactionRuntime runtime;

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
        ser.writeU8(runtime.getByteTag());
    }

    @Override
    @JsonIgnore
    public byte getByteTag() {
        return 1;
    }
}
