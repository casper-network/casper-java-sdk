package com.casper.sdk.service.impl.event;

import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventType;

import java.util.Objects;
import java.util.Optional;

/**
 * The abstract base class implementation for all events
 *
 * @param <T> the type of the event data
 *
 * @author ian@meywood.com
 */
abstract class AbstractEvent<T> implements Event<T> {

    /**
     * The ID of the event
     */
    private final Long id;
    private final EventType eventType;
    /**
     * The event data
     */
    private final T data;

    protected AbstractEvent(final EventType eventType, final T data, final Long id) {
        this.eventType = eventType;
        this.data = data;
        this.id = id;
    }


    public EventType getEventType() {
        return eventType;
    }

    public T getData() {
        return data;
    }

    public Optional<Long> getId() {
        return Optional.ofNullable(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                ", eventType='" + eventType + '\'' +
                ", id=" + id +
                ", data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final AbstractEvent<?> that = (AbstractEvent<?>) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
