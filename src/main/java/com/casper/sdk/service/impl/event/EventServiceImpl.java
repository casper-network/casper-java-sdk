package com.casper.sdk.service.impl.event;

import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;
import com.casper.sdk.service.EventConsumer;
import com.casper.sdk.service.EventService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.time.Duration;
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

    /** The HTTP Client to connect to the node with */
    final OkHttpClient client;

    /**
     * Constructs a new {@link EventServiceImpl}, is private to prevent API users construction manually. To create the
     * service user the interface method {@link EventService#usingPeer(URI)}.
     *
     * @param uri the URL if the node to connect to must contain protocol, host and port number
     */
    private EventServiceImpl(final URI uri) {
        this.uri = uri;

        this.client = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofDays(1))
                .readTimeout(Duration.ofDays(1))
                .build();
    }

    @Override
    public <EventT extends Event<?>> void consumeEvents(final EventType eventType,
                                                        final EventTarget eventTarget,
                                                        final Long startFrom,
                                                        final Consumer<EventT> eventTConsumer) {
        try {
            //noinspection resource
            final Response response = this.client.newCall(
                    new Request.Builder()
                            .url(urlBuilder.buildUrl(uri, eventType, startFrom))
                            .header(ACCEPT, APPLICATION_JSON)
                            .header(CONTENT_TYPE, APPLICATION_JSON)
                            .get()
                            .build()
            ).execute();

            if (response.isSuccessful() && response.body() != null) {
                //noinspection ConstantConditions
                consumeEvent(eventType, eventTarget, new InputStreamReader(response.body().byteStream()), eventTConsumer);
            } else {
                throw new CasperClientException("No response from node " + this.uri);
            }
        } catch (IOException e) {
            throw new CasperClientException("Error executing request against node" + this.uri, e);
        }
    }

    private <EventT, DataT extends Event<DataT>> void consumeEvent(final EventType eventType,
                                                                   final EventTarget eventTarget,
                                                                   final Reader reader,
                                                                   final Consumer<EventT> consumer) {

        final EventBuilder eventBuilder = new EventBuilder(eventType, eventTarget, uri.toString());

        try {
            //noinspection unchecked
            new BufferedReader(reader)
                    .lines()
                    .filter(line -> throwOnStop(consumer))
                    .filter(eventBuilder::processLine)
                    .forEach(line -> consumer.accept(eventBuilder.buildEvent()));

        } catch (StopException e) {
            // The consumer asked to stop reading events
        }
    }

    private <EventT> boolean throwOnStop(final Consumer<EventT> consumer) {
        if (consumer instanceof EventConsumer && ((EventConsumer<?>) consumer).isStop()) {
            throw new StopException();
        }
        return true;
    }
}
