package com.casper.sdk.exception;

import javax.ws.rs.sse.SseEvent;

/**
 * Exception that is thrown whilst processing an SseEvent
 *
 * @author ian@meywood.com
 */
public class CasperSseProcessingException extends RuntimeException {

    /** The SseEvent being processed when the error occurred */
    private final SseEvent event;

    public CasperSseProcessingException(final Throwable cause, final SseEvent event) {
        super(cause);
        this.event = event;
    }

    public <T extends SseEvent> T getEvent() {
        //noinspection unchecked
        return (T) event;
    }
}
