package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The alias identifying the invocable entity.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ByName implements TransactionInvocationTarget {
    @JsonValue
    private String name;

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
        ser.writeString(name);
    }

    @JsonIgnore
    @Override
    public byte getByteTag() {
        return 1;
    }
}
