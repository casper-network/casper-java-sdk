package com.casper.sdk.types;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.security.PublicKey;

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
     *
     * @param accountPublicKey the account public key
     * @param chainName        the name of the chain
     * @param gasPrice         the gas price
     * @param timestamp        the time stamp
     * @param ttl              the time to live
     * @param dependencies     the dependencies
     */
    public DeployParams(final java.security.PublicKey accountPublicKey,
                        final String chainName,
                        final Number gasPrice,
                        final Long timestamp,
                        final Long ttl,
                        final List<Digest> dependencies) {
        this.accountPublicKey = accountPublicKey;
        this.chainName = chainName;
        this.gasPrice = gasPrice != null ? gasPrice.intValue() : 1;
        if (timestamp == null) {
            this.timestamp = Instant.now().toString();
        } else {
            this.timestamp = Instant.ofEpochMilli(timestamp).toString();
        }
        this.ttl = ttl != null ? ttl : DEFAULT_TTL;
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
