package com.casper.sdk.model.deploy.executabledeploy;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.*;

/**
 * Executable Deploy Item containing the StoredVersionedContractByHash.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see ExecutableDeployItem
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("StoredVersionedContractByHash")
public class StoredVersionedContractByHash extends AbstractStoredVersionedContract {

    /** Hex-encoded Hash */
    @JsonProperty("hash")
    private String hash;


    /** {@link ExecutableDeployItem} order 3 */
    @Override
    public byte getOrder() {
        return 0x3;
    }

    @Override
    protected void serializeCustom(final SerializerBuffer ser) {
        ser.writeString(getHash());
    }
}
