package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.CLValueString;
import com.casper.sdk.model.clvalue.CLValueU64;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.key.Tag;
import com.casper.sdk.model.transaction.entrypoint.TransactionEntryPoint;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.casper.sdk.model.transaction.field.Fields;
import com.casper.sdk.model.transaction.pricing.PricingMode;
import com.casper.sdk.model.transaction.scheduling.TransactionScheduling;
import com.casper.sdk.model.transaction.target.TransactionTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;
import org.bouncycastle.util.encoders.Hex;

import java.util.Date;

/**
 * The payload of a TransactionV1.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransactionV1Payload implements CasperSerializableObject, Tag {

    private static final int INITIATOR_ADDR_FIELD_INDEX = 0;
    private static final int TIMESTAMP_FIELD_INDEX = 1;
    private static final int TTL_FIELD_INDEX = 2;
    private static final int CHAIN_NAME_FIELD_INDEX = 3;
    private static final int PRICING_MODE_FIELD_INDEX = 4;
    private static final int FIELDS_FIELD_INDEX = 5;


    @SuppressWarnings("rawtypes")
    @JsonProperty("initiator_addr")
    private InitiatorAddr initiatorAddr;
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timestamp;
    @JsonProperty("ttl")
    private Ttl ttl;
    @JsonProperty("chain_name")
    private String chainName;
    @JsonProperty("pricing_mode")
    private PricingMode pricingMode;
    @JsonProperty("fields")
    @Builder.Default
    private Fields fields = new Fields();

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        new CalltableSerializationEnvelopeBuilder()
                .addField(INITIATOR_ADDR_FIELD_INDEX, this.initiatorAddr)
                .addField(TIMESTAMP_FIELD_INDEX, new CLValueU64(timestamp != null ? timestamp.getTime() : new Date().getTime()))
                .addField(TTL_FIELD_INDEX, new CLValueU64(ttl.getTtl()))
                .addField(CHAIN_NAME_FIELD_INDEX, new CLValueString(this.chainName))
                .addField(PRICING_MODE_FIELD_INDEX, this.pricingMode)
                .addField(FIELDS_FIELD_INDEX, this.fields)
                .serialize(ser, target);
    }

    @JsonIgnore
    @Override
    public byte getByteTag() {
        return 1;
    }

    public void setTarget(final TransactionTarget target) {
        this.getFields().setTarget(target);
    }

    public void setEntryPoint(final TransactionEntryPoint entryPoint) {
        this.getFields().setEntryPoint(entryPoint);
    }

    public void setScheduling(final TransactionScheduling scheduling) {
        this.getFields().setScheduling(scheduling);
    }

    public Digest buildHash() throws NoSuchTypeException, ValueSerializationException {
        final SerializerBuffer serializerBuffer = new SerializerBuffer();
        this.serialize(serializerBuffer, Target.BYTE);
        final byte[] bytes = serializerBuffer.toByteArray();
        final String hexBytes = Hex.toHexString(bytes);
        return Digest.blake2bDigestFromBytes(bytes);
    }
}

