package com.casper.sdk.model.status;

import com.casper.sdk.exception.CasperInvalidStateException;
import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Holds the chainspec data
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 2.2.0
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChainspecData {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ChainspecBytes {
        /**
         * The chainspec bytes (chainspec.toml)
         */
        @JsonProperty("chainspec_bytes")
        private Digest chainspecBytes;
        /**
         * The genesis accounts bytes (accounts.toml)
         */
        @JsonProperty("maybe_genesis_accounts_bytes")
        private Digest genesisAccountsBytes;
        /**
         * The global state bytes (global_state.toml)
         */
        @JsonProperty("maybe_global_state_bytes")
        private Digest globalStateBytes;
    }

    /**
     * The RPC API version
     */
    @JsonProperty("api_version")
    private String apiVersion;

    /**
     * The chainspec bytes object
     */
    @JsonProperty("chainspec_bytes")
    private ChainspecBytes chainspec;

    public void saveChainspec(Path path) throws IOException {
        Files.write(path, chainspec.getChainspecBytes().getDigest());
    }

    public String readChainspecBytesToString() {
        if (chainspec.getChainspecBytes() != null && chainspec.getChainspecBytes().getDigest() != null) {
            return new String(chainspec.getChainspecBytes().getDigest());
        }
        return null;
    }

    public void saveGenesisAccounts(Path path) throws IOException {
        if (chainspec.getGenesisAccountsBytes() != null && chainspec.getGenesisAccountsBytes().getDigest() != null) {
            Files.write(path, chainspec.getGenesisAccountsBytes().getDigest());
        } else {
            throw new CasperInvalidStateException("Genesis accounts bytes not found");
        }
    }

    public String readGenesisAccountsBytesToString() {
        if (chainspec.getGenesisAccountsBytes() != null && chainspec.getGenesisAccountsBytes().getDigest() != null) {
            return new String(chainspec.getGenesisAccountsBytes().getDigest());
        } else {
            throw new CasperInvalidStateException("Genesis accounts bytes not found");
        }
    }

    public void saveGlobalState(Path path) throws IOException {
        if (chainspec.getGlobalStateBytes() != null && chainspec.getGlobalStateBytes().getDigest() != null) {
            Files.write(path, chainspec.getGlobalStateBytes().getDigest());
        } else {
            throw new CasperInvalidStateException("Global state bytes not found");
        }
    }

    public String readGlobalStateBytesToString() {
        if (chainspec.getGlobalStateBytes() != null && chainspec.getGlobalStateBytes().getDigest() != null) {
            return new String(chainspec.getGlobalStateBytes().getDigest());
        } else {
            throw new CasperInvalidStateException("Global state bytes not found");
        }
    }
}
