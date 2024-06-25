package com.casper.sdk.model.common;


import com.casper.sdk.model.clvalue.serde.CasperSerializableObject;
import com.casper.sdk.model.clvalue.serde.Target;
import com.fasterxml.jackson.annotation.JsonValue;
import dev.oak3.sbs4j.SerializerBuffer;
import lombok.*;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.util.Objects;

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
public class Ttl implements CasperSerializableObject {
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
     * implements SerializableObject
     */
    @Override
    public void serialize(SerializerBuffer ser, Target target) {
        ser.writeI64(getTtl());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ttl ttl1 = (Ttl) o;
        return Objects.equals(ttl, ttl1.ttl);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(ttl);
    }

    @Override
    public String toString() {
        return ttl;
    }
}
