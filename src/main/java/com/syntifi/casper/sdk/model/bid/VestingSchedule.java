package com.syntifi.casper.sdk.model.bid;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.syntifi.casper.sdk.annotation.ExcludeFromJacocoGeneratedReport;

import lombok.Data;

/**
 * Vesting schedule.
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Data
public class VestingSchedule {

    /**
     * release time in miliseconds
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
