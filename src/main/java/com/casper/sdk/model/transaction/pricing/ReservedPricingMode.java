package com.casper.sdk.model.transaction.pricing;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The payment for this transaction was previously reserved, as proven by
 * the receipt hash (this is for future use, not currently implemented).
 *
 * @author ian@meywood.com
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReservedPricingMode implements PricingMode {
    @JsonProperty("receipt")
    private Digest receipt;

    @Override
    public void serialize(SerializerBuffer ser, Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
        receipt.serialize(ser, target);
    }

    @Override
    public byte getByteTag() {
        return RESERVED_TAG;
    }
}
