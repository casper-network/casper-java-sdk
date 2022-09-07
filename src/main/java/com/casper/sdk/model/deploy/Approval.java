package com.casper.sdk.model.deploy;

import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.key.PublicKey;
import com.casper.sdk.model.key.Signature;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.*;

/**
 * A struct containing a signature and the public key of the signer
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
public class Approval implements CasperSerializableObject {

    /**
     * @see PublicKey
     */
    private PublicKey signer;

    /**
     * @see Signature
     */
    private Signature signature;

    /**
     * Implements Approval encoder
     */
    @Override
    public void serialize(SerializerBuffer ser, boolean encodeType) {
        signer.serialize(ser, encodeType);
        signature.serialize(ser, encodeType);
    }
}
