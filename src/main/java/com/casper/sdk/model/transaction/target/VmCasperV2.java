package com.casper.sdk.model.transaction.target;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonGenerator;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;

import java.io.IOException;

/**
 * @author ian@meywood.com
 */
@JsonTypeName("VmCasperV2")
public class VmCasperV2 extends TransactionRuntime {

    private static final int TRANSFERRED_VALUE_INDEX = 1;
    private static final int SEED_VALUE_INDEX = 2;

    @JsonProperty("transferred_value")
    private long transferredValue;

    @JsonProperty("seed")
    private Digest seed;

    public VmCasperV2(long transferredValue, final Digest seed) {
        super(1);
        this.transferredValue = transferredValue;
        this.seed = seed;
    }

    //  gen.writeNumberField(TRANSFERRED_VALUE, value.getTransferredValue());
    // gen.writeObjectField(SEED, value.getSeed());

    /*      node.has(TRANSFERRED_VALUE) ? node.get(TRANSFERRED_VALUE).asLong() : 0*/
    //   node.has(SEED) && !node.get(SEED).isNull() && !"null".equals(node.get(SEED).asText()) ? new Digest(node.get(SEED).asText()) : nullS

    public VmCasperV2() {
        super(1);
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder(target)
                .addField(TAG_FIELD_INDEX, getByteTag())
                .addField(TRANSFERRED_VALUE_INDEX, transferredValue)
                .addOptionField(SEED_VALUE_INDEX, seed)
                .serialize(ser, target);
    }

    @Override
    public void toJson(final JsonGenerator gen) throws IOException {

        gen.writeStartObject();
        gen.writeFieldName(getClass().getSimpleName());
        gen.writeStartObject();
        gen.writeNumberField("transferred_value", transferredValue);
        gen.writeStringField("seed", seed != null ? seed.toString() : null);
        gen.writeEndObject();
        gen.writeEndObject();
    }


}
