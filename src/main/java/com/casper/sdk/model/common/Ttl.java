package com.casper.sdk.model.common;


import com.casper.sdk.exception.CLValueEncodeException;
import com.casper.sdk.exception.DynamicInstanceException;
import com.casper.sdk.exception.NoSuchTypeException;
import com.casper.sdk.model.clvalue.encdec.CLValueEncoder;
import com.casper.sdk.model.clvalue.encdec.interfaces.EncodableValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.IOException;

/**
 * TTL wrapper
 *
 * @author Alexandre Carvalho
 * @author Andre Bertolace
 * @since 0.0.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ttl implements EncodableValue {
    @JsonValue
    private String ttl;

    public Long getTtl() {
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays().appendSuffix("d")
                .appendHours().appendSuffix("h")
                .appendMinutes().appendSuffix("m")
                .toFormatter();

        Period p = formatter.parsePeriod(ttl);
        return p.toStandardDuration().getMillis();
    }

    /**
     * Implements EncodableValue
     */
    @Override
    public void encode(CLValueEncoder clve, boolean encodeType)
            throws IOException, CLValueEncodeException, DynamicInstanceException, NoSuchTypeException {
        clve.writeLong(getTtl());
    }
}
