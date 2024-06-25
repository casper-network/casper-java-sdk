package com.casper.sdk.model.transaction.scheduling;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

/**
 * Execution should be scheduled for the specified era.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonTypeName("FutureEra")
public class FutureEra implements TransactionScheduling {
    @JsonValue
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger eraId;

    @JsonCreator
    public FutureEra(final BigInteger eraId) {
        this.eraId = eraId;
    }

}
