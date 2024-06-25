package com.casper.sdk.model.transaction.target;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

/**
 * The execution target is a native operation (e.g. a transfer).
 * This is a placeholder for now.
 *
 * @author ian@meywood.com
 */
@JsonTypeName("Native")
@NoArgsConstructor
@Getter
@Setter
public class Native implements TransactionTarget {

    @JsonCreator
    public Native(String target) {
        this.target = target;
    }

    @JsonValue
    private String target;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return o != null && getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getClass().getSimpleName());
    }
}
