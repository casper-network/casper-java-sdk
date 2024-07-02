package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.common.Ttl;
import com.casper.sdk.model.key.Tag;
import com.casper.sdk.model.transaction.pricing.PricingMode;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

import java.util.Date;

/**
 * The header portion of a TransactionV1.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TransactionV1Header implements CasperSerializableObject, Tag {
    @JsonProperty("chain_name")
    private String chainName;
    @JsonProperty("timestamp")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private Date timestamp;
    @JsonProperty("ttl")
    private Ttl ttl;
    @JsonProperty("body_hash")
    private Digest bodyHash;
    @JsonProperty("pricing_mode")
    private PricingMode pricingMode;
    @SuppressWarnings("rawtypes")
    @JsonProperty("initiator_addr")
    private InitiatorAddr initiatorAddr;

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeU8(getByteTag());
        ser.writeString(chainName);
        ser.writeI64(timestamp.getTime());
        ttl.serialize(ser, target);
        bodyHash.serialize(ser, target);
        pricingMode.serialize(ser, target);
        initiatorAddr.serialize(ser, target);
    }

    @Override
    public byte getByteTag() {
        return 1;
    }

    public Digest buildHash() throws NoSuchTypeException, ValueSerializationException {
        SerializerBuffer serializerBuffer = new SerializerBuffer();
        this.serialize(serializerBuffer, Target.BYTE);
        return Digest.blake2bDigestFromBytes(serializerBuffer.toByteArray());
    }

}

