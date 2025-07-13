package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonGenerator;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;

import java.io.IOException;

/**
 * @author ian@meywood.com
 */
@JsonTypeName("VmCasperV1")
public class VmCasperV1 extends TransactionRuntime {

    public VmCasperV1() {
        super(0);
    }


    @Override
    public byte getTag() {
        return 0;
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder(target)
                .addField(TAG_FIELD_INDEX, getByteTag())
                .serialize(ser, target);
    }


    @Override
    public void toJson(JsonGenerator gen) throws IOException {
        gen.writeString(getClass().getSimpleName());
    }
}
