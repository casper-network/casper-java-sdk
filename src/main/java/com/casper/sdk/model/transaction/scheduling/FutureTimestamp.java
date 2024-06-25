package com.casper.sdk.model.transaction.scheduling;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Execution should be scheduled for the specified timestamp or later.
 *
 * @author ian@meywood.com
 */
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@JsonTypeName("FutureTimestamp")
public class FutureTimestamp implements TransactionScheduling {

    @JsonValue
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    private String futureTimestamp;

    @JsonIgnore
    public Date asDate() {
        return futureTimestamp != null ? new DateTime(futureTimestamp).toDate() : null;
    }

}
