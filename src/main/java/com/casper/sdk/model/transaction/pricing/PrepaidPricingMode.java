package com.casper.sdk.model.transaction.pricing;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

/**
 * The payment for this transaction was previously paid, as proven by
 * the receipt hash (this is for future use, not currently implemented).
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PrepaidPricingMode implements PricingMode {

    private static final int RESERVED_RECEIPT_INDEX = 1;

    @JsonProperty("receipt")
    private Digest receipt;

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder()
                .addField(TAG_FIELD_INDEX,  /* U8 */getByteTag())
                .addField(RESERVED_RECEIPT_INDEX, receipt)
                .serialize(ser, target);
    }

    @Override
    @JsonIgnore
    public byte getByteTag() {
        return PREPAID_TAG;
    }
}
