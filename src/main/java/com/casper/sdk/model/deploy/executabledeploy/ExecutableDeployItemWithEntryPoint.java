package com.casper.sdk.model.deploy.executabledeploy;

/**
 * Abstract Executable Deploy Item containing the runtime args of the contract.
 * It can be any of the following types:
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @see StoredContractByHash
 * @see StoredContractByName
 * @see StoredVersionedContractByHash
 * @see StoredVersionedContractByName
 * @see Transfer
 * @since 2.0.0
 */
public interface ExecutableDeployItemWithEntryPoint extends ExecutableDeployItem {
    String getEntryPoint();
}
