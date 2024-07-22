package com.casper.sdk.model.deploy;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.deploy.executabledeploy.ExecutableDeployItem;
import com.casper.sdk.model.transaction.AbstractTransaction;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.*;

import java.util.List;

/**
 * Deploy an item containing a smart contract along with the requesters'
 * signature(s)
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Deploy extends AbstractTransaction implements CasperSerializableObject {

    /**
     * @see DeployHeader
     */
    private DeployHeader header;

    /**
     * @see ExecutableDeployItem
     */
    private ExecutableDeployItem payment;

    /**
     * @see ExecutableDeployItem
     */
    private ExecutableDeployItem session;

    @Builder
    public Deploy(final Digest hash,
                  final DeployHeader header,
                  final ExecutableDeployItem payment,
                  final ExecutableDeployItem session,
                  final List<Approval> approvals) {
        super(hash, approvals);
        this.header = header;
        this.payment = payment;
        this.session = session;
    }

    /**
     * Implements Deploy encoder
     */
    @Override
    public void serialize(final SerializerBuffer ser,
                          final Target target) throws NoSuchTypeException, ValueSerializationException {
        header.serialize(ser, Target.BYTE);
        getHash().serialize(ser, Target.BYTE);
        payment.serialize(ser, Target.BYTE);
        session.serialize(ser, Target.BYTE);
        serializeApprovals(ser, Target.BYTE);
    }

    @Override
    protected void calculateHash() {
        try {
            header.setBodyHash(calculateSessionAndPaymentHash());
            setHash(header.buildHash());
        } catch (Exception e) {
            throw new CasperClientException("Error calculation header hash", e);
        }
    }

    protected Digest calculateSessionAndPaymentHash() {
        try {
            final SerializerBuffer ser = new SerializerBuffer();
            payment.serialize(ser, Target.BYTE);
            session.serialize(ser, Target.BYTE);
            return Digest.blake2bDigestFromBytes(ser.toByteArray());
        } catch (Exception e) {
            throw new CasperClientException("Error calculation session and payment hash", e);
        }
    }
}

