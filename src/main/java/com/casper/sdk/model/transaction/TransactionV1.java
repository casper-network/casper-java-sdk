package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.deploy.Approval;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class TransactionV1 extends Transaction implements CasperSerializableObject {

    @JsonProperty("header")
    private TransactionV1Header header;
    @JsonProperty("body")
    private TransactionV1Body body;

    @Builder
    public TransactionV1(final Digest hash,
                         final TransactionV1Header header,
                         final TransactionV1Body body,
                         final List<Approval> approvals) {
      super(hash, approvals);
        this.header = header;
        this.body = body;
    }

    @Override
    public void serialize(final SerializerBuffer ser, final Target target) throws ValueSerializationException, NoSuchTypeException {
        getHash().serialize(ser, target);
        header.serialize(ser, target);
        body.serialize(ser, target);
        serializeApprovals(ser, target);
    }

    /**
     * Calculates the body and header hashes and sets the transaction hash.
     */
    public void calculateHash() {
        try {
            header.setBodyHash(body.buildHash());
            setHash(header.buildHash());
        } catch (Exception e) {
            throw new CasperClientException("Error calculating hash", e);
        }
    }
}
