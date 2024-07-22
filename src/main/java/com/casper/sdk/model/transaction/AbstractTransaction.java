package com.casper.sdk.model.transaction;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.serde.Target;
import com.casper.sdk.model.common.Digest;
import com.casper.sdk.model.deploy.Approval;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.key.Signature;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.crypto.key.AbstractPrivateKey;
import dev.oak3.sbs4j.SerializerBuffer;
import dev.oak3.sbs4j.exception.ValueSerializationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Base class for transaction response obtained using the info_get_transaction RPC call.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class AbstractTransaction {

    @JsonProperty("hash")
    private Digest hash;

    /** @see Approval */
    @JsonProperty("approvals")
    private List<Approval> approvals = new ArrayList<>();

    protected void serializeApprovals(final SerializerBuffer ser,
                                      final Target target) throws ValueSerializationException, NoSuchTypeException {
        ser.writeI32(approvals.size());
        for (Approval approval : approvals) {
            approval.serialize(ser, target);
        }
    }

    /**
     * Calculate the hash of the transaction
     */
    protected abstract void calculateHash();

    /**
     * Sign the transaction with the given signer
     *
     * @param signer the approver's private key
     * @return the signed transaction
     */
    public <T extends AbstractTransaction> T sign(final AbstractPrivateKey signer) {

        try {
            if (hash == null) {
                calculateHash();
            }
            final Signature signature = Signature.sign(signer, hash.getDigest());
            getApprovals().add(Approval.builder()
                    .signer(PublicKey.fromAbstractPublicKey(signer.derivePublicKey()))
                    .signature(signature)
                    .build()
            );
            //noinspection unchecked
            return (T) this;

        } catch (GeneralSecurityException e) {
            throw new CasperClientException("Error signing transaction", e);
        }
    }
}
