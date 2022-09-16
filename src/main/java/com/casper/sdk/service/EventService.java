package com.casper.sdk.service;

import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;

import java.io.Reader;
import java.net.URI;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * The EventService interface.
 * <p>
 * Is instantiated using the static create methods within the interface eg:
 * <pre>final EventService eventService = EventService.create("<a href="http://localhost:18101">...</a>");</pre>
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
     * @param <EventT>    the type of the event
     * @param <DataT>     the type of the events data content
     * @return the read event
     */
    <EventT, DataT extends Event<DataT>> Stream<EventT> readEvent(final EventType eventType,
                                                                  final EventTarget eventTarget,
                                                                  final Reader reader);


    /**
     * Reads a stream of events from a node
     * *
     *
     * @param eventType     the type of event to read
     * @param eventTarget   the target of the event JSON string or POJO
     * @param startFrom     the optional event to start streaming from, if not present only obtains new events
     * @param eventConsumer the consumer of the events
     * @param <EventT>      the type of the event
     * @param <DataT>       the type of the events data content
     */
    <EventT, DataT extends Event<DataT>> void consumeEvents(final EventType eventType,
                                                            final EventTarget eventTarget,
                                                            final Long startFrom,
                                                            final Consumer<EventT> eventConsumer);


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
