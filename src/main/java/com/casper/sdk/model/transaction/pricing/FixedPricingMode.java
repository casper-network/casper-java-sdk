package com.casper.sdk.model.transaction.pricing;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

/**
 * The cost of the transaction is determined by the cost table, per the transaction category.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FixedPricingMode implements PricingMode {
    /**
     * User-specified gas_price tolerance (minimum 1).
     * This is interpreted to mean "do not include this transaction in a block
     * if the current gas price is greater than this number"
     */
    @JsonProperty("gas_price_tolerance")
    private int gasPriceTolerance;

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
        ser.writeU8((byte) gasPriceTolerance);
    }

    @JsonIgnore
    @Override
    public byte getByteTag() {
        return FIXED_TAG;
    }
}
