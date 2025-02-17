package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.deploy.Approval;
import com.casper.sdk.model.transaction.field.CalltableSerializationEnvelopeBuilder;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

import java.util.List;

/**
 * Version1 of a Transaction
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonPropertyOrder({"hash", "payload", "approvals"})
public class TransactionV1 extends AbstractTransaction implements CasperSerializableObject {

    private static final int HASH_FIELD_INDEX = 0;
    private static final int PAYLOAD_FIELD_INDEX = 1;
    private static final int APPROVALS_FIELD_INDEX = 2;


    @JsonProperty("payload")
    private TransactionV1Payload payload;

    @Builder
    public TransactionV1(final Digest hash,
                         final TransactionV1Payload payload,
                         final List<Approval> approvals) {
        super(hash, approvals);
        this.payload = payload;
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {

        new CalltableSerializationEnvelopeBuilder(target)
                .addField(HASH_FIELD_INDEX, getHash())
                .addField(PAYLOAD_FIELD_INDEX, payload)
                .addField(APPROVALS_FIELD_INDEX, getApprovals())
                .serialize(ser, target);
    }

    /**
     * Calculates the body and header hashes and sets the transaction hash.
     */
    public void calculateHash() {
        try {
            setHash(payload.buildHash());
        } catch (Exception e) {
            throw new CasperClientException("Error calculating hash", e);
        }
    }
}
