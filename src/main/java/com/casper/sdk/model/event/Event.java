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
     * The key name of the data field.
     *
     * @return the key name of the data field
     */
    DataType getDataType();

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

    /**
     * Obtains the API version of the event
     * @return the API version
     */
    String getVersion();
}
