package com.casper.sdk.service;

import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;

import java.io.Reader;
import java.net.URI;
import java.util.stream.Stream;

/**
 * The EventService interface.
 * <p>
 * Is instantiated using the static create methods within the interface eg:
 * <pre>final EventService eventService = EventService.create("http://localhost:18101");</pre>
 *
 * @author ian@meywood.com
 */
public interface EventService {

    /**
     * Reads a single event from a reader
     *
     * @param eventType   the type of event to read
     * @param eventTarget the target of the event JSON string or POJO
     * @param reader      the reader to read the event from
     * @param <T>         the type of the event
     * @param <E>         the type of the events content
     * @return the read event
     */
    <T, E extends Event<E>> Stream<T> readEvent(final EventType eventType,
                                                final EventTarget eventTarget,
                                                final Reader reader);


    /**
     * Reads a stream of events from a node
     * *
     *
     * @param eventType   the type of event to read
     * @param eventTarget the target of the event JSON string or POJO
     * @param <T>         the type of the event
     * @param <E>         the type of the events content
     * @return the read event
     */
    <T, E extends Event<E>> Stream<T> readEventStream(final EventType eventType,
                                                      final EventTarget eventTarget,
                                                      final Long startFrom);


    /**
     * Creates a new EventService for the specified host including protocol and port
     *
     * @param host the host including protocol and port eg: http:/localhost:18101
     * @return the event service for the specified host
     */
    static EventService usingPeer(final String host) {
        return EventServiceFactory.create(host);
    }

    /**
     * Creates a new EventService for the specified host including protocol and port
     *
     * @param uri the uri of the host to connect to
     * @return the event service for the specified host
     */
    static EventService usingPeer(final URI uri) {
        return EventServiceFactory.create(uri);
    }
}
