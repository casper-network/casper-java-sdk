package com.casper.sdk.model.transaction.target;

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
public class ByPackageName implements TransactionInvocationTarget {
    /** the package name */
    private String name;
    /** If `None`, the latest enabled version is implied. */
    @Getter(AccessLevel.NONE)
    @JsonProperty("version")
    private Long version;

    @JsonIgnore
    public Optional<Long> getVersion() {
        return Optional.ofNullable(version);
    }
}
