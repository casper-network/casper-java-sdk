package com.casper.sdk.service.impl.event;

import com.casper.sdk.model.event.DataType;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventType;
import lombok.*;

import java.util.Optional;

/**
 * The abstract base class implementation for all events
 *
 * @param <T> the type of the event data
 * @author ian@meywood.com
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@ToString(doNotUseGetters = true)
@EqualsAndHashCode(of = {"source", "id"})
abstract class AbstractEvent<T> implements Event<T> {

    /** The type of event RAW or POJO */
    private final EventType eventType;
    /** The type of the data field */
    private final DataType dataType;
    /** The source node of the event */
    private final String source;
    /** The ID of the event */
    private final Long id;
    /** The event data */
    private final T data;
    /** The version of the casper API that generate the event */
    private final String version;

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }
}
