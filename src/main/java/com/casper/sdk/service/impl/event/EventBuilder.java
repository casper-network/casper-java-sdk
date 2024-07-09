package com.casper.sdk.service.impl.event;


import com.casper.sdk.exception.CasperClientException;
import com.casper.sdk.model.event.Event;
import com.casper.sdk.model.event.EventData;
import com.casper.sdk.model.event.EventTarget;
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

    public static final String API_VERSION = "ApiVersion";
    public static final String SHUTDOWN = "\"Shutdown\"";
    /** The most recent ID read from an event stream */
    private Long id;
    /** The line of data read from an event stream */
    private String data;
    /** The target type of the event to build */
    private final EventTarget eventTarget;
    private final String source;
    /** Indicates if an ID line is expected for the current data line */
    private boolean idExpected = true;
    /** Used to build the pojo model objects from the JSON in the data line */
    private final ObjectMapper mapper;
    /** The version of the node the event was generated with */
    private String version;

    EventBuilder(final EventTarget eventTarget, final String source) {
        this.eventTarget = eventTarget;
        this.source = source;
        mapper = new ObjectMapper();
    }

    boolean isComplete() {
        return (id != null || !idExpected) && data != null;
    }

    boolean processLine(final String id, final String data) {
        try {

            // Remove any leading or trailing whitespace
            final String trimmed = data.trim();

            if (data.startsWith("{")) {
                this.data = trimmed;
                this.id = getId(id);
                idExpected = isIdExpected(trimmed);
            }

            if (isApiVersion(trimmed)) {
                this.version = getVersion(trimmed);
            }

            return isComplete();
        } catch (Exception e) {
            throw new CasperClientException(e.getMessage(), e);
        }
    }


    private String getVersion(final String line) {
        // remove all but version number from: data:{"ApiVersion":"1.0.0"}
        return line.replace("{\"ApiVersion\":\"", "")
                .replace("\"}", "");
    }

    private boolean isIdExpected(String line) {
        return !isApiVersion(line);
    }

    private Long getId(final String id) {
        return id != null ? Long.valueOf(id): null;
    }

    private boolean isApiVersion(final String line) {
        return line.contains(API_VERSION);
    }

    @SuppressWarnings("rawtypes")
    <T extends Event> T buildEvent() {

        final T event;
        if (this.eventTarget == EventTarget.RAW) {
            //noinspection unchecked
            event = (T) new RawEvent(source, id, data, version);
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
            final String value = data.trim();

            final EventRoot<T> root;
            if (SHUTDOWN.equals(value)) {
                // Shutdown is text value, so we need to manually convert
                //noinspection unchecked
                root = new EventRoot<>((T) new Shutdown());
            } else {
                //noinspection unchecked
                root = mapper.readValue(value, EventRoot.class);
            }
            event = new PojoEvent<>(source, id, EventRoot.getData(root), version);
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
