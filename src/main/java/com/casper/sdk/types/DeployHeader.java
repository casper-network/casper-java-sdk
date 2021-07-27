package com.casper.sdk.types;

import com.casper.sdk.service.serialization.util.TtlUtils;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.*;
import java.util.List;

import static com.casper.sdk.service.serialization.util.TtlUtils.toTtlStr;

/**
 * Type: header information associated with a deploy.
 */
public class DeployHeader {

    /** Public key of account dispatching deploy to a node. */
    private final PublicKey account;
    /**
     * Timestamp at point of deploy creation as an ISO8601 date time  converted to  milliseconds since the UNIX
     * epoch.with UTC timezone
     */
    private final long timestamp;
    /**
     * The Time to live is defined as the amount of time for which deploy is considered valid. The ttl serializes in the
     * same manner as the timestamp.
     */
    private final long ttl;
    @JsonProperty("gas_price")
    private final Integer gasPrice;
    /** Hash of deploy payload. */
    @JsonProperty("body_hash")
    private final Digest bodyHash;
    /** Set of deploys that must be executed prior to this one. */
    private final List<Digest> dependencies;
    /** Name of target chain to which deploy will be dispatched. */
    @JsonProperty("chain_name")
    private final String chainName;

    @JsonCreator
    public DeployHeader(@JsonProperty("account") final PublicKey account,
                        @JsonProperty("timestamp") final String timestamp,
                        @JsonProperty("ttl") final String ttl,
                        @JsonProperty(value = "gas_price", defaultValue = "null") final Integer gasPrice,
                        @JsonProperty("body_hash") final Digest bodyHash,
                        @JsonProperty("dependencies") final List<Digest> dependencies,
                        @JsonProperty("chain_name") final String chainName) {
        this(account, toEpocMs(timestamp), TtlUtils.getTtlLong(ttl), gasPrice, bodyHash, dependencies, chainName);
    }

    public DeployHeader(final PublicKey account,
                        final long timestamp,
                        final long ttl,
                        final Integer gasPrice,
                        final Digest bodyHash,
                        final List<Digest> dependencies,
                        final String chainName) {
        this.account = account;
        this.timestamp = timestamp;
        this.ttl = ttl;
        this.gasPrice = gasPrice;
        this.bodyHash = bodyHash;
        this.dependencies = dependencies;
        this.chainName = chainName;
    }

    public static long toEpocMs(final String isoDateTime) {
        final ZonedDateTime zonedDateTime = OffsetDateTime.parse(isoDateTime).toZonedDateTime();
        return zonedDateTime.toInstant().toEpochMilli();
    }

    public PublicKey getAccount() {
        return account;
    }

    @JsonIgnore
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Obtains the timestamp as an ISO string for writing to JSON
     *
     * @return the timestamp as an ISO 8601 date format in the UTC timezone
     */
    @JsonProperty("timestamp")
    public String getTimeStampIso() {
        final ZonedDateTime zonedDateTime = Instant.ofEpochMilli(timestamp).atZone(ZoneId.from(ZoneOffset.UTC));
        return zonedDateTime.toString();
    }

    @JsonIgnore
    public long getTtl() {
        return ttl;
    }

    /**
     * Obtains the ttl as a string for writing to JSON
     *
     * @return the ttl as a String
     */
    @JsonProperty("ttl")
    public String getTtlStr() {
        return toTtlStr(ttl);
    }

    public Integer getGasPrice() {
        return gasPrice;
    }

    public Digest getBodyHash() {
        return bodyHash;
    }

    public List<Digest> getDependencies() {
        return dependencies;
    }

    public String getChainName() {
        return chainName;
    }
}
