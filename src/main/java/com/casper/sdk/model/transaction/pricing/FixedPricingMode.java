package com.casper.sdk.model.transaction.pricing;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.CLValueU8;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
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

    private static final int FIXED_GAS_PRICE_TOLERANCE_INDEX = 1;
    private static final int FIXED_ADDITIONAL_COMPUTATION_FACTOR_INDEX = 2;

    /**
     * User-specified additional computation factor (minimum 0). If "0" is provided,
     * no additional logic is applied to the computation limit. Each value above "0"
     * tells the node that it needs to treat the transaction as if it uses more gas
     * than it's serialized size indicates. Each "1" will increase the "wasm lane"
     * size bucket for this transaction by 1. So if the size of the transaction
     * indicates bucket "0" and "additional_computation_factor = 2", the transaction
     * will be treated as a "2".
     */
    @JsonProperty("additional_computation_factor")
    private int additionalComputationFactor;
    /**
     * User-specified gas_price tolerance (minimum 1).
     * This is interpreted to mean "do not include this transaction in a block
     * if the current gas price is greater than this number"
     */
    @JsonProperty("gas_price_tolerance")
    private int gasPriceTolerance;

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder()
                .addField(TAG_FIELD_INDEX,  /* U8 */ new CLValueU8(getByteTag()))
                .addField(FIXED_GAS_PRICE_TOLERANCE_INDEX, /* U8 */ new CLValueU8((byte) gasPriceTolerance))
                .addField(FIXED_ADDITIONAL_COMPUTATION_FACTOR_INDEX, /* U8 */ new CLValueU8((byte) additionalComputationFactor))
                .serialize(ser, target);
    }

    @JsonIgnore
    @Override
    public byte getByteTag() {
        return FIXED_TAG;
    }
}
