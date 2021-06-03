package com.casper.sdk.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class DeployParams {
    /** The default ttl value is 1800000, which is 30 minutes in milliseconds */
    public static final long DEFAULT_TTL = 1800000;

    /** The public key of the account */
    private final PublicKey accountPublicKey;
    /** Name of the chain, to avoid the `Deploy` from being accidentally or maliciously included in a different chain. */
    private final String chainName;
    /** Conversion rate between the cost of Wasm opcodes and the motes sent by the payment code. */
    private final int gasPrice;
    /** Time that the `Deploy` will remain valid for, in milliseconds. The default value is 1800000, which is 30 minutes */
    private final long ttl;
    /** If `timestamp` is empty, the current time will be used. Note that timestamp is UTC, not local. */
    private final String timestamp;
    /** Hex-encoded `Deploy` hashes of deploys which must be executed before this one. */
    private final List<Digest> dependencies;

    /**
     * Container for `Deploy` construction options.
     */
    public DeployParams(final PublicKey accountPublicKey,
                        final String chainName,
                        final int gasPrice,
                        final Long timestamp,
                        final Long ttl,
                        final List<Digest> dependencies) {
        this.accountPublicKey = accountPublicKey;
        this.chainName = chainName;
        this.gasPrice = gasPrice;
        if (timestamp == null) {
            this.timestamp = Instant.now().toString();
        } else {
            this.timestamp = Instant.ofEpochMilli(timestamp).toString();
        }
        this.ttl = ttl != null ? ttl : DEFAULT_TTL;

        // TODO encode dependencies as Base16
        this.dependencies = dependencies != null ? dependencies : new ArrayList<>();
    }

    public PublicKey getAccountPublicKey() {
        return accountPublicKey;
    }

    public String getChainName() {
        return chainName;
    }

    public int getGasPrice() {
        return gasPrice;
    }

    public long getTtl() {
        return ttl;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<Digest> getDependencies() {
        return dependencies;
    }
}
