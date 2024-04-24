package com.casper.sdk.model.deploy.executabledeploy;

import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.*;

/**
 *  Executable Deploy Item containing the StoredVersionedContractByName.
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
@JsonTypeName("StoredVersionedContractByName")
public class StoredVersionedContractByName extends AbstractStoredVersionedContract {

    /** Contract Name */
    private String name;

    /** {@link ExecutableDeployItem} order 4 */
    @Override
    public byte getOrder() {
        return 0x4;
    }

    @Override
    protected void serializeCustom(final SerializerBuffer ser) {
        ser.writeString(getName());
    }
}
