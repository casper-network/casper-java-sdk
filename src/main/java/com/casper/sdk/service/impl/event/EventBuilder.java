package com.casper.sdk.service.impl.event;


import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.event.EventTarget;
import com.casper.sdk.model.event.EventType;
import com.casper.sdk.model.event.shutdown.Shutdown;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;

/**
 * Builds events from the lines read from a node
 *
 * @author ian@meywood.com
 */
final class EventBuilder {

    public static final String DATA = "data:";
    public static final String ID = "id:";
    public static final String API_VERSION = "ApiVersion";
    public static final String SHUTDOWN = "\"Shutdown\"";
    /** The most recent ID read from an event stream */
    private Long id;
    /** The line of data read from an event stream */
    private String data;
    /** The type of data being read */
    private final EventType eventType;
    /** The target type of the event to build */
    private final EventTarget eventTarget;
    private final String source;
    /** Indicates if an ID line is expected for the current data line */
    private boolean idExpected = true;
    /** Used to build the pojo model objects from the JSON in the data line */
    private final ObjectMapper mapper;

    EventBuilder(final EventType eventType, final EventTarget eventTarget, final String source) {
        this.eventType = eventType;
        this.eventTarget = eventTarget;
        this.source = source;
        mapper = new ObjectMapper();
    }

    boolean isComplete() {
        return (id != null || !idExpected) && data != null;
    }

    boolean processLine(final String line) {
        try {

            if (line.startsWith(DATA)) {
                this.data = line;
                idExpected = isIdExpected(line);
            }

            if (isId(line)) {
                this.id = getId(line);
            }

            return isComplete();
        } catch (Exception e) {
            throw new CasperClientException(e.getMessage(), e);
        }
    }

    private boolean isIdExpected(String line) {
        return !isApiVersion(line);
    }

    private boolean isId(final String line) {
        return line.startsWith(ID);
    }

    private Long getId(final String line) {
        return Long.valueOf(line.substring(ID.length()));
    }

    private boolean isApiVersion(final String line) {
        return line.contains(API_VERSION);
    }

    @SuppressWarnings("rawtypes")
    <T extends Event> T buildEvent() {

        final T event;
        if (this.eventTarget == EventTarget.RAW) {
            //noinspection unchecked
            event = (T) new RawEvent(eventType, source, id, data);
        } else if (eventTarget == EventTarget.POJO) {
            //noinspection unchecked
            event = (T) buildPojoEvent();
        } else {
            throw new IllegalArgumentException("Unsupported eventTarget: " + eventTarget);
        }
        reset();
        return event;
    }

    @NotNull
    private <T extends EventData> PojoEvent<T> buildPojoEvent() {
        final PojoEvent<T> event;
        try {
            // Remove the "data:" prefix from the line
            final String value = data.substring(DATA.length()).trim();

            final EventRoot<T> root;
            if (SHUTDOWN.equals(value)) {
                // Shutdown is text value, so we need to manually convert
                //noinspection unchecked
                root = new EventRoot<>((T) new Shutdown());
            } else {
                //noinspection unchecked
                root = mapper.readValue(value, EventRoot.class);
            }
            event = new PojoEvent<>(eventType, source, id, EventRoot.getData(root));
        } catch (JsonProcessingException e) {
            throw new CasperClientException("Error building POJO event for: " + data, e);
        }
        return event;
    }

    private void reset() {
        this.data = null;
        this.id = null;
        this.idExpected = true;
    }
}
