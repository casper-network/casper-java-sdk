package com.casper.sdk.service;

import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;

import java.net.URI;
import java.util.function.Consumer;

/**
 * The EventService interface.
 * <p>
 * Is instantiated using the static create methods within the interface eg:
 * <pre>final EventService eventService = EventService.usingPeer("<a href="http://localhost:18101">...</a>");</pre>
 *
 * @author ian@meywood.com
 */
public interface EventService {

    /**
     * Reads a stream of events from a node
     * *
     *
     * @param eventType     the type of event to read
     * @param eventTarget   the target of the event JSON string or POJO
     * @param startFrom     the optional event to start streaming from, if not present only obtains new events
     * @param eventConsumer the consumer of the events
     * @param <EventT>      the type of the event
     */
    <EventT extends Event<?>> AutoCloseable consumeEvents(final EventType eventType,
                                                          final EventTarget eventTarget,
                                                          final Long startFrom,
                                                          final Consumer<EventT> eventConsumer);

    /**
     * Creates a new EventService for the specified host including protocol and port
     *
     * @param uri the uri of the host to connect to must include protocol, hostname, and port number
     * @return the event service for the specified host
     */
    static EventService usingPeer(final URI uri) {
        return EventServiceFactory.create(uri);
    }
}
