package com.casper.sdk.model.deploy;

import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.deploy.executabledeploy.ExecutableDeployItem;
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
public class Deploy implements CasperSerializableObject {

    /**
     * Hex-encoded deploy hash
     */
    private Digest hash;

    /**
     * @see DeployHeader
     */
    private DeployHeader header;

    /**
     * @see Approval
     */
    private List<Approval> approvals;

    /**
     * @see ExecutableDeployItem
     */
    private ExecutableDeployItem payment;

    /**
     * @see ExecutableDeployItem
     */
    private ExecutableDeployItem session;

    /**
     * Implements Deploy encoder
     */
    @Override
    public void serialize(SerializerBuffer ser, boolean encodeType) throws NoSuchTypeException, ValueSerializationException {
        header.serialize(ser, true);
        hash.serialize(ser, true);
        payment.serialize(ser, true);
        session.serialize(ser, true);
        ser.writeI32(approvals.size());
        for (Approval approval : approvals) {
            approval.serialize(ser, true);
        }
    }
}

