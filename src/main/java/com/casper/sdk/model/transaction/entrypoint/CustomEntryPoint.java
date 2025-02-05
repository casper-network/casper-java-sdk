package com.casper.sdk.model.transaction.entrypoint;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
        this();
        this.custom = custom;
    }

    public CustomEntryPoint() {
        super((byte) CUSTOM_TAG, "Custom");
    }

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {

        new CalltableSerializationEnvelopeBuilder()
                .addField(TAG_FIELD_INDEX, getTag())
                .addField(CUSTOM_CUSTOM_INDEX, this.custom)
                .serialize(ser, target);
    }

    @Override
    public String toString() {
        return "{\"Custom\":\"" + custom + "\"}";
    }
}
