package com.casper.sdk.model.bid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

/**
 * Vesting schedule.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VestingSchedule {

    /**
     * release time in milliseconds
     */
    @JsonProperty("initial_release_timestamp_millis")
    private BigInteger initialReleaseTimeStampMillis;

    /**
     * amount locked
     */
    @JsonIgnore
    private List<BigInteger> lockedAmounts;

    @JsonProperty("locked_amounts")
    @ExcludeFromJacocoGeneratedReport
    protected void setJsonLockedAmounts(final List<String> lockedAmounts) {
        List<BigInteger> list = new LinkedList<>();
        for (String string : lockedAmounts) {
            list.add(new BigInteger(string, 10));
        }
        this.lockedAmounts = list;
    }

    @JsonProperty("locked_amounts")
    @ExcludeFromJacocoGeneratedReport
    protected List<String> getJsonLockedAmounts() {
        List<String> list = new LinkedList<>();
        for (BigInteger bi : lockedAmounts) {
            list.add(bi.toString(10));
        }
        return list;
    }
}
