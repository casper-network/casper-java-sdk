package com.casper.sdk.model.transaction.entrypoint;

import com.casper.sdk.model.clvalue.serde.Target;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * A non-native, arbitrary entry point.
 *
 * @author ian@meywood.com
 */
@Getter
@Setter
@Builder
public class CustomEntryPoint extends TransactionEntryPoint {

    private String custom;

    public CustomEntryPoint(final String custom) {
        super((byte) 1, "Custom");
        this.custom = custom;
    }

    public CustomEntryPoint() {
        this(null);
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException {
        super.serialize(ser, target);
        ser.writeString(custom);
    }

    @Override
    public String toString() {
        return "{\"Custom\":\"" + custom + "\"}";
    }
}
