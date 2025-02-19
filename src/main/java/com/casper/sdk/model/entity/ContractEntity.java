package com.casper.sdk.model.entity;

import com.casper.sdk.model.contract.Contract;
import com.casper.sdk.model.contract.NamedKey;
import com.casper.sdk.model.contract.entrypoint.EntryPointV1;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Contract entity
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@JsonTypeName("Contract")
public class ContractEntity extends Contract implements StateEntity {

    public ContractEntity(final String packageHash,
                          final String wasmHash,
                          final List<EntryPointV1> entryPoints,
                          final List<NamedKey> namedKeys,
                          final String protocolVersion) {
        super(packageHash, wasmHash, entryPoints, namedKeys, protocolVersion);
    }
}
