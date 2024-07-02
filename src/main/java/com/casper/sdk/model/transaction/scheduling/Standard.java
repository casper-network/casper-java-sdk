package com.casper.sdk.model.transaction.scheduling;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * No special scheduling applied.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@Builder
@Setter
public class Standard implements TransactionScheduling {

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
    }

    @Override
    public byte getByteTag() {
        return 0;
    }
}
