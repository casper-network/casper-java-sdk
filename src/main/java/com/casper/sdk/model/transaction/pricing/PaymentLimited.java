package com.casper.sdk.model.transaction.pricing;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
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
@Getter
@Setter
@Builder
public class PaymentLimited implements PricingMode {

    private static final int PAYMENT_LIMITED_PAYMENT_AMOUNT_INDEX = 1;
    private static final int PAYMENT_LIMITED_GAS_PRICE_TOLERANCE_INDEX = 2;
    private static final int PAYMENT_LIMITED_STANDARD_PAYMENT_INDEX = 3;

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
        new CalltableSerializationEnvelopeBuilder()
                .addField(TAG_FIELD_INDEX,  /* U8 */  getByteTag())
                .addField(PAYMENT_LIMITED_PAYMENT_AMOUNT_INDEX, /* U64 */paymentAmount.longValue())
                .addField(PAYMENT_LIMITED_GAS_PRICE_TOLERANCE_INDEX, /* U8 */ (byte) gasPriceTolerance)
                .addField(PAYMENT_LIMITED_STANDARD_PAYMENT_INDEX, /* bool */ standardPayment)
                .serialize(ser, target);
    }

    @Override
    @JsonIgnore
    public byte getByteTag() {
        return PAYMENT_LIMITED_TAG;
    }
}
