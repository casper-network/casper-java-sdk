package com.casper.sdk.model.event;

import java.util.Optional;

/**
 * The interface implemented by all events that are read from a nodes event stream
 *
 * @author ian@meywood.com
 */
public interface Event<T> {

    /**
     * The node URL that is the source of the event
     *
     * @return the  URL of the source node
     */
    String getSource();

    /**
     * The type of the event
     *
     * @return the type of the event
     */
    EventType getEventType();

    /**
     * The event payload a JSON string or Pojo
     *
     * @return the event payload
     */
    T getData();

    /**
     * The optional ID of the event
     *
     * @return the optional ID
     */
    Optional<Long> getId();
}
