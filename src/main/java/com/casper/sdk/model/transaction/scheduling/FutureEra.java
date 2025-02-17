package com.casper.sdk.model.transaction.scheduling;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Execution should be scheduled for the specified era.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonTypeName("FutureEra")
public class FutureEra implements TransactionScheduling {

    private static final int TAG_FIELD_INDEX = 0;
    private static final int FUTURE_ERA_ID_INDEX = 1;

    @JsonValue
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger eraId;

    @JsonCreator
    public FutureEra(final BigInteger eraId) {
        this.eraId = eraId;
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder(target)
                .addField(TAG_FIELD_INDEX, getByteTag())
                .addField(FUTURE_ERA_ID_INDEX, eraId)
                .serialize(ser, target);
    }

    @Override
    public byte getByteTag() {
        return 1;
    }
}
