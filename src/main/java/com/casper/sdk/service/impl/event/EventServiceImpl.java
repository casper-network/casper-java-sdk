package com.casper.sdk.service.impl.event;

import com.casper.sdk.exception.CasperSseProcessingException;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;
import com.casper.sdk.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.sse.InboundSseEvent;
import javax.ws.rs.sse.SseEventSource;
import java.net.URI;
import java.net.URL;
import java.util.function.Consumer;

/**
 * Package local to prevent construction.
 *
 * @author ian@meywood.com
 */
@SuppressWarnings("unused")
final class EventServiceImpl implements EventService {

    /** The mime type for JSON */
    private static final String APPLICATION_JSON = "application/json";
    public static final String ACCEPT = "Accept";
    public static final String CONTENT_TYPE = "Content-Type";

    /** Build the URLs for event get requests */
    private final EventUrlBuilder urlBuilder = new EventUrlBuilder();
    /** The URI of the node to request events from */
    private final URI uri;
    /** The SSE Client */
    private final Client sssClient = ClientBuilder.newClient();
    private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);


    /**
     * Constructs a new {@link EventServiceImpl}, is private to prevent API users construction manually. To create the
     * service user the interface method {@link EventService#usingPeer(URI)}.
     *
     * @param uri the URL if the node to connect to must contain protocol, host and port number
     */
    private EventServiceImpl(final URI uri) {
        this.uri = uri;
    }

    @Override
    public <EventT extends Event<?>> AutoCloseable consumeEvents(final EventType eventType,
                                                                 final EventTarget eventTarget,
                                                                 final Long startFrom,
                                                                 final Consumer<EventT> onEvent,
                                                                 final Consumer<Throwable> onFailure) {

        final URL url = urlBuilder.buildUrl(uri, eventType, startFrom);
        logger.info("Targeting SSE URL {}", url);
        final WebTarget target = sssClient.target(url.toString());
        final Response response = target.request("text/plain", "text/event-stream").get();
        final EventBuilder eventBuilder = new EventBuilder(eventType, eventTarget, target.getUri().toString());

        final SseEventSource source = SseEventSource.target(target).build();


        source.register((inboundSseEvent) -> {
            if (inboundSseEvent.readData() != null) {
                logger.info("SSE event id: {}, data: {}", inboundSseEvent.getId(), inboundSseEvent.readData());
                try {
                    consumeEvent(eventBuilder, inboundSseEvent, onEvent);
                } catch (Exception e) {
                    logger.error("error in consumeEvent", e);
                    onFailure.accept(new CasperSseProcessingException(e, inboundSseEvent));
                }
            }
        }, throwable -> {
            logger.error("SSE Event Error", throwable);
            onFailure.accept(throwable);
        });
        source.open();

        return source;
    }

    private <EventT extends Event<?>> void consumeEvent(final EventBuilder builder,
                                                        final InboundSseEvent event,
                                                        final Consumer<EventT> consumer) {

        if (builder.processLine(event.getId(), event.readData())) {
            consumer.accept(builder.buildEvent());
        }
    }

}
