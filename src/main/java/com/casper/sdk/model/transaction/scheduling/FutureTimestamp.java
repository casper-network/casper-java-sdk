package com.casper.sdk.model.transaction.scheduling;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.*;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Execution should be scheduled for the specified timestamp or later.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@JsonTypeName("FutureTimestamp")
public class FutureTimestamp implements TransactionScheduling {

    @JsonValue
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private String futureTimestamp;

    @JsonIgnore
    public Date asDate() {
        return futureTimestamp != null ? new DateTime(futureTimestamp).toDate() : null;
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
        ser.writeI64(asDate().getTime());
    }

    @Override
    public byte getByteTag() {
        return 2;
    }
}
