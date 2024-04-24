package com.casper.sdk.model.deploy.executabledeploy;

import com.casper.sdk.model.deploy.NamedArg;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.*;

import java.util.List;

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
@Builder(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@NoArgsConstructor
@JsonTypeName("StoredVersionedContractByName")
public class StoredVersionedContractByName extends AbstractStoredVersionedContract {

    /** Contract Name */
    private String name;

    @Builder
    public StoredVersionedContractByName(final String name, final Long version, final String entryPoint, final List<NamedArg<?>> args) {
        super(version, entryPoint, args);
        this.name = name;
    }

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
