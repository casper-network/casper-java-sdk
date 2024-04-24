package com.casper.sdk.model.deploy.executabledeploy;

import com.casper.sdk.model.deploy.NamedArg;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.*;
import org.bouncycastle.util.encoders.Hex;

import java.util.List;

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
@NoArgsConstructor
@JsonTypeName("StoredVersionedContractByHash")
public class StoredVersionedContractByHash extends AbstractStoredVersionedContract {

    @Builder
    public StoredVersionedContractByHash(final String hash, final Long version, final String entryPoint, final List<NamedArg<?>> args) {
        super(version, entryPoint, args);
        this.hash = hash;
    }

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
        ser.writeByteArray(Hex.decode(getHash()));
    }
}
