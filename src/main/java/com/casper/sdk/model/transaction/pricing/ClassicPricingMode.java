package com.casper.sdk.model.transaction.pricing;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

import java.math.BigInteger;

/**
 * The original payment model, where the creator of the transaction specifies how much they will pay, at what gas price.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ClassicPricingMode implements PricingMode {
    /** User-specified payment amount. */
    @JsonProperty("payment_amount")
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger paymentAmount;
    /**
     * User-specified gas_price tolerance (minimum 1). This is interpreted to mean "do not include this transaction in
     * a block if the current gas price is greater than this number"
     */
    @JsonProperty("gas_price_tolerance")
    private int gasPriceTolerance;
    /** Standard payment. */
    @JsonProperty("standard_payment")
    private boolean standardPayment;

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
        ser.writeU64(paymentAmount);
        ser.writeU8((byte) gasPriceTolerance);
        ser.writeBool(standardPayment);
    }

    @Override
    @JsonIgnore
    public byte getByteTag() {
        return CLASSIC_TAG;
    }
}
