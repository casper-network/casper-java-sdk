package com.casper.sdk.model.transaction.target;

import com.casper.sdk.model.common.Digest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Optional;

/**
 * The address and optional version identifying the package.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ByPackageHash implements TransactionInvocationTarget {
    /** The package address. */
    private Digest addr;
    /** If `None`, the latest enabled version is implied. */
    @Getter(AccessLevel.NONE)
    @JsonProperty("version")
    private Long version;

    @JsonIgnore
    public Optional<Long> getVersion() {
        return Optional.ofNullable(version);
    }
}
