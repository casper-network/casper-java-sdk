package com.casper.sdk.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Domain type: header information associated with a deploy.
 */
public class DeployHeader {

    /** Public key of account dispatching deploy to a node. */
    private final PublicKey account;
    // TODO convert to date
    /** Timestamp at point of deploy creation as an ISO8601 date time with timezone */
    private final long timestamp;
    // TODO convert to long in milliseconds?
    /** Time interval after which the deploy will no longer be considered for processing by a node. eg 3m */
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
        this(account, toEpocMs(timestamp), getTtlLong(ttl), gasPrice, bodyHash, dependencies, chainName);
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

    @JsonIgnore
    public static long getTtlLong(final String strTtl) {
        if (strTtl != null) {

            final Pattern p = Pattern.compile("\\p{Alpha}");
            final Matcher m = p.matcher(strTtl);
            final int unitIndex;
            if (m.find()) {
                unitIndex = m.start();
            } else {
                unitIndex = strTtl.length() - 1;
            }

            final String unit = strTtl.substring(unitIndex);

            final long value = Long.parseLong(strTtl.substring(0, unitIndex));

            final long multiplier = switch (unit) {
                case "d" -> 24 * 60L * 60L * 1000L;
                case "h" -> 60L * 60L * 1000L;
                case "m" -> 60L * 1000L;
                case "s" -> 1000L;
                default -> 1L;
            };

            return value * multiplier;

        }
        return 0L;
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
        // TODO allow for other time units depending on value
        return (ttl / 60000) + "m";
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
