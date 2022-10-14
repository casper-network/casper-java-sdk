package com.casper.sdk.service;

import com.casper.sdk.model.event.Event;

import java.util.function.Consumer;

/**
 * The class that is used to consume events from an SSE stream
 *
 * @author ian@meywood.com
 */
public abstract class EventConsumer<DataT> implements Consumer<Event<DataT>> {

    private boolean stop;

    /**
     * Tells the consumer to stop accepting events
     */
    void stop() {
        this.stop = true;
    }

    /**
     * Indicates if the consumer has requested to stop processing events
     *
     * @return true if event processing should stop otherwise false
     */
    public boolean isStop() {
        return stop;
    }
}
