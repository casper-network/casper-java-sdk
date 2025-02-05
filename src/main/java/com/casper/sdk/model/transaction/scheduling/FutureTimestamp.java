package com.casper.sdk.model.transaction.scheduling;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
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

    private static final int TAG_FIELD_INDEX = 0;
    private static final int FUTURE_TIMESTAMP_TIMESTAMP_INDEX = 1;

    @JsonValue
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private String futureTimestamp;

    @JsonIgnore
    public Date asDate() {
        return futureTimestamp != null ? new DateTime(futureTimestamp).toDate() : null;
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder()
                .addField(TAG_FIELD_INDEX, getByteTag())
                .addField(FUTURE_TIMESTAMP_TIMESTAMP_INDEX, asDate().getTime())
                .serialize(ser, target);
    }

    @Override
    public byte getByteTag() {
        return 2;
    }
}
