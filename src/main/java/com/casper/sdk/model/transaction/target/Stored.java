package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
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

    private static final int TAG_FIELD_INDEX = 0;
    private static final int STORED_ID_INDEX = 1;
    private static final int STORED_RUNTIME_INDEX = 2;

    /** The identifier of the stored execution target. */
    private TransactionInvocationTarget id;
    /** The execution runtime to use. */
    @JsonProperty("runtime")
    private TransactionRuntime runtime;
    @JsonProperty("transferred_value")
    private long transferredValue;

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder(target)
                .addField(TAG_FIELD_INDEX, getByteTag())
                .addField(STORED_ID_INDEX, id)
                .addField(STORED_RUNTIME_INDEX, runtime)
                .serialize(ser, target);
    }

    @Override
    @JsonIgnore
    public byte getByteTag() {
        return 1;
    }
}
